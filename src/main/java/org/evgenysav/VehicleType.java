package org.evgenysav;

public enum VehicleType {
    BUS("Bus", 1.2),
    CAR("Car", 1),
    RINK("Rink", 1.5),
    TRACTOR("Tractor", 1.2);

    private String typeName;
    private double taxCoefficient;

    VehicleType(String typeName, double taxCoefficient) {
        this.typeName = typeName;
        this.taxCoefficient = taxCoefficient;
    }

    public void display() {
        System.out.println("typeName = " + typeName);
        System.out.println("taxCoefficient = " + taxCoefficient);
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

    @Override
    public String toString() {
        return typeName;
    }
}
