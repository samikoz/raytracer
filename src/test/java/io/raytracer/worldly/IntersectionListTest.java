package io.raytracer.worldly;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.worldly.drawables.Sphere;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntersectionListTest {

    static IRay testRay;

    @BeforeAll
    static void setupMaterialAndPosition() {
        testRay = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
    }
    @Test
    void intersectionCount() {
        IIntersections intersections = new Intersections(
                new Intersection(testRay, 2.0, new Sphere()), new Intersection(testRay, 0, new Sphere()));

        assertEquals(2, intersections.count(), "Should have two intersections.");
    }

    @Test
    void intersectionGet() {
        Intersection i = new Intersection(testRay,2.0, new Sphere());
        IIntersections intersections = new Intersections(i);

        assertEquals(i, intersections.get(0), "Should return the first intersection.");
    }

    @Test
    void intersectionCombineWithEmpty() {
        Intersection i = new Intersection(testRay,2.0, new Sphere());
        Intersections intersections = new Intersections(i);
        IIntersections finalIntersections = intersections.combine(new Intersections());

        assertEquals( 1, intersections.count());
        assertEquals(i, finalIntersections.get(0), "The only intersection should be the first one");
    }

    @Test
    void intersectionCombineEmptyItself() {
        Intersection i = new Intersection(testRay,2.0, new Sphere());
        Intersections intersections = new Intersections();
        IIntersections finalIntersections = intersections.combine(new Intersections(i));

        assertEquals( 1, finalIntersections.count());
        assertEquals(i, finalIntersections.get(0), "The only intersection should be the first one");
    }

    @Test
    void intersectionCombine() {
        Intersection i = new Intersection(testRay,2.0, new Sphere());
        Intersections firstIntersections = new Intersections(i);
        Intersections secondIntersections = new Intersections(i);
        IIntersections finalIntersections = firstIntersections.combine(secondIntersections);

        assertEquals(2, finalIntersections.count());
        assertEquals(i, finalIntersections.get(0));
        assertEquals(i, finalIntersections.get(1));
    }

    @Test
    void intersectionCombineSorts() {
        Intersection iEarly = new Intersection(testRay,2.0, new Sphere());
        Intersection iLate = new Intersection(testRay,3.0, new Sphere());
        Intersections firstIntersections = new Intersections(iLate);
        Intersections secondIntersections = new Intersections(iEarly);
        IIntersections finalIntersections = firstIntersections.combine(secondIntersections);

        assertEquals(iEarly, finalIntersections.get(0), "The first intersection should be the earliest one.");
    }

    @Test
    void intersectionGetBeyondCount() {
        IIntersections intersections = new Intersections();

        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> intersections.get(0));
        String exceptionMessage = exception.getMessage();

        assertTrue(exceptionMessage.contains("Index out of bounds: 0"));
    }

    @Test
    void hitWithPositiveIntersections() {
        Intersection i1 = new Intersection(testRay,2.0, new Sphere());
        Intersection i2 = new Intersection(testRay,0.1, new Sphere());
        IIntersections intersections = new Intersections(i1, i2);

        assertEquals(i2, intersections.getHit().orElseThrow(NullPointerException::new),
                "Hit should be with the lowest positive time value.");
    }

    @Test
    void hitWithSomeNegativeIntersections() {
        Intersection i1 = new Intersection(testRay,2.0, new Sphere());
        Intersection i2 = new Intersection(testRay,-0.1, new Sphere());
        IIntersections intersections = new Intersections(i1, i2);

        assertEquals(i1, intersections.getHit().orElseThrow(NullPointerException::new),
                "Hit should be with the lowest positive time value.");
    }

    @Test
    void hitWithAllNegativeIntersections() {
        Intersection i1 = new Intersection(testRay,-2.0, new Sphere());
        Intersection i2 = new Intersection(testRay,-0.1, new Sphere());
        IIntersections intersections = new Intersections(i1, i2);

        assertFalse(intersections.getHit().isPresent(), "All negative intersections should produce no hit.");
    }
}
