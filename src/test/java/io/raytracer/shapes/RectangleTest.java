package io.raytracer.shapes;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Ray;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class RectangleTest {

    @Test
    void rayHitsRectangle() {
        Rectangle rectangle = new Rectangle();
        IRay hittingRay = new Ray(new Point(1, 1, -2), new Vector(-0.2, -0.3, 1));
        double[] firstHit = rectangle.getLocalIntersectionPositions(hittingRay);
        IRay rayStartingAtRectangle = new Ray(new Point(0.5, 0.5, 0), new Vector(5, -2, 13));
        double[] secondHit = rectangle.getLocalIntersectionPositions(rayStartingAtRectangle);

        assertEquals(2, firstHit[0], 1e-6);
        assertEquals(0, secondHit[0], 1e-6);
    }

    @Test
    void rayMissesRectangle() {
        Rectangle rectangle = new Rectangle();
        IRay parallelRay = new Ray(new Point(-3, 2, 0), new Vector(1, 2, 0));
        IRay missingRay = new Ray(new Point(0.5, -0.2, -3), new Vector(1, 1, 1));

        assertEquals(0, rectangle.getLocalIntersectionPositions(parallelRay).length);
        assertEquals(0, rectangle.getLocalIntersectionPositions(missingRay).length);
    }
}