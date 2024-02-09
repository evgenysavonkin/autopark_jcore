package org.evgenysav;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        VehicleType bus = new VehicleType("Bus", 1.2);
        VehicleType car = new VehicleType("Car", 1);
        VehicleType rink = new VehicleType("Rink", 1.5);
        VehicleType tractor = new VehicleType("Tractor", 1.2);
        VehicleType[] types = new VehicleType[]{bus, car, rink, tractor};

        Random random = new Random();
        Vehicle vehicle1 = new Vehicle(new GasolineEngine(2, 8.1, 75),
                types[0], "Volkswagen Crafter", "5427 AX-7",
                2022, 2015, 37600, Color.BLUE, random.nextInt(100));
        Vehicle vehicle2 = new Vehicle(new GasolineEngine(2.18, 8.5, 75),
                types[0], "Volkswagen Crafter", "6427 AA-7",
                2500, 2014, 227010, Color.WHITE, random.nextInt(100));
        Vehicle vehicle3 = new Vehicle(new ElectricalEngine(50, 150),
                types[0], "Electric Bus E321", "6785 BA-7",
                12080, 2019, 20451, Color.GREEN, random.nextInt(100));
        Vehicle vehicle4 = new Vehicle(new DieselEngine(1.6, 7.2, 55),
                types[1], "Golf 5", "8682 AX-7",
                1200, 2006, 230451, Color.GRAY, random.nextInt(100));
        Vehicle vehicle5 = new Vehicle(new ElectricalEngine(25, 70),
                types[1], "Tesla Model S 70D", "0001 AA-7",
                2200, 2019, 10454, Color.WHITE, random.nextInt(100));
        Vehicle vehicle6 = new Vehicle(new DieselEngine(3.2, 25, 20),
                types[2], "Hamm HD 12 VV", null,
                3000, 2016, 122, Color.YELLOW, random.nextInt(100));
        Vehicle vehicle7 = new Vehicle(new DieselEngine(4.75, 20.1, 135),
                types[3], "МТЗ Беларус-1025.4", "1145 AB-7",
                1200, 2020, 109, Color.RED, random.nextInt(100));
        Vehicle[] vehicles = new Vehicle[]{vehicle1, vehicle2, vehicle3, vehicle4, vehicle5, vehicle6, vehicle7};
        Helper.printCars(vehicles);
        Helper.printDuplicates(vehicles);
        Helper.printMaxKilometersVehicle(vehicles);
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