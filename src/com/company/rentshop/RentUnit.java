package com.company.rentshop;

public class RentUnit {

    private SportEquipment[] units;
    private int size;

    public RentUnit() {
        units = new SportEquipment[3];
    }

    public RentUnit(SportEquipment[] units) {
        this.units = units;
    }

    public boolean addGood(SportEquipment unit) {
        if (size >= 3) {
            return false;
        }
        units[size++] = unit;
        return true;
    }

    public SportEquipment[] getUnits() {
        return units;
    }

    public void setUnits(SportEquipment[] units) {
        this.units = units;
    }

    public int getSize() {
        return size;
    }
}
