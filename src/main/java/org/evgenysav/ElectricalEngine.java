package org.evgenysav;

import java.util.Objects;

public class ElectricalEngine extends AbstractEngine {

    private double batterySize;
    private double electricityConsumption;

    public ElectricalEngine(double batterySize, double electricityConsumption) {
        super("Electrical", 0.1);
        this.batterySize = batterySize;
        this.electricityConsumption = electricityConsumption;
    }

    @Override
    public double getMaxKilometers() {
        return batterySize / electricityConsumption;
    }

    public double getBatterySize() {
        return batterySize;
    }

    public void setBatterySize(double batterySize) {
        this.batterySize = batterySize;
    }

    public double getElectricityConsumption() {
        return electricityConsumption;
    }

    public void setElectricityConsumption(double electricityConsumption) {
        this.electricityConsumption = electricityConsumption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElectricalEngine that = (ElectricalEngine) o;
        return Double.compare(that.batterySize, batterySize) == 0 && Double.compare(that.electricityConsumption, electricityConsumption) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(batterySize, electricityConsumption);
    }

    @Override
    public String toString() {
        return super.toString() + "," + batterySize + "," + electricityConsumption;
    }
}
