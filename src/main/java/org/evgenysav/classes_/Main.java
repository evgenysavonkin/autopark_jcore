package org.evgenysav.classes_;

import org.evgenysav.classes.Fixer;
import org.evgenysav.infrastructure.core.impl.ApplicationContext;
import org.evgenysav.infrastructure.dto.entity.CombustionStartable;
import org.evgenysav.infrastructure.dto.entity.OrderEntries;
import org.evgenysav.infrastructure.dto.entity.Vehicles;
import org.evgenysav.infrastructure.dto.service.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<Class<?>, Class<?>> interfaceToImplementation = new HashMap<>();
        interfaceToImplementation.put(Fixer.class, MechanicService.class);

        ApplicationContext context = new ApplicationContext("org.evgenysav",
                interfaceToImplementation);
        initAndInsertValuesInDB(context);

        VehicleService vehicleService = context.getObject(VehicleService.class);
        CombustionEngineService combustionService = context.getObject(CombustionEngineService.class);
        List<CombustionStartable> combustionEngines = combustionService.getAll();
        combustionEngines.forEach(System.out::println);
        List<Vehicles> vehicles = vehicleService.getAll();
        vehicles.forEach(System.out::println);
    }

    private static void initAndInsertValuesInDB(ApplicationContext context) {
        context.getObject(VehicleService.class);
        context.getObject(TypesService.class);
        context.getObject(RentsService.class);
        context.getObject(OrdersService.class);
        context.getObject(OrderEntries.class);
        context.getObject(ElectricalEngineService.class);
        context.getObject(CombustionEngineService.class);
    }
}
