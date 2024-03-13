package org.evgenysav.classes_;

import org.evgenysav.classes.Fixer;
import org.evgenysav.classes.Vehicle;

import java.util.Collections;
import java.util.Map;

public class BadMechanicService implements Fixer {
    @Override
    public Map<String, Integer> detectBreaking(Vehicle vehicle) {
        return Collections.emptyMap();
    }

    @Override
    public void repair(Vehicle vehicle) {

    }

    @Override
    public boolean isBroken(Vehicle vehicle) {
        return false;
    }

    @Override
    public int findVehicleIdByStringFromFile(String line) {
        return 0;
    }
}
