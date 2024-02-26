package org.evgenysav.level7;

import org.evgenysav.Vehicle;

public class CarWashingQueue {
    private Vehicle[] vehicles;
    private int size;

    public CarWashingQueue(int size) {
        vehicles = new Vehicle[size];
    }

    public CarWashingQueue() {
        vehicles = new Vehicle[10];
    }

    public void enqueue(Vehicle vehicle) {
        if (size == vehicles.length) {
            grow();
        }
        int indexToInsert = getInsertIndex();
        if (indexToInsert != -1) {
            vehicles[indexToInsert] = vehicle;
            size++;
        }
    }

    public Vehicle dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }

        return removeFirst();
    }

    public Vehicle peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }

        if (size >= 2) {
            return vehicles[1];
        }

        throw new IllegalStateException("There is only one element in a queue");
    }

    public int size() {
        return size;
    }

    public void printVehicles() {
        System.out.println();
        for (Vehicle v : vehicles) {
            if (v != null) {
                System.out.println(v.getModelName());
            }
        }
    }

    private Vehicle removeFirst() {
        Vehicle firstVehicle = vehicles[0];
        vehicles[0] = null;
        for (int i = 1; i < vehicles.length; i++) {
            if (vehicles[i] != null) {
                Vehicle temp = vehicles[i];
                vehicles[i - 1] = temp;
                vehicles[i] = null;
            }
        }
        size--;

        return firstVehicle;
    }

    private boolean isEmpty() {
        return size == 0;
    }

    private int getInsertIndex() {
        int counter = 0;
        for (Vehicle v : vehicles) {
            if (v == null) {
                return counter;
            }
            counter++;
        }

        return -1;
    }

    private void grow() {
        int oldCapacity = vehicles.length;
        int newCapacity = 2 * oldCapacity;
        Vehicle[] dest = new Vehicle[newCapacity];
        System.arraycopy(vehicles, 0, dest, 0, oldCapacity);
        vehicles = dest.clone();
    }
}
