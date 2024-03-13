package org.evgenysav.classes;

import org.evgenysav.exceptions.DefectedVehicleException;
import org.evgenysav.exceptions.NotVehicleException;

import java.util.*;
import java.util.stream.Stream;

public class Main {
    private static final MechanicService mechanicService = new MechanicService();

    public static void main(String[] args) throws NotVehicleException {
        VehicleCollection vehicleCollection = new VehicleCollection("types",
                "vehicles", "rents");
        List<Vehicle> vehicles = vehicleCollection.getVehicles();
        printVehicleWithMaxBreakDowns(vehicles);
        vehicleCollection.getVehicles().forEach(mechanicService::repair);
        for (Vehicle v : vehicles) {
            try {
                rentVehicle(v);
            } catch (DefectedVehicleException e) {
                System.err.println(e.getMessage());
            }

            System.out.println("Vehicle with id = " + v.getVehicleId() + " with model name \"" + v.getModelName() + "\" is rented now");
        }
    }

    private static void printVehicleWithMaxBreakDowns(List<Vehicle> vehicles) {
        List<Map<String, Integer>> list = new ArrayList<>();
        for (Vehicle v : vehicles) {
            list.add(new LinkedHashMap<>(mechanicService.detectBreaking(v)));
        }
        int maxNumberOfBreaking = 0;
        Set<Map.Entry<String, Integer>> entrySetFromMaxBreakingCar = null;
        for (Map<String, Integer> map : list) {
            int sum = 0;
            for (var entry : map.entrySet()) {
                sum += entry.getValue();
            }
            if (sum > maxNumberOfBreaking) {
                maxNumberOfBreaking = sum;
                entrySetFromMaxBreakingCar = map.entrySet();
            }

        }

        StringBuilder stringBuilder = new StringBuilder();
        if (entrySetFromMaxBreakingCar != null) {
            int counter = 0;
            for (var entry : entrySetFromMaxBreakingCar) {
                if (counter == entrySetFromMaxBreakingCar.size() - 1) {
                    stringBuilder.append(entry.getKey()).append(",").append(entry.getValue());
                    break;
                }
                stringBuilder.append(entry.getKey()).append(",").append(entry.getValue()).append(",");
                counter++;
            }
        }

        int vehicleIdWithMaxBreaking = mechanicService.findVehicleIdByStringFromFile(stringBuilder.toString());
        for (Vehicle v : vehicles) {
            if (v.getVehicleId() == vehicleIdWithMaxBreaking) {
                System.out.println("A car with most breakdowns numbers is " + v);
            }
        }
    }

    private static boolean rentVehicle(Vehicle vehicle) {
        if (mechanicService.isBroken(vehicle)) {
            throw new DefectedVehicleException("Vehicle with id = " + vehicle.getVehicleId() + " is defected");
        }

        vehicle.setRented(true);
        return true;
    }

    private static class Helper {
        private static void printCars(Vehicle[] vehicles) {
            Stream.of(vehicles)
                    .forEach(System.out::println);
        }

        public static void printDuplicates(Vehicle[] vehicles) {
            Map<Vehicle, Integer> map = new HashMap<>();
            for (Vehicle v : vehicles) {
                if (map.containsKey(v)) {
                    if (map.get(v) >= 2) {
                        continue;
                    }
                    int counter = map.get(v);
                    map.replace(v, ++counter);
                    System.out.println("Duplicate: " + v);
                    continue;
                }

                map.put(v, 1);
            }
            if (map.size() == vehicles.length) {
                System.out.println("No duplicates were found!");
            }
        }

        public static void printMaxKilometersVehicle(Vehicle[] vehicles) {
            Vehicle vehicle = Stream.of(vehicles)
                    .distinct()
                    .max(Comparator.comparingDouble(v -> v.getStartable().getMaxKilometers()))
                    .orElse(null);
            if (vehicle != null) {
                System.out.println("Vehicle that will pass max km is " + vehicle);
            } else {
                System.out.println("Not found!");
            }
        }


        private static void sortCars(Vehicle[] vehicles) {
            QuickSort.sort(vehicles);
        }

        private static class QuickSort {
            private static void sort(Vehicle[] arr) {
                sort(arr, 0, arr.length - 1);
            }

            private static void sort(Vehicle[] arr, int low, int high) {
                if (low < high) {
                    int pi = partition(arr, low, high);
                    sort(arr, low, pi - 1);
                    sort(arr, pi + 1, high);
                }
            }

            private static int partition(Vehicle[] arr, int low, int high) {
                Vehicle pivot = arr[high];
                int i = (low - 1);
                for (int j = low; j < high; j++) {
                    if (arr[j].compareTo(pivot) < 0) {
                        i++;

                        Vehicle temp = arr[i];
                        arr[i] = arr[j];
                        arr[j] = temp;
                    }
                }

                Vehicle temp = arr[i + 1];
                arr[i + 1] = arr[high];
                arr[high] = temp;

                return i + 1;
            }
        }
    }
}