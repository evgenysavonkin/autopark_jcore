package org.evgenysav;

import java.util.Comparator;

public class VehicleComparator implements Comparator<Vehicle> {
    @Override
    public int compare(Vehicle o1, Vehicle o2) {
        return Comparator.comparing(Vehicle::getModelName).compare(o1, o2);
    }
}
