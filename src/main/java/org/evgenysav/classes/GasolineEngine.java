package org.evgenysav.classes;

public class GasolineEngine extends CombustionEngine {

    public GasolineEngine() {

    }

    public GasolineEngine(double engineCapacity, double fuelTankCapacity, double fuelConsumptionPer100) {
        super("Gasoline", 1.1, engineCapacity, fuelTankCapacity, fuelConsumptionPer100);
    }

    @Override
    public String getStartableName() {
        return "Gasoline";
    }
}
