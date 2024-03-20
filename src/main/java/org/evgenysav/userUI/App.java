package org.evgenysav.userUI;

import org.evgenysav.classes.*;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

public class App {

    private static final VehicleCollection vehicleCollection = new VehicleCollection("types",
            "vehicles", "rents");
    private static final MechanicService mechanicService = new MechanicService();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String userChoice;
        System.out.println("Hello, User!");
        while (true) {
            printMenu();
            userChoice = scanner.nextLine();
            try {
                Integer.parseInt(userChoice);
            } catch (NumberFormatException e) {
                System.err.println("Incorrect input! Try again");
                continue;
            }
            if (userChoice.equals("-1")) {
                System.out.println("Good bye!");
                break;
            }
            processUserInput(userChoice, scanner);
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("""
                MENU:
                                
                1. Print all vehicles
                2. Print tax coefficient by option
                3. Print all vehicles sorted by value
                4. Find duplicates by option
                5. Print info about vehicle by id
                6. Print average and sum of tax, income and profit of vehicles
                7. Repair vehicles              
                8. Get info about auto diagnostics     
                                
                Type -1 if you want to exit.         
                    Your choice:                 
                """);
    }

    private static void processUserInput(String input, Scanner scanner) {
        switch (input) {
            case "1" -> vehicleCollection.display();
            case "2" -> {
                System.out.println("Input way of sorting by tax coefficient (max, min, avg)");
                String userChoice = scanner.nextLine();
                switch (userChoice) {
                    case "max" -> printSpecifiedTaxCoefficient(TaxCoefficientMode.MAX);
                    case "min" -> printSpecifiedTaxCoefficient(TaxCoefficientMode.MIN);
                    case "avg" -> printSpecifiedTaxCoefficient(TaxCoefficientMode.AVG);
                    default -> System.err.println("Incorrect input!");
                }

            }
            case "3" -> {
                System.out.println("Input name of field to sort by (vehicle_type, model_name, reg_number, " +
                        "weight, manufacture_year, color, engine, mileage, tank_vol)");
                String userChoice = scanner.nextLine();
                switch (userChoice) {
                    case "vehicle_type" -> printSortedVehicles(SortMode.VEHICLE_NAME);
                    case "model_name" -> printSortedVehicles(SortMode.MODEL_NAME);
                    case "reg_number" -> printSortedVehicles(SortMode.REG_NUMBER);
                    case "weight" -> printSortedVehicles(SortMode.WEIGHT);
                    case "manufacture_year" -> printSortedVehicles(SortMode.MANUFACTURE_YEAR);
                    case "color" -> printSortedVehicles(SortMode.COLOR);
                    case "engine" -> printSortedVehicles(SortMode.STARTABLE);
                    case "mileage" -> printSortedVehicles(SortMode.MILEAGE);
                    case "tank_vol" -> printSortedVehicles(SortMode.TANK_VOLUME);
                    default -> {
                        System.err.println("Incorrect input!");
                    }
                }
            }
            case "4" -> {
                System.out.println("Input parameter to find duplicates (vehicle_type, model_name, weight, " +
                        "manufacture_year, color, engine)");
                String userChoice = scanner.nextLine();
                switch (userChoice) {
                    case "vehicle_type" -> printEqualVehiclesByKey(SortMode.VEHICLE_NAME);
                    case "model_name" -> printEqualVehiclesByKey(SortMode.MODEL_NAME);
                    case "weight" -> printEqualVehiclesByKey(SortMode.WEIGHT);
                    case "manufacture_year" -> printEqualVehiclesByKey(SortMode.MANUFACTURE_YEAR);
                    case "color" -> printEqualVehiclesByKey(SortMode.COLOR);
                    case "engine" -> printEqualVehiclesByKey(SortMode.STARTABLE);
                    default -> {
                        System.err.println("Incorrect input!");
                    }
                }
            }
            case "5" -> {
                System.out.println("Input vehicle id to get info: ");
                String vehicleIdStr = scanner.nextLine();
                int vehicleId;
                try {
                    vehicleId = Integer.parseInt(vehicleIdStr);
                } catch (NumberFormatException e) {
                    System.err.println("Incorrect input!");
                    return;
                }

                Vehicle vehicle;
                try {
                    vehicle = vehicleCollection.getVehicleById(vehicleId);
                } catch (IllegalArgumentException e){
                    System.out.println();
                    System.err.println("Incorrect vehicle id!");
                    return;
                }

                printVehicleInfo(vehicle);
            }
            case "6" -> {
                List<Vehicle> vehicles = vehicleCollection.getVehicles();
                System.out.printf("\nAverage tax: %.2f\n", getAverageNumber(vehicles, Vehicle::getCalcTaxPerMonth));
                System.out.printf("Total tax: %.2f\n", getSumNumber(vehicles, Vehicle::getCalcTaxPerMonth));
                System.out.printf("Average income: %.2f\n", getAverageNumber(vehicles, Vehicle::getTotalIncome));
                System.out.printf("Total income: %.2f\n", getSumNumber(vehicles, Vehicle::getTotalIncome));
                System.out.printf("Average profit: %.2f\n", getAverageNumber(vehicles, Vehicle::getTotalProfit));
                System.out.printf("Total profit: %.2f\n", getSumNumber(vehicles, Vehicle::getTotalProfit));
            }
            case "7" -> {
                List<Vehicle> vehicles = vehicleCollection.getVehicles();
                vehicles.forEach(mechanicService::detectBreaking);

                vehicles.stream()
                        .filter(mechanicService::isBroken)
                        .forEach(v -> System.out.println(v.getModelName() + " was broken"));

                System.out.println();
                vehicles.forEach(mechanicService::repair);
            }
            case "8" -> {
                System.out.println("\nPeriod between each auto diagnostics is one month");
                System.out.println("Last diagnostics was " + LocalDate.now());
            }
            default -> {
                System.err.println("Incorrect input!");
            }
        }
    }

