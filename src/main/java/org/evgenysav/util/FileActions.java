package org.evgenysav.util;

import org.evgenysav.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileActions {
    private static final String FORMATTED_PATH_TO_CSV = "csv/%s.csv";

    private FileActions() {}

    public static List<VehicleType> getVehicleTypesFromFile(String inFile) throws IOException {
        return (List<VehicleType>) getListOfEntitiesFromFile("vehicleTypes", inFile);
    }

    public static List<Rent> getRentsFromFile(String inFile) throws IOException {
        return (List<Rent>) getListOfEntitiesFromFile("rents", inFile);
    }

    public static List<Vehicle> getVehiclesFromFile(String inFile, List<VehicleType> vehicleTypes, List<Rent> rents) throws IOException {
        List<Vehicle> resultList = new ArrayList<>();
        Path path = getPathFromFilename(inFile);
        List<String> linesFromFile = Files.readAllLines(path);
        processLinesFromVehiclesCsv(resultList, linesFromFile, vehicleTypes, rents);
        return resultList;
    }

    public static VehicleType createTypeFromCsv(String csvString) {
        return getEntityFromCsv(new VehicleType(), csvString);
    }

    public static Vehicle createVehicleFromCsv(String csvString, List<VehicleType> vehicleTypes, List<Rent> rents) {
        List<Vehicle> resultList = new ArrayList<>();
        processLinesFromVehiclesCsv(resultList, List.of(csvString), vehicleTypes, rents);
        if (resultList.size() != 0) {
            return resultList.get(0);
        }

        throw new IllegalArgumentException("Invalid line from csv: " + csvString);
    }

    public static Rent createRentFromCsv(String csvString) {
        return getEntityFromCsv(new Rent(), csvString);
    }

    public static void setRentsListToVehicle(Vehicle vehicle, List<Rent> rents) {
        List<Rent> vehicleOrders = new ArrayList<>();
        int vehicleId = vehicle.getVehicleId();
        for (Rent r : rents) {
            if (r.getId() == vehicleId) {
                vehicleOrders.add(r);
            }
        }
        vehicle.setMachineOrders(vehicleOrders);
    }

    private static <T> T getEntityFromCsv(T t, String csvString) {
        List<T> resultList = new ArrayList<>();
        if (t instanceof VehicleType){
            processLinesFromVehicleTypesCsv((List<VehicleType>) resultList, List.of(csvString));
        }
        if (t instanceof Rent){
            processLinesFromRentsCsv((List<Rent>) resultList, List.of(csvString));
        }

        if (resultList.size() != 0) {
            return resultList.get(0);
        }

        throw new IllegalArgumentException("Invalid line from csv: " + csvString);
    }

    private static List<?> getListOfEntitiesFromFile(String choice, String inFile) throws IOException {
        Path path = getPathFromFilename(inFile);
        List<String> linesFromFile = Files.readAllLines(path);
        if (choice.equals("vehicleTypes")) {
            List<VehicleType> resultList = new ArrayList<>();
            processLinesFromVehicleTypesCsv(resultList, linesFromFile);
            return resultList;
        }
        if (choice.equals("rents")) {
            List<Rent> resultList = new ArrayList<>();
            processLinesFromRentsCsv(resultList, linesFromFile);
            return resultList;
        }

        throw new IllegalArgumentException("Invalid file choice: " + choice);
    }

    private static void processLinesFromVehiclesCsv(List<Vehicle> resultList, List<String> linesFromFile,
                                                    List<VehicleType> vehicleTypes, List<Rent> rents) {
        for (String line : linesFromFile) {
            String[] values = line.split(",");
            Vehicle vehicle = new Vehicle();
            if (line.contains("Electrical")) {
                if (!line.contains("\"")) {
                    setFieldsToElectricVehicleWithoutQuotes(vehicle, values, vehicleTypes);
                } else {
                    if (checkQuoteNumber(line) && line.endsWith("\"")) {
                        setFieldsToElectricVehicleWithTwoQuotes(true, vehicle, vehicleTypes, values);
                    } else if (checkQuoteNumber(line) && !line.endsWith("\"")) {
                        setFieldsToElectricVehicleWithTwoQuotes(false, vehicle, vehicleTypes, values);
                    } else {
                        setFieldsToElectricVehicleWithFourQuotes(vehicle, vehicleTypes, values);
                    }
                }
                setRentsListToVehicle(vehicle, rents);
                resultList.add(vehicle);
                continue;
            }
            if (!line.contains("\"")) {
                setFieldsToNonElectricVehicleWithoutQuotes(vehicle, vehicleTypes, values, line.contains("Gasoline"));
            } else {
                if (values.length == 13) {
                    String[] tempValues = line.replaceFirst("\"", "!").split("!");
                    String[] spittedAfterEngine = tempValues[1].split(",");
                    setFieldToNonElectricVehicleWithQuotes(vehicle, vehicleTypes, values,
                            line.contains("Gasoline"), spittedAfterEngine.length != 3, false);
                } else {
                    setFieldToNonElectricVehicleWithQuotes(vehicle, vehicleTypes, values,
                            line.contains("Gasoline"), true, true);
                }
            }
            setRentsListToVehicle(vehicle, rents);
            resultList.add(vehicle);
        }
    }

    private static void setFieldToNonElectricVehicleWithQuotes(Vehicle vehicle, List<VehicleType> vehicleTypes,
                                                               String[] values, boolean isGasolineEngine, boolean isQuoteFirst,
                                                               boolean containsFourQuotes) {
        try {
            setDuplicatedLogicToVehicleFields(vehicle, values, vehicleTypes);
            setStartableToNonElectricVehicle(vehicle, values, isQuoteFirst, containsFourQuotes, isGasolineEngine);
        } catch (NotVehicleException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static void setStartableToNonElectricVehicle(Vehicle vehicle, String[] values, boolean isQuoteFirst,
                                                         boolean containsFourQuotes, boolean isGasolineEngine) throws NotVehicleException {
        if (containsFourQuotes) {
            double firstDouble = getDoubleFromCsv(values[9], values[10]);
            double secondDouble = getDoubleFromCsv(values[11], values[12]);
            if (isGasolineEngine) {
                vehicle.setStartable(new GasolineEngine(firstDouble, secondDouble, Double.parseDouble(values[13])));
            } else {
                vehicle.setStartable(new DieselEngine(firstDouble, secondDouble, Double.parseDouble(values[13])));
            }
            return;
        }
        if (isQuoteFirst) {
            double firstDouble = getDoubleFromCsv(values[9], values[10]);
            if (isGasolineEngine) {
                vehicle.setStartable(new GasolineEngine(firstDouble, Double.parseDouble(values[11]),
                        Double.parseDouble(values[12])));
            } else {
                vehicle.setStartable(new DieselEngine(firstDouble, Double.parseDouble(values[11]),
                        Double.parseDouble(values[12])));
            }
        } else {
            double secondDouble = getDoubleFromCsv(values[10], values[11]);
            if (isGasolineEngine) {
                vehicle.setStartable(new GasolineEngine(Double.parseDouble(values[9]), secondDouble,
                        Double.parseDouble(values[12])));
            } else {
                vehicle.setStartable(new DieselEngine(Double.parseDouble(values[9]), secondDouble,
                        Double.parseDouble(values[12])));
            }
        }
    }

    private static void setFieldsToNonElectricVehicleWithoutQuotes(Vehicle vehicle, List<VehicleType> vehicleTypes,
                                                                   String[] values, boolean isGasolineEngine) {
        try {
            setDuplicatedLogicToVehicleFields(vehicle, values, vehicleTypes);
            if (isGasolineEngine) {
                vehicle.setStartable(new GasolineEngine(Double.parseDouble(values[9]), Double.parseDouble(values[10]),
                        Double.parseDouble(values[11])));
            } else {
                vehicle.setStartable(new DieselEngine(Double.parseDouble(values[9]), Double.parseDouble(values[10]),
                        Double.parseDouble(values[11])));
            }
        } catch (NotVehicleException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static void setFieldsToElectricVehicleWithFourQuotes(Vehicle vehicle, List<VehicleType> vehicleTypes,
                                                                 String[] values) {
        try {
            double prevDouble = getDoubleFromCsv(values[9], values[10]);
            double lastDouble = getDoubleFromCsv(values[11], values[12]);
            vehicle.setStartable(new ElectricalEngine(prevDouble, lastDouble));
            setDuplicatedLogicToVehicleFields(vehicle, values, vehicleTypes);
        } catch (NotVehicleException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static void setFieldsToElectricVehicleWithTwoQuotes(boolean locatedInEnd, Vehicle vehicle,
                                                                List<VehicleType> vehicleTypes, String[] values) {
        try {
            if (locatedInEnd) {
                double lastDouble = getDoubleFromCsv(values[10], values[11]);
                vehicle.setStartable(new ElectricalEngine(Double.parseDouble(values[9]), lastDouble));
                setDuplicatedLogicToVehicleFields(vehicle, values, vehicleTypes);
            } else {
                double prevDouble = getDoubleFromCsv(values[9], values[10]);
                vehicle.setStartable(new ElectricalEngine(prevDouble, Double.parseDouble(values[11])));
                setDuplicatedLogicToVehicleFields(vehicle, values, vehicleTypes);
            }
        } catch (NotVehicleException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static void setDuplicatedLogicToVehicleFields(Vehicle vehicle, String[] values,
                                                          List<VehicleType> vehicleTypes) throws NotVehicleException {
        vehicle.setVehicleId(Integer.parseInt(values[0]));
        int vehicleTypeId = Integer.parseInt(values[1]);
        vehicle.setVehicleTypeId(vehicleTypeId);
        VehicleType vehicleType = vehicleTypes.get(vehicleTypeId - 1);
        vehicle.setType(vehicleType);
        vehicle.setModelName(values[2]);
        vehicle.setRegistrationNumber(values[3]);
        vehicle.setWeight(Integer.parseInt(values[4]));
        vehicle.setManufactureYear(Integer.parseInt(values[5]));
        vehicle.setMileage(Integer.parseInt(values[6]));
        vehicle.setColor(Color.valueOf(values[7].toUpperCase()));
    }

    private static void setFieldsToElectricVehicleWithoutQuotes(Vehicle vehicle, String[] values,
                                                                List<VehicleType> vehicleTypes) {
        try {
            vehicle.setStartable(new ElectricalEngine(Double.parseDouble(values[9]), Double.parseDouble(values[10])));
            setDuplicatedLogicToVehicleFields(vehicle, values, vehicleTypes);
        } catch (NotVehicleException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static boolean checkQuoteNumber(String str) {
        char[] chars = str.toCharArray();
        int counter = 0;
        for (char c : chars) {
            if (c == '"') {
                counter++;
            }
        }

        return counter == 2;
    }

    private static void processLinesFromRentsCsv(List<Rent> resultList, List<String> linesFromFile) {
        for (String line : linesFromFile) {
            Rent rent = new Rent();
            String[] values = line.split(",");
            if (line.contains("\"")) {
                rent.setId(Integer.parseInt(values[0]));
                String[] dateParts = values[1].split("\\.");
                rent.setRentalDate(LocalDate.of(Integer.parseInt(dateParts[2]),
                        Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[0])));
                double doubleFromCsv = getDoubleFromCsv(values[2], values[3]);
                rent.setRentCost(doubleFromCsv);
                resultList.add(rent);
                continue;
            }

            rent.setId(Integer.parseInt(values[0]));
            String[] dateParts = values[1].split("\\.");
            rent.setRentalDate(LocalDate.of(Integer.parseInt(dateParts[2]),
                    Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[0])));
            rent.setRentCost(Double.parseDouble(values[2]));
            resultList.add(rent);
        }
    }

    private static void processLinesFromVehicleTypesCsv(List<VehicleType> resultList, List<String> linesFromFile) {
        for (String line : linesFromFile) {
            VehicleType vehicleType = new VehicleType();
            String[] values = line.split(",");
            if (line.contains("\"")) {
                vehicleType.setId(Integer.parseInt(values[0]));
                vehicleType.setTypeName(values[1]);
                vehicleType.setTaxCoefficient(getDoubleFromCsv(values[2], values[3]));
                resultList.add(vehicleType);
                continue;
            }

            vehicleType.setId(Integer.parseInt(values[0]));
            vehicleType.setTypeName(values[1]);
            vehicleType.setTaxCoefficient(Double.parseDouble(values[2]));
            resultList.add(vehicleType);
        }
    }

    private static double getDoubleFromCsv(String... strings) {
        int leftQuotePos = strings[0].indexOf("\"");
        int rightQuotePos = strings[1].indexOf("\"");
        return Double.parseDouble(strings[0].substring(leftQuotePos + 1) + "." +
                strings[1].substring(0, rightQuotePos));
    }

    private static Path getPathFromFilename(String filename) {
        ClassLoader classLoader = FileActions.class.getClassLoader();
        URL resource = classLoader.getResource(String.format(FORMATTED_PATH_TO_CSV, filename));
        if (resource == null) {
            throw new IllegalArgumentException("Cannot find file with name \"" + filename + "\"", new FileNotFoundException());
        }
        try {
            return Paths.get(resource.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid filename: " + filename, e);
        }
    }
}