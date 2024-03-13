package org.evgenysav.classes;

import java.util.Objects;

public abstract class CombustionEngine extends AbstractEngine {
    private double engineCapacity;
    private double fuelTankCapacity;
    private double fuelConsumptionPer100;

    public CombustionEngine(String typeOfEngine, double taxCoefficient, double engineCapacity,
                            double fuelTankCapacity, double fuelConsumptionPer100) {
        super(typeOfEngine, taxCoefficient);
        this.engineCapacity = engineCapacity;
        this.fuelTankCapacity = fuelTankCapacity;
        this.fuelConsumptionPer100 = fuelConsumptionPer100;
    }

    @Override
    public double getMaxKilometers() {
        return (fuelTankCapacity * 100) / fuelConsumptionPer100;
    }

    public double getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(double engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    public double getFuelTankCapacity() {
        return fuelTankCapacity;
    }

    public void setFuelTankCapacity(double fuelTankCapacity) {
        this.fuelTankCapacity = fuelTankCapacity;
    }

    public double getFuelConsumptionPer100() {
        return fuelConsumptionPer100;
    }

    public void setFuelConsumptionPer100(double fuelConsumptionPer100) {
        this.fuelConsumptionPer100 = fuelConsumptionPer100;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CombustionEngine that = (CombustionEngine) o;
        return Double.compare(that.engineCapacity, engineCapacity) == 0 && Double.compare(that.fuelTankCapacity, fuelTankCapacity) == 0 && Double.compare(that.fuelConsumptionPer100, fuelConsumptionPer100) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(engineCapacity, fuelTankCapacity, fuelConsumptionPer100);
    }

    @Override
    public String toString() {
        return super.toString() + "," + engineCapacity + "," + fuelTankCapacity + "," + fuelConsumptionPer100;
    }
}
