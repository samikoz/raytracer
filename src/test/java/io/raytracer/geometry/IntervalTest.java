package io.raytracer.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntervalTest {

    @Test
    void size() {
        Interval i = new Interval(5.5, 13);
        assertEquals(7.5, i.size(), 1e-6);
    }

    @Test
    void newIntervalFromTwoOther() {
        Interval i1 = new Interval(1, 2);
        Interval i2 = new Interval(-1, 0);
        Interval iNew = new Interval(i1, i2);

        assertEquals(-1, iNew.min, 1e-6);
        assertEquals(2, iNew.max, 1e-6);
    }
}