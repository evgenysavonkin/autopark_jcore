package org.evgenysav.classes_;

import org.evgenysav.classes.Fixer;
import org.evgenysav.classes.Vehicle;
import org.evgenysav.infrastructure.core.annotations.Autowired;
import org.evgenysav.infrastructure.dto.classes_dto.VehicleDto;
import org.evgenysav.infrastructure.dto.entity.OrderEntries;
import org.evgenysav.infrastructure.dto.entity.Orders;
import org.evgenysav.infrastructure.dto.service.OrderEntriesService;
import org.evgenysav.infrastructure.dto.service.OrdersService;

import java.util.*;

public class Workroom {

    @Autowired
    private Fixer mechanic;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderEntriesService orderEntriesService;

    private final Random random = new Random();
    private static final String[] DETAILS = new String[]{"Фильтр", "Фланец", "Втулка", "Вал", "Ось", "Свеча", "Масло", "ГРМ", "ШРУС"};

    public Workroom() {

    }

    public void checkAllVehicle(List<Vehicle> vehicles) {
        vehicles.forEach(mechanic::detectBreaking);
        List<Vehicle> notBrokenVehicles = new ArrayList<>();
        for (Vehicle v : vehicles) {
            if (mechanic.isBroken(v)) {
                System.out.println(v.getModelName() + " was broken");
            } else {
                notBrokenVehicles.add(v);
            }
        }

        notBrokenVehicles.forEach(v -> System.out.println(v.getModelName() + " wasn't broken"));

        System.out.println();
        vehicles.forEach(mechanic::repair);
    }

    public boolean wasBroken(VehicleDto vehicleDto) {
        Optional<Orders> orderByVehicleIdOptional = ordersService.getAll().stream()
                .filter(v -> v.getVehicleId() == vehicleDto.getVehicleId())
                .findFirst();

        if (orderByVehicleIdOptional.isEmpty()) {
            return false;
        }

        return true;
    }

    public void repair(VehicleDto vehicleDto) {
        ordersService.remove(new Orders(), "vehicle_id", vehicleDto.getVehicleId());
    }

    public void detectBreaking(VehicleDto vehicleDto) {
        int numberOfDetails = random.nextInt(DETAILS.length);
        if (numberOfDetails == 0){
            return;
        }

        Map<String, Integer> mapOfBreakDowns = new HashMap<>();
        Set<Integer> takenIndexes = new HashSet<>();
        while (numberOfDetails != 0) {
            int randomKey = random.nextInt(DETAILS.length);
            if (takenIndexes.contains(randomKey)) {
                while (takenIndexes.contains(randomKey)) {
                    randomKey = random.nextInt(DETAILS.length);
                }
            }

            takenIndexes.add(randomKey);
            mapOfBreakDowns.put(DETAILS[randomKey], random.nextInt(10) + 1);
            numberOfDetails--;
        }

        Orders order = new Orders();
        order.setVehicleId(vehicleDto.getVehicleId());
        List<OrderEntries> orderEntries = new ArrayList<>();
        for (var entry : mapOfBreakDowns.entrySet()) {
            OrderEntries orderEntry = new OrderEntries();
            orderEntry.setVehicleId(vehicleDto.getVehicleId());
            orderEntry.setDetailName(entry.getKey());
            orderEntry.setQuantity(Long.valueOf(entry.getValue()));
            orderEntries.add(orderEntry);
        }

        ordersService.save(order);
        orderEntries.forEach(orderEntriesService::save);
    }
}