    private static double getAverageNumber(List<Vehicle> vehicles, ToDoubleFunction<Vehicle> function) {
        return vehicles.stream()
                .mapToDouble(function)
                .average().getAsDouble();
    }

    private static double getSumNumber(List<Vehicle> vehicles, ToDoubleFunction<Vehicle> function) {
        return vehicles.stream()
                .mapToDouble(function)
                .sum();
    }

    private static void printVehicleInfo(Vehicle vehicle) {
        System.out.println("\nSelected vehicle: " + vehicle);
        System.out.println("\nInfo about engine: \n");
        Startable engine = vehicle.getStartable();
        if (engine instanceof ElectricalEngine e) {
            System.out.println("Engine capacity is " + e.getBatterySize());
            System.out.println("Consumption is " + e.getElectricityConsumption());
            System.out.println("Tax coefficient is " + e.getTaxCoefficient());
        }
        if (engine instanceof CombustionEngine e) {
            System.out.println("Engine capacity is " + e.getEngineCapacity());
            System.out.println("Consumption is " + e.getFuelConsumptionPer100());
            System.out.println("Tax coefficient is " + e.getTaxCoefficient());
        }

        System.out.printf("\nMax kilometers with full tank: %.2f\n", vehicle.getStartable().getMaxKilometers());
        System.out.println("\nList of rents by month: ");
        vehicle.getMachineOrders().forEach(e -> System.out.println("Rental date " + e.getRentalDate() +
                ", rent cost " + e.getRentCost()));
        System.out.printf("\nSum of road tax: %.2f\n", vehicle.getCalcTaxPerMonth());
        System.out.printf("Monthly income from the car: %.2f\n", vehicle.getTotalIncome());
        System.out.printf("Monthly profit from the car: %.2f\n", vehicle.getTotalProfit());
    }

