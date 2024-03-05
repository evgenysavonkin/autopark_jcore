package org.evgenysav;

import org.evgenysav.util.FileActions;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class VehicleCollection {
    private final List<VehicleType> vehicleTypes;
    private final List<Vehicle> vehicles;
    private final List<Rent> rents;
    private static final String DISPLAY_FORMAT = "%-6d %-9s %-25s %-15s %-15d %-8d %-10d %-11s %-13.3f %-13.3f %-13.3f";

    public VehicleCollection(String vehicleTypesFileName, String vehiclesFileName, String rentsFileName) {
        vehicleTypes = loadTypes(vehicleTypesFileName);
        rents = loadRents(rentsFileName);
        vehicles = loadVehicles(vehiclesFileName);
    }

    private List<VehicleType> loadTypes(String inFile) {
        try {
            return FileActions.getVehicleTypesFromFile(inFile);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    private List<Rent> loadRents(String inFile) {
        try {
            return FileActions.getRentsFromFile(inFile);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    private List<Vehicle> loadVehicles(String inFile) {
        try {
            return FileActions.getVehiclesFromFile(inFile, vehicleTypes, rents);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public VehicleType createType(String csvString) {
        return FileActions.createTypeFromCsv(csvString);
    }

    public Vehicle createVehicle(String csvString) {
        return FileActions.createVehicleFromCsv(csvString, vehicleTypes, rents);
    }

    public Rent createRent(String csvString) {
        return FileActions.createRentFromCsv(csvString);
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
        return vehicles.stream().mapToDouble(Vehicle::getTotalProfit).sum();
    }

    public void display() {
        System.out.println("Id     Type      ModelName                 Number          Weight (kg)     Year     Mileage    Color       Income        Tax           Profit");
        for (Vehicle v : vehicles) {
            System.out.printf(DISPLAY_FORMAT, v.getVehicleId(), v.getType(), v.getModelName(),
                    v.getRegistrationNumber(), v.getWeight(), v.getManufactureYear(), v.getMileage(),
                    getOrdinalColorForm(v.getColor()), v.getTotalIncome(), v.getCalcTaxPerMonth(), v.getTotalProfit());
            System.out.println();
        }
        System.out.printf("Total                                                                                                        " +
                "                          %.3f", sumTotalProfit());
        System.out.println();
        System.out.println();
    }

    public void sort(Comparator<Vehicle> comparator) {
        vehicles.sort(comparator);
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
