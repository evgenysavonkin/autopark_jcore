package org.evgenysav;

import java.util.Objects;

import static org.evgenysav.TechnicalSpecialist.*;

public class Vehicle implements Comparable<Vehicle> {
    private VehicleType type;
    private String modelName;
    private String registrationNumber;
    private int weight;
    private int manufactureYear;
    private int mileage;
    private Color color;
    private int tankVolume;

    public Vehicle() {

    }

    public Vehicle(VehicleType type, String modelName, String registrationNumber,
                   int weight, int manufactureYear, int mileage, Color color,
                   int tankVolume) {
        setType(type);
        setModelName(modelName);
        setRegistrationNumber(registrationNumber);
        setWeight(weight);
        setManufactureYear(manufactureYear);
        setMileage(mileage);
        setColor(color);
        setTankVolume(tankVolume);
    }

    public double getCalcTaxPerMonth() {
        return (weight * 0.0013) + (type.getTaxCoefficient() * 30) + 5;
    }

    @Override
    public int compareTo(Vehicle o) {
        if (manufactureYear == o.getManufactureYear()) {
            return mileage - o.getMileage();
        } else {
            return manufactureYear - o.getManufactureYear();
        }
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        if (validateVehicleType(type)) {
            this.type = type;
            return;
        }

        this.type = null;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        if (validateModelName(modelName)) {
            this.modelName = modelName;
            return;
        }

        this.modelName = "";
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        if (validateRegistrationNumber(registrationNumber)) {
            this.registrationNumber = registrationNumber;
            return;
        }

        this.registrationNumber = "";
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        if (validateWeight(weight)) {
            this.weight = weight;
            return;
        }

        this.weight = 0;
    }

    public int getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(int manufactureYear) {
        if (validateManufactureYear(manufactureYear)) {
            this.manufactureYear = manufactureYear;
            return;
        }

        this.manufactureYear = 0;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        if (validateMileage(mileage)) {
            this.mileage = mileage;
            return;
        }

        this.mileage = 0;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        if (validateColor(color)) {
            this.color = color;
            return;
        }

        this.color = null;
    }

    public int getTankVolume() {
        return tankVolume;
    }

    public void setTankVolume(int tankVolume) {
        this.tankVolume = tankVolume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(type, vehicle.type) && Objects.equals(modelName, vehicle.modelName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, modelName);
    }

    @Override
    public String toString() {
        return type + "," + modelName + "," + registrationNumber + "," + weight + "," + manufactureYear
                + "," + mileage + "," + color + "," + tankVolume + "," + getCalcTaxPerMonth();
    }
}
