package io.raytracer.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PointTest {
    @Test
    void additionOfPointAndVector() {
        IPoint aPoint = new Point(12.0, 3.7, -0.2);
        IVector aVector = new Vector(-5.0, 0.2, 0.0);
        IPoint expectedSum = new Point(7.0, 3.9, -0.2);
        IPoint actualSum = aPoint.add(aVector);

        assertEquals(expectedSum, actualSum);
    }

    @Test
    void subtractionOfTwoPoints() {
        IPoint first = new Point(-1.0, -1.0, 0.0);
        IPoint second = new Point(2.0, -2.0, 5.0);
        IVector expectedDifference = new Vector(-3.0, 1.0, -5.0);
        IVector actualDifference = first.subtract(second);

        assertEquals(expectedDifference, actualDifference);
    }
}