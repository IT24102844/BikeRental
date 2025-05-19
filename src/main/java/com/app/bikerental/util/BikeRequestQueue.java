package com.app.bikerental.util;

import com.app.bikerental.model.BikeRequest;

public class BikeRequestQueue {
    private static final int MAX_SIZE = 100;
    private BikeRequest[] requests;
    private int front;
    private int rear;
    private int size;

    public BikeRequestQueue() {
        requests = new BikeRequest[MAX_SIZE];
        front = 0;
        rear = -1;
        size = 0;
    }

    public synchronized void enqueue(BikeRequest request) {
        if (isFull()) {
            throw new IllegalStateException("Queue is full");
        }
        rear = (rear + 1) % MAX_SIZE;
        requests[rear] = request;
        size++;
    }

    public synchronized BikeRequest dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        BikeRequest request = requests[front];
        front = (front + 1) % MAX_SIZE;
        size--;
        return request;
    }

    public synchronized BikeRequest peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return requests[front];
    }

    public synchronized boolean isEmpty() {
        return size == 0;
    }

    public synchronized boolean isFull() {
        return size == MAX_SIZE;
    }

    public synchronized int size() {
        return size;
    }

    public synchronized void clear() {
        front = 0;
        rear = -1;
        size = 0;
    }

    // New method to find requests by bike type
    public synchronized BikeRequest findRequestByBikeType(String bikeType) {
        for (int i = 0; i < size; i++) {
            int index = (front + i) % MAX_SIZE;
            if (requests[index].getBikeType().equalsIgnoreCase(bikeType)
                    && requests[index].getStatus().equals("PENDING")) {
                return requests[index];
            }
        }
        return null;
    }
}