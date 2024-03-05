package org.evgenysav.level10;

import org.evgenysav.MechanicService;
import org.evgenysav.Vehicle;
import org.evgenysav.VehicleCollection;
import org.evgenysav.level7.CarWashingQueue;
import org.evgenysav.level8.Stack;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class ConsoleApp {
    private static final MechanicService mechanicService = new MechanicService();
    private static final VehicleCollection vehicleCollection = new VehicleCollection("types",
            "vehicles", "rents");

    public static void main(String[] args) {
        List<Vehicle> vehicles = vehicleCollection.getVehicles();
        List<Vehicle> volkswagenVehicles = vehicles.stream()
                .filter(v -> v.getModelName().contains("Volkswagen"))
                .toList();
        volkswagenVehicles.forEach(System.out::println);

        System.out.println(volkswagenVehicles.stream()
                .max(Comparator.comparing(Vehicle::getManufactureYear))
                .orElse(new Vehicle()));
        task7WithStreams();
        task8WithStreams();
        getBrokenVehicles(vehicles)
                .forEach(e -> {
                    if (e.getDefectCount() == 0) {
                        System.out.println(e.getModelName() + " wasn't defected");
                    } else {
                        System.out.println("Defected details count for auto \"" + e.getModelName() + "\" is " + e.getDefectCount());
                    }
                    mechanicService.repair(e);
                });
    }

    private static void task7WithStreams() {
        CarWashingQueue<Vehicle> queue = new CarWashingQueue<>();
        IntStream.range(1, 6)
                .forEach(i -> {
                    queue.enqueue(new Vehicle("Auto" + i));
                    System.out.println("Auto" + i + " has entered washing");
                });
        IntStream.range(1, 6)
                .forEach(i -> System.out.println(queue.dequeue() + " was washed"));
    }

    private static void task8WithStreams() {
        Stack<Vehicle> stack = new Stack<>();
        IntStream.range(1, 16)
                .forEach(i -> {
                    stack.push(new Vehicle("Auto" + i));
                    System.out.println("Auto" + i + " заехало в гараж");
                });

        IntStream.range(1, 16)
                .forEach(i -> System.out.println(stack.pop().getModelName() + " выехало из гаража"));
    }

    private static List<Vehicle> getBrokenVehicles(List<Vehicle> vehicles) {
        return vehicles.stream()
                .filter(x -> !mechanicService.detectBreaking(x).isEmpty())
                .toList();
    }

    private static List<Vehicle> sortByNumberOfDefectsDesc(List<Vehicle> vehicles) {
        return vehicles.stream()
                .sorted(Comparator
                        .comparing(Vehicle::getDefectCount)
                        .reversed())
                .toList();
    }

    private static Vehicle findVehicleWithMaxTax(List<Vehicle> vehicles) {
        return vehicles.stream()
                .max(Comparator
                        .comparing(Vehicle::getCalcTaxPerMonth))
                .stream()
                .findFirst()
                .orElse(new Vehicle());
    }
}
