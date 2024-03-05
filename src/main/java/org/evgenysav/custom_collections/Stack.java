package org.evgenysav.custom_collections;

public class Stack<T> {
    private T[] elements;
    private int size;

    public Stack() {
        elements = (T[]) new Object[10];
    }

    public Stack(int capacity) {
        elements = (T[]) new Object[capacity];
    }

    public void push(T t) {
        if (size == elements.length) {
            grow();
        }
        int indexToInsert = getInsertIndex();
        if (indexToInsert != -1) {
            elements[indexToInsert] = t;
            size++;
        }
    }

    public T peek() {
        if (isEmpty()) {
            throw new IllegalArgumentException("Stack is empty");
        }

        return elements[size - 1];
    }

    public T pop() {
        if (isEmpty()) {
            throw new IllegalArgumentException("Stack is empty");
        }

        T lastElem = peek();
        elements[size - 1] = null;
        size--;
        return lastElem;
    }

    public void printElements() {
        for (T t : elements) {
            if (t != null) {
                System.out.println(t);
            }
        }
    }

    public int size() {
        return size;
    }

    private int getInsertIndex() {
        int counter = 0;
        for (T t : elements) {
            if (t == null) {
                return counter;
            }
            counter++;
        }

        return -1;
    }

    private boolean isEmpty() {
        return size == 0;
    }

    private void grow() {
        int oldCapacity = elements.length;
        int newCapacity = 2 * oldCapacity;
        T[] dest = (T[]) new Object[newCapacity];
        System.arraycopy(elements, 0, dest, 0, oldCapacity);
        elements = dest.clone();
    }
}
