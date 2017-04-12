package com.company.rentshop;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static com.company.rentshop.Commands.*;

public class Shop {

    private static String filePath;

    private Map<SportEquipment, Integer> goods;

    private Map<SportEquipment, Integer> rentedGoods;

    private RentUnit rentUnit;

    private Scanner scanner;

    public Shop() {
        rentedGoods = new HashMap<>();
        rentUnit = new RentUnit();
        scanner = new Scanner(System.in);
    }

    public void loadData(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        goods = new HashMap<>();
        while (reader.ready()) {
            String line = reader.readLine().replace("\uFEFF", "");
            String[] tokens = line.split(";");
            Category category = Category.valueOf(tokens[0]);
            String title = tokens[1];
            int price = Integer.parseInt(tokens[2]);
            SportEquipment sportEquipment = new SportEquipment(category, title, price);
            if (goods.containsKey(sportEquipment)) {
                goods.put(sportEquipment, goods.get(sportEquipment) + 1);
            } else {
                goods.put(sportEquipment, 1);
            }
        }
    }

    private void showMainMenu() {
        System.out.println();
        System.out.println("Choose action:");
        System.out.println("1. Show available goods.");
        System.out.println("2. Search good.");
        System.out.println("3. Show goods in a shopping cart.");
        System.out.println("4. Show rented goods.");
        System.out.println("0. Exit.");
        System.out.println();
    }

    private int getUserInput() {
        System.out.println("Enter command:");
        try {
            String input = scanner.nextLine();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("You should enter a command number!");
        }
        return ERROR_COMMAND;
    }

    private void searchAndAddGood() {
        System.out.println("Type the desired good title:");
        String title = scanner.nextLine();
        SportEquipment unit = searchUnit(title);
        if (unit == null) {
            System.out.println("Good not found.");
        } else if (goods.get(unit) == 0) {
            System.out.println("Good currently not available.");
        } else {
            System.out.println("Found: " + unit.getTitle());
            System.out.println("1. Add to cart.");
            System.out.println("2. To main menu.");
            int command = getUserInput();
            if (command == 1) {
                addGoodToRentUnit(unit);
            }
        }
    }

    private void addGoodToRentUnit(SportEquipment unit) {
        boolean isAdded = rentUnit.addGood(unit);
        System.out.println(isAdded ? unit.getTitle() + " successfully added." : "Cart is full.");
        if (isAdded) {
            goods.put(unit, goods.get(unit) - 1);
            if (rentedGoods.containsKey(unit)) {
                rentedGoods.put(unit, rentedGoods.get(unit) + 1);
            } else {
                rentedGoods.put(unit, 1);
            }
        }
    }

    private SportEquipment searchUnit(String title) {
        for (Map.Entry<SportEquipment, Integer> entry : goods.entrySet()) {
            if (entry.getKey().getTitle().toLowerCase().contains(title.toLowerCase())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void showAvailable() {
        if (goods.isEmpty()) {
            System.out.println("No goods available.");
        } else {
            for (Map.Entry<SportEquipment, Integer> entry : goods.entrySet()) {
                System.out.println("SportEquipment: " + entry.getKey().getTitle() + ", Quantity: " + entry.getValue());
            }
        }
        System.out.println("Press any key to continue...");
        scanner.nextLine();
    }

    public void showRented() {
        if (rentedGoods.isEmpty()) {
            System.out.println("No goods rented.");
        } else {
            for (Map.Entry<SportEquipment, Integer> entry : rentedGoods.entrySet()) {
                System.out.println("SportEquipment: " + entry.getKey().getTitle() + ", Quantity: " + entry.getValue());
            }
        }
        System.out.println("Press any key to continue...");
        scanner.nextLine();
    }

    private void showRentUnit() {
        if (rentUnit.getSize() == 0) {
            System.out.println("Cart is empty.");
        } else {
            for (int i = 0; i < rentUnit.getSize(); i++) {
                SportEquipment unit = rentUnit.getUnits()[i];
                System.out.println("Category: " + unit.getCategory().toString() + ", Title: " + unit.getTitle() +
                        ", Price: " + unit.getPrice());
            }
        }
        System.out.println("Press any key to continue...");
        scanner.nextLine();
    }

    public void run() {
        try {
            loadData(filePath);
        } catch (NumberFormatException e) {
            System.out.println("Error parsing price from data file: " + filePath);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error parsing data file, incorrect number of columns: " + filePath);
        } catch (IOException e) {
            System.out.println("Error reading data file: " + filePath);
        }
        System.out.println(Messages.WELCOME_MESSAGE);
        showMainMenu();
        int command = getUserInput();
        while (command != EXIT_COMMAND) {
            switch (command) {
                case SHOW_AVAILABLE_COMMAND:
                    showAvailable();
                    break;
                case SEARCH_UNIT_COMMAND:
                    searchAndAddGood();
                    break;
                case SHOW_RENT_UNIT_COMMAND:
                    showRentUnit();
                    break;
                case SHOW_RENTED_COMMAND:
                    showRented();
                    break;
                case ERROR_COMMAND:
                    System.out.println("Try again.");
                default:
                    System.out.println("Unknown command. Try again.");
            }
            showMainMenu();
            command = getUserInput();
        }

    }

    public static void main(String[] args) {

        filePath = "d:\\SportEquipment.txt";//args[0];
        new Shop().run();
    }

}
