package io.raytracer.mechanics;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Vector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RayTest {
    @Test
    void rayPosition() {
        IRay ray = new Ray(new Point(2, 3, 4), new Vector(1, 0, 0));
        IPoint expectedPosition = new Point(3, 3, 4);
        IPoint actualPosition = ray.getPosition(1);

        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    void translatingRay() {
        IVector direction = new Vector(0, 1, 0);
        IRay ray = new Ray(new Point(1, 2, 3), direction);
        IRay translatedRay = ray.getTransformed(ThreeTransform.translation(3, 4, 5));
        IPoint expectedOrigin = new Point(4, 6, 8);

        assertEquals(expectedOrigin, translatedRay.getOrigin(), "Translation should translate ray's origin.");
        assertEquals(direction, translatedRay.getDirection(),
                "Translation should not affect ray's direction.");
    }

    @Test
    void scalingRay() {
        IRay ray = new Ray(new Point(1, 2, 3), new Vector(0, 1, 0));
        IRay scaledRay = ray.getTransformed(ThreeTransform.scaling(2, 3, 4));
        IPoint expectedOrigin = new Point(2, 6, 12);
        IVector expectedDirection = new Vector(0, 3, 0);

        assertEquals(expectedOrigin, scaledRay.getOrigin(), "Scaling should scale ray's origin.");
        assertEquals(expectedDirection, scaledRay.getDirection(), "Scaling should scale ray's direction.");
    }
}
