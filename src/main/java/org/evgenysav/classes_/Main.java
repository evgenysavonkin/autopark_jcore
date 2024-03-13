package org.evgenysav.classes_;

import org.evgenysav.classes.Fixer;
import org.evgenysav.infrastructure.core.impl.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<Class<?>, Class<?>> interfaceToImplementation = new HashMap<>();
        interfaceToImplementation.put(Fixer.class, MechanicService.class);
        interfaceToImplementation.put(Fixer.class, BadMechanicService.class);

        ApplicationContext context = new ApplicationContext("org.evgenysav",
                interfaceToImplementation);
        VehicleCollection vehicleCollection = context.getObject(VehicleCollection.class);
        Workroom workroom = context.getObject(Workroom.class);
        workroom.checkAllVehicle(vehicleCollection.getVehicles());
    }
}
