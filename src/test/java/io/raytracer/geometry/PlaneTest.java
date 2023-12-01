package io.raytracer.geometry;

import io.raytracer.algebra.ThreeTransform;
import io.raytracer.shapes.Plane;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlaneTest {
    @Test
    void doesContain() {
        IPlane aPlane = new Plane(new Vector(1, 0, 0), new Point(8, 5, 6));

        assertTrue(aPlane.doesContain(new Point(8, -12, 1245)));
        assertFalse(aPlane.doesContain(new Point(7.9, 0 ,0)));
    }

    @Test
    void intersectHits() {
        IPlane aPlane = new Plane(new Vector(0, 1, 0), new Point(17, 5, 0));
        ILine aLine = new Line(new Point(-1, -1, 0), new Vector(1, 1, 0));
        IPoint intersection = new Point(5, 5, 0);

        Optional<IPoint> actualIntersection = aPlane.intersect(aLine);

        assertTrue(actualIntersection.isPresent());
        assertEquals(intersection, actualIntersection.get());
    }

    @Test
    void intersectionMisses() {
        IPlane aPlane = new Plane(new Vector(0, 1, 0), new Point(17, 5, 0));
        ILine aLine = new Line(new Point(-1, -1, 0), new Vector(1, 0, -2));

        Optional<IPoint> actualIntersection = aPlane.intersect(aLine);

        assertFalse(actualIntersection.isPresent());
    }

    @Test
    void translate() {
        IVector normal = new Vector(0, 1, 0);
        Plane aPlane = new Plane(normal, new Point(0, 0, 0));
        aPlane.setTransform(ThreeTransform.translation(5, 5, 0));

        ILine testLine = new Line(new Point(0, 10, 0), new Vector(0, -1, 0));
        Optional<Double> testIntersection = testLine.intersect(aPlane);

        assertTrue(testIntersection.isPresent());
        assertEquals(normal, aPlane.getNormal());
        assertEquals(5, testIntersection.get(), 1e-3);
    }

    @Test
    void rotate_x() {
        IPoint aPoint = new Point(5, 2, 0);
        Plane aPlane = new Plane(new Vector(0, 1, 0), aPoint);
        aPlane.setTransform(ThreeTransform.rotation_x(Math.PI / 2));

        ILine testLine = new Line(new Point(0, 0, 0), new Vector(1, 1, 1));
        Optional<Double> testIntersection = testLine.intersect(aPlane);

        assertTrue(testIntersection.isPresent());
        assertEquals(new Vector(0, 0, 1), aPlane.getNormal());
        assertEquals(new Point(5, 0, 2), aPlane.getPoint());
        assertEquals(2, testIntersection.get(), 1e-3);
    }

    @Test
    void rotate_y() {
        IPoint aPoint = new Point(5, 0 ,-2);
        Plane aPlane = new Plane(new Vector(0, 0, 1), aPoint);
        aPlane.setTransform(ThreeTransform.rotation_y(Math.PI / 2));

        ILine testLine = new Line(new Point(0, 0, 0), new Vector(1, 1, 1));
        Optional<Double> testIntersection = testLine.intersect(aPlane);

        assertTrue(testIntersection.isPresent());
        assertEquals(new Vector(1, 0, 0), aPlane.getNormal());
        assertEquals(new Point(-2, 0, -5), aPlane.getPoint());
        assertEquals(-2, testIntersection.get(), 1e-3);
    }

    @Test
    void reflect() {
        IPoint aPoint = new Point(3, 1, -1);
        IPlane aPlane = new Plane(new Vector(1, 1, 1), new Point(2, 0 ,0));

        assertEquals(new Point(1, -1, -3), aPlane.reflect(aPoint));
    }
}