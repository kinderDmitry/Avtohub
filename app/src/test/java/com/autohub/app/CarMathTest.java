package com.autohub.app;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CarMathTest {
    @Test public void fuelTotalIsExact() { assertEquals(2500.0, CarMath.total(50,50), 0.0001); }
    @Test public void consumptionIsLitersPer100Km() { assertEquals(8.0, CarMath.consumption(40,500), 0.0001); }
    @Test public void costPerKmIsSafeForZeroDistance() { assertEquals(0.0, CarMath.costPerKm(1000,0), 0.0001); }
    @Test public void oneTankConsumptionIsStable() { assertEquals(6.25, CarMath.consumption(25,400), 0.0001); }
    @Test public void largeValuesRemainFinite() { assertEquals(100.0, CarMath.costPerKm(1000000,10000), 0.0001); }
    @Test(expected = IllegalArgumentException.class) public void negativeLitersRejected() { CarMath.total(-1,50); }
    @Test(expected = IllegalArgumentException.class) public void negativeDistanceRejected() { CarMath.consumption(10,-1); }
}
