package org.evgenysav.classes;

public class DieselEngine extends CombustionEngine {

    public DieselEngine() {

    }

    public DieselEngine(double engineCapacity, double fuelTankCapacity, double fuelConsumptionPer100) {
        super("Diesel", 1.2, engineCapacity, fuelTankCapacity, fuelConsumptionPer100);
    }

    @Override
    public String getStartableName() {
        return "Diesel";
    }
}
