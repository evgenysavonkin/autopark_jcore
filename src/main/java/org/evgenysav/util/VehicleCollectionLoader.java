package org.evgenysav.util;

import org.evgenysav.Rent;
import org.evgenysav.Vehicle;
import org.evgenysav.VehicleType;

import java.io.IOException;
import java.util.List;

public class VehicleCollectionLoader {

    private VehicleCollectionLoader() {
    }

    public static List<VehicleType> loadTypes(String inFile) {
        try {
            return FileActions.getVehicleTypesFromFile(inFile);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public static List<Rent> loadRents(String inFile) {
        try {
            return FileActions.getRentsFromFile(inFile);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public static List<Vehicle> loadVehicles(String inFile, List<VehicleType> vehicleTypes, List<Rent> rents) {
        try {
            return FileActions.getVehiclesFromFile(inFile, vehicleTypes, rents);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public static VehicleType createType(String csvString) {
        return FileActions.createTypeFromCsv(csvString);
    }

    public static Vehicle createVehicle(String csvString, List<VehicleType> vehicleTypes, List<Rent> rents) {
        return FileActions.createVehicleFromCsv(csvString, vehicleTypes, rents);
    }

    public static Rent createRent(String csvString) {
        return FileActions.createRentFromCsv(csvString);
    }
}
