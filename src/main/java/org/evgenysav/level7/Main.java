package org.evgenysav.level7;

import org.evgenysav.Vehicle;

public class Main {
    public static void main(String[] args) {
        CarWashingQueue<Vehicle> carWashingQueue = new CarWashingQueue<Vehicle>();
        for (int i = 1; i <= 5; i++) {
            carWashingQueue.enqueue(new Vehicle("Auto" + i));
            System.out.println("Auto" + i + " has entered washing");
        }
        System.out.println("size is " + carWashingQueue.size());
        for (int i = 1; i <= 5; i++) {
            System.out.println(carWashingQueue.dequeue() + " was washed");
        }
        System.out.println("size is " + carWashingQueue.size());
    }
}
