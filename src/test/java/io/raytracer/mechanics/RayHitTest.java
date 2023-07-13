package io.raytracer.mechanics;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.shapes.Sphere;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RayHitTest {
    static IRay testRay;

    @BeforeAll
    static void setupMaterialAndPosition() {
        testRay = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
    }

    @Test
    void hitWithPositiveIntersections() {
        Intersection i1 = new Intersection(testRay,2.0, new Sphere());
        Intersection i2 = new Intersection(testRay,0.1, new Sphere());
        Optional<RayHit> expectedHit = RayHit.fromIntersections(Arrays.asList(i1, i2));

        assertTrue(expectedHit.isPresent());
        assertEquals(0.1, expectedHit.get().rayParameter, 1e-3,
                "Hit should be with lowest positive parameter value");
    }

    @Test
    void hitWithSomeNegativeIntersections() {
        Intersection i1 = new Intersection(testRay,2.0, new Sphere());
        Intersection i2 = new Intersection(testRay,-0.1, new Sphere());
        Optional<RayHit> expectedHit = RayHit.fromIntersections(Arrays.asList(i1, i2));

        assertTrue(expectedHit.isPresent());
        assertEquals(2.0, expectedHit.get().rayParameter, 1e-3,
                "Hit should be with the lowest positive parameter value.");
    }

    @Test
    void hitWithAllNegativeIntersections() {
        Intersection i1 = new Intersection(testRay,-2.0, new Sphere());
        Intersection i2 = new Intersection(testRay,-0.1, new Sphere());
        Optional<RayHit> expectedHit = RayHit.fromIntersections(Arrays.asList(i1, i2));

        assertFalse(expectedHit.isPresent(), "All negative intersections should produce no hit.");
    }

    @Test
    void hitpointCreation() {
        Ray ray = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));
        Sphere sphere = new Sphere();
        Optional<RayHit> hit = RayHit.fromIntersections(Collections.singletonList(new Intersection(ray,4, sphere)));

        assertTrue(hit.isPresent());
        RayHit hitpoint = hit.get();

        assertEquals(sphere, hitpoint.object);
        assertEquals(new Point(0, 0, -1), hitpoint.point);
        assertEquals(new Vector(0, 0, -1), hitpoint.eyeVector);
        assertEquals(new Vector(0, 0, -1), hitpoint.normalVector);
    }

    @Test
    void hitpointCreationFromInside() {
        Ray ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        Sphere sphere = new Sphere();
        Optional<RayHit> hit = RayHit.fromIntersections(Collections.singletonList(new Intersection(ray,1, sphere)));

        assertTrue(hit.isPresent());
        RayHit hitpoint = hit.get();

        assertEquals(new Point(0, 0, 1), hitpoint.point);
        assertEquals(new Vector(0, 0, -1), hitpoint.eyeVector);
        assertEquals(new Vector(0, 0, -1), hitpoint.normalVector,
                "Normal vector should be inverted");
    }
}