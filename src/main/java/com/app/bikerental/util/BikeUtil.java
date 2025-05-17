package com.app.bikerental.util;

import com.app.bikerental.model.Bike;

import java.util.List;

public class BikeUtil {

    public static void quickSortByAvailability(List<Bike> bikes, int low, int high) {
        if (low < high) {
            int pi = partition(bikes, low, high);
            quickSortByAvailability(bikes, low, pi - 1);
            quickSortByAvailability(bikes, pi + 1, high);
        }
    }

    private static int partition(List<Bike> bikes, int low, int high) {
        boolean pivot = bikes.get(high).isAvailable();
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (!bikes.get(j).isAvailable() && pivot) { // put unavailable before available
                i++;
                Bike temp = bikes.get(i);
                bikes.set(i, bikes.get(j));
                bikes.set(j, temp);
            }
        }

        Bike temp = bikes.get(i + 1);
        bikes.set(i + 1, bikes.get(high));
        bikes.set(high, temp);

        return i + 1;
    }
}
