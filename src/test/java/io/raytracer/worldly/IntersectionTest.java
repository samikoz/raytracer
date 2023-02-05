package io.raytracer.worldly;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.worldly.drawables.Sphere;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntersectionTest {
    @Test
    void getMaterialPoint() {
        Ray ray = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));
        Sphere sphere = new Sphere();
        Intersection intersection = new Intersection(ray,4, sphere);
        MaterialPoint illuminated = intersection.getMaterialPoint();

        assertEquals(sphere, illuminated.object);
        assertEquals(new Point(0, 0, -1), illuminated.point);
        assertEquals(new Vector(0, 0, -1), illuminated.eyeVector);
        assertEquals(new Vector(0, 0, -1), illuminated.normalVector);
    }

    @Test
    void getMaterialPointFromInside() {
        Ray ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        Sphere sphere = new Sphere();
        Intersection intersection = new Intersection(ray,1, sphere);
        MaterialPoint illuminated = intersection.getMaterialPoint();

        assertEquals(new Point(0, 0, 1), illuminated.point);
        assertEquals(new Vector(0, 0, -1), illuminated.eyeVector);
        assertEquals(new Vector(0, 0, -1), illuminated.normalVector,
                "Normal vector should be inverted");
    }
}