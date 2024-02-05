package org.evgenysav;

public class VehicleType {
    private String typeName;
    private double taxCoefficient;

    public VehicleType() {
    }

    public VehicleType(String typeName, double taxCoefficient) {
        this.typeName = typeName;
        this.taxCoefficient = taxCoefficient;
    }

    public void display() {
        System.out.println("typeName = " + typeName);
        System.out.println("taxCoefficient = " + taxCoefficient);
    }

    public String getString() {
        return typeName + "," + taxCoefficient;
    }

    @Override
    public String toString() {
        return typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public double getTaxCoefficient() {
        return taxCoefficient;
    }

    public void setTaxCoefficient(double taxCoefficient) {
        this.taxCoefficient = taxCoefficient;
    }
}
