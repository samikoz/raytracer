package io.raytracer.shapes;

import io.raytracer.geometry.Interval;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.mechanics.BBox;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.Ray;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RectangleTest {

    @Test
    void rayHitsRectangle() {
        Rectangle rectangle = new Rectangle();
        IRay hittingRay = new Ray(new Point(1, 1, -2), new Vector(-0.2, -0.3, 1));
        Intersection[] firstHit = rectangle.getLocalIntersections(hittingRay, new Interval(0, Double.POSITIVE_INFINITY));
        IRay rayStartingAtRectangle = new Ray(new Point(0.5, 0.5, 0), new Vector(5, -2, 13));
        Intersection[] secondHit = rectangle.getLocalIntersections(rayStartingAtRectangle, new Interval(0, Double.POSITIVE_INFINITY));

        assertEquals(2, firstHit[0].rayParameter, 1e-6);
        assertEquals(0, secondHit[0].rayParameter, 1e-6);
    }

    @Test
    void rayMissesRectangle() {
        Rectangle rectangle = new Rectangle();
        IRay parallelRay = new Ray(new Point(-3, 2, 0), new Vector(1, 2, 0));
        IRay missingRay = new Ray(new Point(0.5, -0.2, -3), new Vector(1, 1, 1));

        assertEquals(0, rectangle.getLocalIntersections(parallelRay, new Interval(0, Double.POSITIVE_INFINITY)).length);
        assertEquals(0, rectangle.getLocalIntersections(missingRay, new Interval(0, Double.POSITIVE_INFINITY)).length);
    }

    @Test
    void rectangleBBOx() {
        Rectangle rectangle = new Rectangle();
        BBox bbox = rectangle.getBoundingBox();

        IRay missingRay = new Ray(new Point(-1e-3, 0.75, -2), new Vector(0, 0, 1));
        IRay missingVerticalRay = new Ray(new Point(0, -2, 2e-3), new Vector(0, 1, 0));
        IRay hittingRay = new Ray(new Point(1-1e-3, 0.8, -2), new Vector(0, 0, 1));

        assertFalse(bbox.isHit(missingRay, new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)));
        assertFalse(bbox.isHit(missingVerticalRay, new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)));
        assertTrue(bbox.isHit(hittingRay, new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)));
    }
}