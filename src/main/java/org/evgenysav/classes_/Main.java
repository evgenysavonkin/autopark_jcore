package org.evgenysav.classes_;

import org.evgenysav.classes.Fixer;
import org.evgenysav.infrastructure.core.impl.ApplicationContext;
import org.evgenysav.infrastructure.dto.entity.OrderEntries;
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
        ScheduledMain scheduledMain = context.getObject(ScheduledMain.class);
        scheduledMain.moveVehiclesToWorkRoom(context);
        Thread.sleep(120_000);
    }

    static void initAndInsertValuesInDB(ApplicationContext context) {
        context.getObject(VehicleService.class);
        context.getObject(TypesService.class);
        context.getObject(RentsService.class);
        context.getObject(OrdersService.class);
        context.getObject(OrderEntries.class);
        context.getObject(ElectricalEngineService.class);
        context.getObject(CombustionEngineService.class);
    }
}
