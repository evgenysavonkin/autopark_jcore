package org.evgenysav.classes;

import org.evgenysav.util.FileActions;
import org.evgenysav.util.VehicleCollectionLoader;

import java.util.Comparator;
import java.util.List;

public class VehicleCollection {
    private final List<VehicleType> vehicleTypes;
    private final List<Vehicle> vehicles;
    private final List<Rent> rents;
    private static final String DISPLAY_FORMAT = "%-6d %-9s %-25s %-15s %-15d %-8d %-10d %-11s %-13.3f %-13.3f %-13.3f";

    public VehicleCollection(String vehicleTypesFileName, String vehiclesFileName, String rentsFileName) {
        vehicleTypes = VehicleCollectionLoader.loadTypes(vehicleTypesFileName);
        rents = VehicleCollectionLoader.loadRents(rentsFileName);
        vehicles = VehicleCollectionLoader.loadVehicles(vehiclesFileName, vehicleTypes, rents);
    }

    public void insert(int index, Vehicle v) {
        if (v.getMachineOrders() == null) {
            FileActions.setRentsListToVehicle(v, rents);
        }

        int collectionSize = vehicles.size();
        if (index >= 0 && index < collectionSize) {
            vehicles.add(index, v);
        } else {
            vehicles.add(v);
        }

    }

    public int delete(int index) {
        int collectionSize = vehicles.size();
        if (index >= 0 && index < collectionSize) {
            vehicles.remove(index);
            return index;
        }

        return -1;
    }

    public double sumTotalProfit() {
        return vehicles.stream()
                .mapToDouble(Vehicle::getTotalProfit)
                .sum();
    }

    public double sumTotalProfit(List<Vehicle> vehicles) {
        return vehicles
                .stream()
                .mapToDouble(Vehicle::getTotalProfit)
                .sum();
    }

    public void display() {
        displayVehicles(vehicles, false);
    }

    public void display(List<Vehicle> vehicles) {
        displayVehicles(vehicles, true);
    }

    public void sort(Comparator<Vehicle> comparator) {
        vehicles.sort(comparator);
    }

    public Vehicle getVehicleById(int id) {
        return vehicles
                .stream()
                .filter(v -> v.getVehicleId() == id)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private void displayVehicles(List<Vehicle> vehicles, boolean customCollection) {
        System.out.println("Id     Type      ModelName                 Number          Weight (kg)     Year     Mileage    Color       Income        Tax           Profit");
        for (Vehicle v : vehicles) {
            System.out.printf(DISPLAY_FORMAT, v.getVehicleId(), v.getType(), v.getModelName(),
                    v.getRegistrationNumber(), v.getWeight(), v.getManufactureYear(), v.getMileage(),
                    getOrdinalColorForm(v.getColor()), v.getTotalIncome(), v.getCalcTaxPerMonth(), v.getTotalProfit());
            System.out.println();
        }
        if (customCollection) {
            System.out.printf("Total                                                                                                        " +
                    "                          %.3f", sumTotalProfit(vehicles));
        } else {
            System.out.printf("Total                                                                                                        " +
                    "                          %.3f", sumTotalProfit(this.vehicles));
        }
        System.out.println();
        System.out.println();
    }

    private String getOrdinalColorForm(Color color) {
        String colorStr = color.toString();
        char firstLetter = colorStr.charAt(0);
        colorStr = colorStr.substring(1).toLowerCase();
        return firstLetter + colorStr;
    }

    public List<VehicleType> getVehicleTypes() {
        return vehicleTypes;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public List<Rent> getRents() {
        return rents;
    }
}
