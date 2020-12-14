package io.raytracer.light;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntersectionListTest {
    @Test
    void intersectionCount() {
        IntersectionList intersections = new IntersectionListImpl(
                new Intersection(2.0, new Sphere()), new Intersection(0, new Sphere()));

        assertEquals(2, intersections.count(), "Should have two intersections.");
    }

    @Test
    void intersectionGet() {
        Intersection i = new Intersection(2.0, new Sphere());
        IntersectionList intersections = new IntersectionListImpl(i);

        assertEquals(i, intersections.get(0), "Should return the first intersection.");
    }

    @Test
    void intersectionGetBeyondCount() {
        IntersectionList intersections = new IntersectionListImpl();

        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> intersections.get(0));
        String exceptionMessage = exception.getMessage();

        assertTrue(exceptionMessage.contains("Index out of bounds: 0"));
    }

    @Test
    void hitWithPositiveIntersections() {
        Intersection i1 = new Intersection(2.0, new Sphere());
        Intersection i2 = new Intersection(0.1, new Sphere());
        IntersectionList intersections = new IntersectionListImpl(i1, i2);

        assertEquals(i2, intersections.hit().orElseThrow(NullPointerException::new),
                "Hit should be with the lowest positive time value.");
    }

    @Test
    void hitWithSomeNegativeIntersections() {
        Intersection i1 = new Intersection(2.0, new Sphere());
        Intersection i2 = new Intersection(-0.1, new Sphere());
        IntersectionList intersections = new IntersectionListImpl(i1, i2);

        assertEquals(i1, intersections.hit().orElseThrow(NullPointerException::new),
                "Hit should be with the lowest positive time value.");
    }

    @Test
    void hitWithAllNegativeIntersections() {
        Intersection i1 = new Intersection(-2.0, new Sphere());
        Intersection i2 = new Intersection(-0.1, new Sphere());
        IntersectionList intersections = new IntersectionListImpl(i1, i2);

        assertFalse(intersections.hit().isPresent(), "All negative intersections should produce no hit.");
    }
}
