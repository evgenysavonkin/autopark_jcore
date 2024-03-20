package org.evgenysav.classes;

public abstract class AbstractEngine implements Startable {

    private String typeOfEngine;
    private double taxCoefficient;

    public AbstractEngine() {

    }

    public AbstractEngine(String typeOfEngine, double taxCoefficient) {
        this.typeOfEngine = typeOfEngine;
        this.taxCoefficient = taxCoefficient;
    }

    @Override
    public double getTaxPerMonth() {
        return taxCoefficient;
    }

    @Override
    public double getMaxKilometers() {
        return 0;
    }

    public String getTypeOfEngine() {
        return typeOfEngine;
    }

    public void setTypeOfEngine(String typeOfEngine) {
        this.typeOfEngine = typeOfEngine;
    }

    public double getTaxCoefficient() {
        return taxCoefficient;
    }

    public void setTaxCoefficient(double taxCoefficient) {
        this.taxCoefficient = taxCoefficient;
    }

    @Override
    public String toString() {
        return typeOfEngine + "," + taxCoefficient;
    }
}
