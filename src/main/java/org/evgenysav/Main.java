package org.evgenysav;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws NotVehicleException {
        VehicleCollection vehicleCollection = new VehicleCollection("types", "vehicles", "rents");
        vehicleCollection.display();
        Vehicle vehicle = new Vehicle(8, 3, new DieselEngine(1.6, 7.2, 55),
                new VehicleType("Car", 1.1), "Golf 137", "8682 AX-7",
                1200, 2006, 230451, Color.GRAY, new Random().nextInt(100));
        vehicleCollection.insert(6, vehicle);
        vehicleCollection.delete(1);
        vehicleCollection.delete(4);
        vehicleCollection.display();
        vehicleCollection.sort(new VehicleComparator());
        vehicleCollection.display();
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