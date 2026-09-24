package com.autohub.app;

/** Pure calculations used by the vehicle statistics layer. No Android dependencies. */
public final class CarMath {
    private CarMath() {}
    public static double total(double liters, double pricePerLiter) {
        if (liters < 0 || pricePerLiter < 0) throw new IllegalArgumentException("negative value");
        return liters * pricePerLiter;
    }
    public static double consumption(double liters, double distanceKm) {
        if (liters < 0 || distanceKm < 0) throw new IllegalArgumentException("negative value");
        if (distanceKm == 0) return 0;
        return liters / distanceKm * 100.0;
    }
    public static double costPerKm(double totalCost, double distanceKm) {
        if (totalCost < 0 || distanceKm < 0) throw new IllegalArgumentException("negative value");
        if (distanceKm == 0) return 0;
        return totalCost / distanceKm;
    }
}
