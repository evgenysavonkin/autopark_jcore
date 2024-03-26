package org.evgenysav.classes_;

import org.evgenysav.classes.Fixer;
import org.evgenysav.infrastructure.core.impl.ApplicationContext;
import org.evgenysav.infrastructure.dto.service.*;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Map<Class<?>, Class<?>> interfaceToImplementation = new HashMap<>();
        interfaceToImplementation.put(Fixer.class, MechanicService.class);

        ApplicationContext context = new ApplicationContext("org.evgenysav",
                interfaceToImplementation);
        initAndInsertValuesInDB(context);
        VehicleService vehicleService = context.getObject(VehicleService.class);
        vehicleService.getAll().forEach(System.out::println);

    }

    static void initAndInsertValuesInDB(ApplicationContext context) {
        context.getObject(VehicleService.class);
        context.getObject(ElectricalEngineService.class);
        context.getObject(CombustionEngineService.class);
        context.getObject(TypesService.class);
        context.getObject(RentsService.class);
        context.getObject(ColorService.class);
    }
}
