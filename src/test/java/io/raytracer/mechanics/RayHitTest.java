package io.raytracer.mechanics;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.shapes.Sphere;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RayHitTest {
    static IRay testRay;

    @BeforeAll
    static void setupMaterialAndPosition() {
        testRay = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
    }

    @Test
    void hitChoosingIntersectionCorrectly() {
        Intersection i1 = new Intersection(new Sphere(), testRay,2.0);
        Intersection i2 = new Intersection(new Sphere(), testRay,-0.1);
        Optional<RayHit> expectedHit = RayHit.fromIntersections(Arrays.asList(i1, i2));

        assertTrue(expectedHit.isPresent());
        assertEquals(2, expectedHit.get().rayParameter, 1e-3,
                "Hit should be with lowest positive parameter value");
    }

    @Test
    void hitpointCreation() {
        Ray ray = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));
        Sphere sphere = new Sphere();
        Optional<RayHit> hit = RayHit.fromIntersections(Collections.singletonList(new Intersection(sphere, ray, 4)));

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
        Optional<RayHit> hit = RayHit.fromIntersections(Collections.singletonList(new Intersection(sphere, ray,1)));

        assertTrue(hit.isPresent());
        RayHit hitpoint = hit.get();

        assertEquals(new Point(0, 0, 1), hitpoint.point);
        assertEquals(new Vector(0, 0, -1), hitpoint.eyeVector);
        assertEquals(new Vector(0, 0, -1), hitpoint.normalVector,
                "Normal vector should be inverted");
    }
}