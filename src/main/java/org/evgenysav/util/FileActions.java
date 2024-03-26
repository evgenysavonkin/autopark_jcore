package org.evgenysav.util;

import org.evgenysav.classes.*;
import org.evgenysav.exceptions.NotVehicleException;
import org.evgenysav.infrastructure.dto.entity.Color_;
import org.evgenysav.infrastructure.dto.entity.CombustionStartable;
import org.evgenysav.infrastructure.dto.entity.ElectricalStartable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FileActions {

    private static final SimpleDateFormat dateFormatFromRents = new SimpleDateFormat("dd.MM.yyyy");

    private FileActions() {
    }

    public static List<VehicleType> getVehicleTypesFromFile(String inFile) throws IOException {
        return getListOfEntitiesFromFile(new VehicleType(), inFile);
    }

    public static List<Rent> getRentsFromFile(String inFile) throws IOException {
        return getListOfEntitiesFromFile(new Rent(), inFile);
    }

    public static List<Vehicle> getVehiclesFromFile(String inFile, List<VehicleType> vehicleTypes, List<Rent> rents) throws IOException {
        return getListOfEntitiesFromFile(new Vehicle(), inFile, vehicleTypes, rents);
    }

    public static VehicleType createTypeFromCsv(String csvString) {
        return getEntityFromCsv(new VehicleType(), csvString);
    }

    public static Vehicle createVehicleFromCsv(String csvString, List<VehicleType> vehicleTypes, List<Rent> rents) {
        return getEntityFromCsv(new Vehicle(), csvString, vehicleTypes, rents);
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

    public static List<String> getLinesFromFile(String filename) throws IOException {
        Path path = getPathFromFilename(filename);
        return Files.readAllLines(path);
    }

    public static Path getPathFromFilename(String filename) {
        String absolutePath = "C:\\WORK\\Java\\Pet_projects\\java_core\\autopark\\csv";
        return Paths.get(absolutePath, filename + ".csv");
    }

    public static List<Color_> getColorsList(String fileName) throws IOException {
        List<String> linesFromFile = getLinesFromFile(fileName);
        List<Color_> colorsList = new ArrayList<>();
        for (String line : linesFromFile) {
            String[] lineValues = line.split(",");
            Color_ color = new Color_();
            color.setVehicleId(Long.parseLong(lineValues[0]));
            color.setColor(lineValues[7]);
            colorsList.add(color);
        }

        return colorsList;
    }

    public static List<Object> getEngineListFromFile(String filename) throws IOException {
        List<String> linesFromFile = getLinesFromFile(filename);
        List<Object> engineList = new ArrayList<>();

        for (String line : linesFromFile) {
            String[] valuesFromLine = line.split(",");
            long vehicleId;
            try {
                vehicleId = Long.parseLong(valuesFromLine[0]);
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }

            boolean containsGasolineType = line.contains("Gasoline");
            boolean containsDieselType = line.contains("Diesel");
            boolean lineContainsQuotes = line.contains("\"");
            if (containsGasolineType || containsDieselType) {
                CombustionStartable combustionStartable = new CombustionStartable();
                if (containsGasolineType) {
                    combustionStartable.setEngineType("Gasoline");
                    combustionStartable.setTaxCoefficient(1.1);
                } else {
                    combustionStartable.setEngineType("Diesel");
                    combustionStartable.setTaxCoefficient(1.2);
                }
                combustionStartable.setVehicleId(vehicleId);

                double engineCapacity;
                double fuelTankCapacity;
                double fuelConsumptionPer100;
                if (!lineContainsQuotes) {
                    engineCapacity = Double.parseDouble(valuesFromLine[9]);
                    fuelTankCapacity = Double.parseDouble(valuesFromLine[10]);
                    fuelConsumptionPer100 = Double.parseDouble(valuesFromLine[11]);
                } else {
                    if (valuesFromLine.length == 13) {
                        String[] tempValues = line.replaceFirst("\"", "!").split("!");
                        String[] splittedAfterEngine = tempValues[1].split(",");
                        if (splittedAfterEngine.length != 3) {
                            engineCapacity = getDoubleFromCsv(valuesFromLine[9], valuesFromLine[10]);
                            fuelTankCapacity = Double.parseDouble(valuesFromLine[11]);
                            fuelConsumptionPer100 = Double.parseDouble(valuesFromLine[12]);
                        } else {
                            engineCapacity = Double.parseDouble(valuesFromLine[9]);
                            fuelTankCapacity = getDoubleFromCsv(valuesFromLine[10], valuesFromLine[11]);
                            fuelConsumptionPer100 = Double.parseDouble(valuesFromLine[12]);
                        }
                    } else {
                        engineCapacity = getDoubleFromCsv(valuesFromLine[9], valuesFromLine[10]);
                        fuelTankCapacity = getDoubleFromCsv(valuesFromLine[11], valuesFromLine[12]);
                        fuelConsumptionPer100 = Double.parseDouble(valuesFromLine[13]);
                    }
                }

                combustionStartable.setEngineCapacity(engineCapacity);
                combustionStartable.setFuelTankCapacity(fuelTankCapacity);
                combustionStartable.setFuelConsumptionPer100(fuelConsumptionPer100);
                engineList.add(combustionStartable);

            } else {
                ElectricalStartable electricalStartable = new ElectricalStartable();
                electricalStartable.setVehicleId(vehicleId);
                electricalStartable.setTaxCoefficient(0.1);
                if (!lineContainsQuotes) {
                    electricalStartable.setBatterySize(Double.parseDouble(valuesFromLine[9]));
                    electricalStartable.setElectricityConsumptionPer100(Double.parseDouble(valuesFromLine[10]));
                } else {
                    double batterySize;
                    double electricityConsumption;
                    if (checkQuoteNumber(line) && line.endsWith("\"")) {
                        batterySize = Double.parseDouble(valuesFromLine[9]);
                        electricityConsumption = getDoubleFromCsv(valuesFromLine[10], valuesFromLine[11]);
                    } else if (checkQuoteNumber(line) && !line.endsWith("\"")) {
                        batterySize = getDoubleFromCsv(valuesFromLine[9], valuesFromLine[10]);
                        electricityConsumption = Double.parseDouble(valuesFromLine[11]);
                    } else {
                        batterySize = getDoubleFromCsv(valuesFromLine[9], valuesFromLine[10]);
                        electricityConsumption = getDoubleFromCsv(valuesFromLine[11], valuesFromLine[12]);
                    }

                    electricalStartable.setBatterySize(batterySize);
                    electricalStartable.setElectricityConsumptionPer100(electricityConsumption);
                }

                engineList.add(electricalStartable);
            }
        }

        return engineList;
    }

    private static <T> T getEntityFromCsv(T t, Object... values) {
        List<T> resultList = new ArrayList<>();
        String csvString = (String) values[0];
        if (t instanceof VehicleType) {
            processLinesFromVehicleTypesCsv((List<VehicleType>) resultList,
                    List.of(csvString));
        }
        if (t instanceof Rent) {
            processLinesFromRentsCsv((List<Rent>) resultList,
                    List.of(csvString));
        }
        if (t instanceof Vehicle) {
            processLinesFromVehiclesCsv((List<Vehicle>) resultList,
                    List.of(csvString), (List<VehicleType>) values[1],
                    (List<Rent>) values[2]);
        }

        if (resultList.size() != 0) {
            return resultList.get(0);
        }

        throw new IllegalArgumentException("Invalid line from csv: " + csvString);
    }

    private static <T> List<T> getListOfEntitiesFromFile(T t, Object... values) throws IOException {
        String inFile = (String) values[0];
        Path path = getPathFromFilename(inFile);
        List<String> linesFromFile = Files.readAllLines(path);
        List<T> resultList = new ArrayList<>();
        if (t instanceof VehicleType) {
            processLinesFromVehicleTypesCsv((List<VehicleType>) resultList, linesFromFile);
        }
        if (t instanceof Rent) {
            processLinesFromRentsCsv((List<Rent>) resultList, linesFromFile);
        }
        if (t instanceof Vehicle) {
            processLinesFromVehiclesCsv((List<Vehicle>) resultList, linesFromFile,
                    (List<VehicleType>) values[1], (List<Rent>) values[2]);
        }
        return resultList;
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

    private static void setFieldsToNonElectricVehicleWithoutQuotes(Vehicle
                                                                           vehicle, List<VehicleType> vehicleTypes,
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

    private static void setFieldsToElectricVehicleWithFourQuotes(Vehicle
                                                                         vehicle, List<VehicleType> vehicleTypes,
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
                try {
                    rent.setRentalDate(dateFormatFromRents.parse(values[1]));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                double doubleFromCsv = getDoubleFromCsv(values[2], values[3]);
                rent.setRentCost(doubleFromCsv);
                resultList.add(rent);
                continue;
            }

            rent.setId(Integer.parseInt(values[0]));
            try {
                rent.setRentalDate(dateFormatFromRents.parse(values[1]));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            rent.setRentCost(Double.parseDouble(values[2]));
            resultList.add(rent);
        }
    }

    private static void processLinesFromVehicleTypesCsv
            (List<VehicleType> resultList, List<String> linesFromFile) {
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
}
