package io.raytracer.geometry;

import io.raytracer.shapes.Plane;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LineTest {

    @Test
    void pointAt() {
        ILine aLine = new Line(new Point(0, 1, 3), new Point(0, 1, 0));
        ILine bLine = new Line(new Point(0, 1,3), new Vector(0, 0, -1));
        IPoint expectedPoint = new Point(0, 1, -3);

        assertEquals(expectedPoint, aLine.pointAt(2));
        assertEquals(expectedPoint, bLine.pointAt(6));
    }

    @Test
    void closestTo() {
        ILine aLine = new Line(new Point(5, 0, -3), new Vector(0, 0, 1));
        IPoint aPoint = new Point(15, 16, 0);
        IPoint closestPoint = new Point(5, 0, 0);

        assertEquals(closestPoint, aLine.closestTo(aPoint));
    }

    @Test
    void intersectPlane() {
        ILine line = new Line(new Point(0, 1, 0), new Vector(1, 1, 1));
        IPlane plane = new Plane(new Vector(0, 1, 0), new Point(-120, 13, 0));

        Optional<Double> intersection = line.intersect(plane);

        assertTrue(intersection.isPresent());
        assertEquals(12, intersection.get());
    }

    @Test
    void lineMissesPlane() {
        ILine line = new Line(new Point(0, 0, 0), new Vector(1, 0, 0));
        IPlane plane = new Plane(new Vector(0, 1, 0), new Point(12, 13, 14));

        assertFalse(line.intersect(plane).isPresent());
    }
}