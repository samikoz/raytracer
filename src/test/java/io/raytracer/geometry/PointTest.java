package io.raytracer.geometry;

import io.raytracer.shapes.Plane;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void project() {
        IPoint projectionCentre = new Point(1, 0, 0);
        IPlane projectionPlane = new Plane(new Vector(1, 0, 0), new Point(2, 0, 0));
        IPoint projectionPoint = new Point(16, 10, 0);
        IPoint expectedProjection = new Point(2, 2.0/3, 0);

        Optional<IPoint> projection = projectionPoint.project(projectionPlane, projectionCentre);

        assertTrue(projection.isPresent());
        assertEquals(expectedProjection, projection.get());
    }

    @Test
    void projectFails() {
        IPoint projectionCentre = new Point(1, 0, 0);
        IPlane projectionPlane = new Plane(new Vector(1, 0, 0), new Point(2, 0, 0));
        IPoint projectionPoint = new Point(1, 100, 0);

        Optional<IPoint> projection = projectionPoint.project(projectionPlane, projectionCentre);

        assertFalse(projection.isPresent());
    }
}