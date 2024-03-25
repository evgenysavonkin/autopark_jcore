package org.evgenysav.classes_;

import org.evgenysav.classes.Color;
import org.evgenysav.classes.Rent;
import org.evgenysav.classes.Vehicle;
import org.evgenysav.exceptions.NotVehicleException;
import org.evgenysav.infrastructure.core.impl.ApplicationContext;
import org.evgenysav.infrastructure.dto.entity.Rents;
import org.evgenysav.infrastructure.dto.entity.Vehicles;
import org.evgenysav.infrastructure.dto.service.VehicleService;
import org.evgenysav.infrastructure.threads.annotations.Schedule;

import java.util.List;

public class ScheduledMain {

    @Schedule(delta = 1000, timeout = 10000)
    public void moveVehiclesToWorkRoom(ApplicationContext context) {
        VehicleService vehicleService = context.getObject(VehicleService.class);
        Workroom workroom = context.getObject(Workroom.class);
        List<Vehicles> vehiclesFromDB = vehicleService.getAll();
        List<Vehicle> vehicles = convertVehiclesFromDB(vehiclesFromDB);
        workroom.checkAllVehicle(vehicles);
    }

    private List<Vehicle> convertVehiclesFromDB(List<Vehicles> vehiclesList) {
        return vehiclesList.stream()
                .map((Vehicles vehicleFromDB) -> {
                    Vehicle vehicle = new Vehicle();
                    vehicle.setVehicleId(Math.toIntExact(vehicleFromDB.getVehicleId()));
                    vehicle.setVehicleTypeId(Math.toIntExact(vehicleFromDB.getVehicleTypeId()));
                    List<Rents> ordersFromDB = vehicleFromDB.getMachineOrders();
                    List<Rent> machineOrders = ordersFromDB.stream()
                            .map((Rents rentFromDB) -> {
                                Rent rent = new Rent();
                                rent.setId(Math.toIntExact(rentFromDB.getVehicleId()));
                                rent.setRentalDate(rentFromDB.getRentDate());
                                rent.setRentCost(rentFromDB.getRentalCost());
                                return rent;
                            }).toList();

                    vehicle.setMachineOrders(machineOrders);
                    setFieldWithThrowing(vehicle, vehicleFromDB, "startable");
                    setFieldWithThrowing(vehicle, vehicleFromDB, "type");
                    setFieldWithThrowing(vehicle, vehicleFromDB, "modelName");
                    setFieldWithThrowing(vehicle, vehicleFromDB, "regNumber");
                    setFieldWithThrowing(vehicle, vehicleFromDB, "weight");
                    setFieldWithThrowing(vehicle, vehicleFromDB, "manYear");
                    setFieldWithThrowing(vehicle, vehicleFromDB, "mileage");
                    setFieldWithThrowing(vehicle, vehicleFromDB, "color");
                    vehicle.setTankVolume(Math.toIntExact(vehicleFromDB.getTankVolume()));
                    return vehicle;
                }).toList();
    }

    private <T> void setFieldWithThrowing(Vehicle vehicle, Vehicles vehicleFromDB, String setFieldName) {
        if (setFieldName == null) {
            throw new RuntimeException("setFieldName is null");
        }
        try {
            switch (setFieldName) {
                case "startable" -> vehicle.setStartable(vehicleFromDB.getStartable());
                case "type" -> vehicle.setType(vehicleFromDB.getType());
                case "modelName" -> vehicle.setModelName(vehicleFromDB.getModelName());
                case "regNumber" -> vehicle.setRegistrationNumber(vehicleFromDB.getRegistrationNumber());
                case "weight" -> vehicle.setWeight(Math.toIntExact(vehicleFromDB.getWeight()));
                case "manYear" -> vehicle.setManufactureYear(Math.toIntExact(vehicleFromDB.getManufactureYear()));
                case "mileage" -> vehicle.setMileage(Math.toIntExact(vehicleFromDB.getMileage()));
                case "color" -> vehicle.setColor(Color.YELLOW);
                default -> {

                }
            }
        } catch (NotVehicleException e) {
            throw new RuntimeException(e);
        }
    }


}
