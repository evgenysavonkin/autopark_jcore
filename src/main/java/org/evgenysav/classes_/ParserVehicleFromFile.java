package org.evgenysav.classes_;

import org.evgenysav.classes.Rent;
import org.evgenysav.classes.Vehicle;
import org.evgenysav.classes.VehicleType;
import org.evgenysav.util.FileActions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ParserVehicleFromFile {

    public ParserVehicleFromFile() {

    }

    public List<VehicleType> loadTypes(String inFile) {
        try {
            return FileActions.getVehicleTypesFromFile(inFile);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public List<Rent> loadRents(String inFile) {
        try {
            return FileActions.getRentsFromFile(inFile);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public List<Vehicle> loadVehicles(String inFile, List<VehicleType> vehicleTypes, List<Rent> rents) {
        try {
            return FileActions.getVehiclesFromFile(inFile, vehicleTypes, rents);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public VehicleType createType(String csvString) {
        return FileActions.createTypeFromCsv(csvString);
    }

    public Vehicle createVehicle(String csvString, List<VehicleType> vehicleTypes, List<Rent> rents) {
        return FileActions.createVehicleFromCsv(csvString, vehicleTypes, rents);
    }

    public Rent createRent(String csvString) {
        return FileActions.createRentFromCsv(csvString);
    }

    public List<String> getLinesFromFile(String filename) throws IOException {
        Path path = FileActions.getPathFromFilename(filename);
        return Files.readAllLines(path);
    }
}
