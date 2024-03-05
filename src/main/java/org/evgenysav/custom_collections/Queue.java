package org.evgenysav.custom_collections;

public class Queue<T> {
    private T[] elements;
    private int size;

    public Queue(int size) {
        elements = (T[]) new Object[size];

    }

    public Queue() {
        elements = (T[]) new Object[10];
    }

    public void enqueue(T t) {
        if (size == elements.length) {
            grow();
        }
        int indexToInsert = getInsertIndex();
        if (indexToInsert != -1) {
            elements[indexToInsert] = t;
            size++;
        }
    }

    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }

        return removeFirst();
    }

    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }

        if (size >= 2) {
            return elements[1];
        }

        throw new IllegalStateException("There is only one element in a queue");
    }

    public int size() {
        return size;
    }

    public void printElements() {
        System.out.println();
        for (T t : elements) {
            if (t != null) {
                System.out.println(t);
            }
        }
    }

    private T removeFirst() {
        T firstElement = elements[0];
        elements[0] = null;
        for (int i = 1; i < elements.length; i++) {
            if (elements[i] != null) {
                T temp = elements[i];
                elements[i - 1] = temp;
                elements[i] = null;
            }
        }
        size--;

        return firstElement;
    }

    private boolean isEmpty() {
        return size == 0;
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

    private void grow() {
        int oldCapacity = elements.length;
        int newCapacity = 2 * oldCapacity;
        T[] dest = (T[]) new Object[newCapacity];
        System.arraycopy(elements, 0, dest, 0, oldCapacity);
        elements = dest.clone();
    }
}
