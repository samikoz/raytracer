package io.raytracer.worldly;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.worldly.drawables.Sphere;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class HitTest {
    static IRay testRay;

    @BeforeAll
    static void setupMaterialAndPosition() {
        testRay = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
    }

    @Test
    void hitWithPositiveIntersections() {
        Intersection i1 = new Intersection(testRay,2.0, new Sphere());
        Intersection i2 = new Intersection(testRay,0.1, new Sphere());
        Optional<Hit> expectedHit = Hit.fromIntersections(new Intersection[] { i1, i2 });

        assertTrue(expectedHit.isPresent());
        assertEquals(0.1, expectedHit.get().rayParameter, 1e-3,
                "Hit should be with lowest positive parameter value");
    }

    @Test
    void hitWithSomeNegativeIntersections() {
        Intersection i1 = new Intersection(testRay,2.0, new Sphere());
        Intersection i2 = new Intersection(testRay,-0.1, new Sphere());
        Optional<Hit> expectedHit = Hit.fromIntersections(new Intersection[] { i1, i2 });

        assertTrue(expectedHit.isPresent());
        assertEquals(2.0, expectedHit.get().rayParameter, 1e-3,
                "Hit should be with the lowest positive parameter value.");
    }

    @Test
    void hitWithAllNegativeIntersections() {
        Intersection i1 = new Intersection(testRay,-2.0, new Sphere());
        Intersection i2 = new Intersection(testRay,-0.1, new Sphere());
        Optional<Hit> expectedHit = Hit.fromIntersections(new Intersection[] { i1, i2 });

        assertFalse(expectedHit.isPresent(), "All negative intersections should produce no hit.");
    }
}