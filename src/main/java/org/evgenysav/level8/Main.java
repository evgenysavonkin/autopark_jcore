package org.evgenysav.level8;

import org.evgenysav.Vehicle;

public class Main {
    public static void main(String[] args) {
        Stack<Vehicle> stack = new Stack<>();
        for (int i = 1; i <= 15; i++) {
            stack.push(new Vehicle("Auto" + i));
            System.out.println("Auto" + i + " заехало в гараж");
        }
        System.out.println("Гараж заполнен");
        System.out.println("Размер стека: " + stack.size());
        System.out.println();
        for (int i = 1; i <= 15; i++) {
            System.out.println(stack.pop().getModelName() + " выехало из гаража");
        }
        System.out.println("Размер стека: " + stack.size());
    }
}
