package org.evgenysav;

import org.evgenysav.exceptions.NotVehicleException;

import java.util.List;
import java.util.Objects;

import static org.evgenysav.TechnicalSpecialist.*;

public class Vehicle implements Comparable<Vehicle> {
    private int vehicleId;
    private int vehicleTypeId;
    private List<Rent> machineOrders;
    private Startable startable;
    private VehicleType type;
    private String modelName;
    private String registrationNumber;
    private int weight;
    private int manufactureYear;
    private int mileage;
    private Color color;
    private int tankVolume;
    private boolean isRented;
    private long defectCount;

    public Vehicle() {
    }

    public Vehicle(Startable startable, VehicleType type, String modelName, String registrationNumber,
                   int weight, int manufactureYear, int mileage, Color color,
                   int tankVolume) throws NotVehicleException {
        setStartable(startable);
        setType(type);
        setModelName(modelName);
        setRegistrationNumber(registrationNumber);
        setWeight(weight);
        setManufactureYear(manufactureYear);
        setMileage(mileage);
        setColor(color);
        setTankVolume(tankVolume);
    }

    public Vehicle(int vehicleId, int vehicleTypeId, Startable startable, VehicleType type, String modelName, String registrationNumber,
                   int weight, int manufactureYear, int mileage, Color color,
                   int tankVolume) throws NotVehicleException {
        this.vehicleId = vehicleId;
        this.vehicleTypeId = vehicleTypeId;
        setStartable(startable);
        setType(type);
        setModelName(modelName);
        setRegistrationNumber(registrationNumber);
        setWeight(weight);
        setManufactureYear(manufactureYear);
        setMileage(mileage);
        setColor(color);
        setTankVolume(tankVolume);
    }

    //for debug only
    public Vehicle(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Vehicle(String modelName) {
        this.modelName = modelName;
    }

    public double getTotalIncome() {
        return machineOrders.stream().mapToDouble(Rent::getRentCost).sum();
    }

    public double getTotalProfit() {
        return getTotalIncome() - getCalcTaxPerMonth();
    }

    public double getCalcTaxPerMonth() {
        return (weight * 0.0013) + (type.getTaxCoefficient() * startable.getTaxPerMonth() * 30) + 5;
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

    public void setType(VehicleType type) throws NotVehicleException {
        if (validateVehicleType(type)) {
            this.type = type;
            return;
        }

        this.type = null;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) throws NotVehicleException {
        if (validateModelName(modelName)) {
            this.modelName = modelName;
            return;
        }

        this.modelName = "";
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) throws NotVehicleException {
        if (validateRegistrationNumber(registrationNumber)) {
            this.registrationNumber = registrationNumber;
            return;
        }

        this.registrationNumber = "";
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) throws NotVehicleException {
        if (validateWeight(weight)) {
            this.weight = weight;
            return;
        }

        this.weight = 0;
    }

    public int getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(int manufactureYear) throws NotVehicleException {
        if (validateManufactureYear(manufactureYear)) {
            this.manufactureYear = manufactureYear;
            return;
        }

        this.manufactureYear = 0;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) throws NotVehicleException {
        if (validateMileage(mileage)) {
            this.mileage = mileage;
            return;
        }

        this.mileage = 0;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) throws NotVehicleException {
        if (validateColor(color)) {
            this.color = color;
            return;
        }

        this.color = null;
    }

    public Startable getStartable() {
        return startable;
    }

    public void setStartable(Startable startable) throws NotVehicleException {
        if (startable == null) {
            return;
        }

        if (startable instanceof ElectricalEngine o) {
            if (validateElectricalEngine(o)) {
                this.startable = startable;
                return;
            }

            this.startable = null;
        }
        if (startable instanceof DieselEngine o) {
            if (validateDieselEngine(o)) {
                this.startable = startable;
                return;
            }

            this.startable = null;
        }
        if (startable instanceof GasolineEngine o) {
            if (validateGasolineEngine(o)) {
                this.startable = startable;
                return;
            }

            this.startable = null;
        }
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public List<Rent> getMachineOrders() {
        return machineOrders;
    }

    public void setMachineOrders(List<Rent> machineOrders) {
        this.machineOrders = machineOrders;
    }

    public int getTankVolume() {
        return tankVolume;
    }

    public void setTankVolume(int tankVolume) {
        this.tankVolume = tankVolume;
    }

    public int getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(int vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public boolean isRented() {
        return isRented;
    }

    public void setRented(boolean rented) {
        isRented = rented;
    }

    public long getDefectCount() {
        return defectCount;
    }

    public void setDefectCount(long defectCount) {
        this.defectCount = defectCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return vehicleId == vehicle.vehicleId && vehicleTypeId == vehicle.vehicleTypeId &&
                weight == vehicle.weight && manufactureYear == vehicle.manufactureYear &&
                mileage == vehicle.mileage && tankVolume == vehicle.tankVolume &&
                isRented == vehicle.isRented && Objects.equals(machineOrders,
                vehicle.machineOrders) && Objects.equals(startable, vehicle.startable) &&
                Objects.equals(type, vehicle.type) && Objects.equals(modelName, vehicle.modelName) &&
                Objects.equals(registrationNumber, vehicle.registrationNumber) && color == vehicle.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleId, vehicleTypeId, machineOrders, startable, type, modelName, registrationNumber,
                weight, manufactureYear, mileage, color, tankVolume, isRented);
    }

    @Override
    public String toString() {
        if (startable == null) {
            return modelName;
        }

        return startable + "," + type + "," + modelName + "," + registrationNumber + "," + weight + "," + manufactureYear
                + "," + mileage + "," + color + "," + tankVolume + "," + String.format("%.3f", getCalcTaxPerMonth());
    }
}
