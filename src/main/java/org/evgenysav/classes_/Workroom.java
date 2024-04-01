package org.evgenysav.classes_;

import org.evgenysav.classes.Fixer;
import org.evgenysav.classes.Vehicle;
import org.evgenysav.infrastructure.core.annotations.Autowired;
import org.evgenysav.infrastructure.dto.classes_dto.VehicleDto;

import java.util.ArrayList;
import java.util.List;

public class Workroom {

    @Autowired
    private Fixer mechanic;

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
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId(Math.toIntExact(vehicleDto.getVehicleId()));
        return mechanic.isBroken(vehicle);
    }
}