    private static void printEqualVehiclesByKey(SortMode mode) {
        Function<Vehicle, String> predicate = null;
        switch (mode) {
            case VEHICLE_NAME -> predicate = v -> v.getType().getTypeName();
            case MODEL_NAME -> predicate = Vehicle::getModelName;
            case WEIGHT -> predicate = v -> String.valueOf(v.getWeight());
            case MANUFACTURE_YEAR -> predicate = v -> String.valueOf(v.getManufactureYear());
            case COLOR -> predicate = v -> v.getColor().toString();
            case STARTABLE -> predicate = v -> String.valueOf(v.getStartable().getMaxKilometers());
        }

        if (predicate != null) {
            Map<String, List<Vehicle>> grouped = vehicleCollection.getVehicles()
                    .stream()
                    .collect(Collectors.groupingBy(predicate));

            boolean noDuplicates = grouped.values().stream().allMatch(group -> group.size() == 1);

            if (noDuplicates) {
                System.out.println();
                System.out.println("Duplicates are not found!");
                return;
            }

            List<Vehicle> duplicates = grouped.values()
                    .stream()
                    .filter(group -> group.size() > 1)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            vehicleCollection.display(duplicates);
        }
    }

    private static void printSortedVehicles(SortMode mode) {
        Comparator<Vehicle> comparator = null;
        switch (mode) {
            case VEHICLE_NAME -> comparator = Comparator.comparing((Vehicle v) -> v.getType().getTypeName());
            case MODEL_NAME -> comparator = Comparator.comparing(Vehicle::getModelName);
            case REG_NUMBER -> comparator = Comparator.comparing(Vehicle::getRegistrationNumber);
            case WEIGHT -> comparator = Comparator.comparing(Vehicle::getWeight);
            case MANUFACTURE_YEAR -> comparator = Comparator.comparing(Vehicle::getManufactureYear);
            case COLOR -> comparator = Comparator.comparing(v -> v.getColor().toString());
            case STARTABLE -> comparator = Comparator.comparing(e -> e.getStartable().getMaxKilometers());
            case MILEAGE -> comparator = Comparator.comparing(Vehicle::getMileage);
            case TANK_VOLUME -> comparator = Comparator.comparing(Vehicle::getTankVolume);
        }

        if (comparator != null) {
            vehicleCollection.display(
                    vehicleCollection.getVehicles()
                            .stream()
                            .sorted(comparator)
                            .toList()
            );
        } else {
            vehicleCollection.display();
        }
    }

    private static void printSpecifiedTaxCoefficient(TaxCoefficientMode mode) {
        Vehicle vehicle = null;
        double avg = -1;
        System.out.println();
        String answerTemplate = " tax coefficient is %.2f\n";

        if (mode == TaxCoefficientMode.MAX) {
            vehicle = vehicleCollection.getVehicles().stream()
                    .max(Comparator.comparing(Vehicle::getCalcTaxPerMonth))
                    .orElse(null);
        } else if (mode == TaxCoefficientMode.MIN) {
            vehicle = vehicleCollection.getVehicles().stream()
                    .min(Comparator.comparing(Vehicle::getCalcTaxPerMonth))
                    .orElse(null);
        } else {
            avg = vehicleCollection.getVehicles().stream()
                    .mapToDouble(Vehicle::getCalcTaxPerMonth)
                    .average()
                    .getAsDouble();
        }

        if (vehicle == null && mode != TaxCoefficientMode.AVG) {
            System.out.println("Not found!");
            return;
        }
        if (mode == TaxCoefficientMode.MAX) {
            answerTemplate = "Max" + answerTemplate;
        }
        if (mode == TaxCoefficientMode.MIN) {
            answerTemplate = "Min" + answerTemplate;
        }
        if (mode == TaxCoefficientMode.AVG) {
            answerTemplate = "Average" + answerTemplate;
        }

        if (mode == TaxCoefficientMode.AVG) {
            System.out.printf(answerTemplate, avg);
            return;
        }

        System.out.printf(answerTemplate, vehicle.getCalcTaxPerMonth());
    }

    private enum TaxCoefficientMode {
        MAX,
        MIN,
        AVG
    }

    private enum SortMode {
        VEHICLE_NAME,
        MODEL_NAME,
        REG_NUMBER,
        WEIGHT,
        MANUFACTURE_YEAR,
        COLOR,
        STARTABLE,
        MILEAGE,
        TANK_VOLUME
    }
}
