package io.raytracer.shapes;

import io.raytracer.geometry.Interval;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.mechanics.BBox;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Ray;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiscTest {

    @Test
    void getBoundingBox() {
        Disc disc = new Disc();
        BBox bbox = disc.getBoundingBox();

        IRay missingRay = new Ray(new Point(1+1e-3, 0.75, -2), new Vector(0, 0, 1));
        IRay missingVerticalRay = new Ray(new Point(0, -2, 2e-3), new Vector(0, 1, 0));
        IRay hittingRay = new Ray(new Point(1-1e-3, 0.8, -2), new Vector(0, 0, 1));

        assertFalse(bbox.isHit(missingRay, new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)));
        assertFalse(bbox.isHit(missingVerticalRay, new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)));
        assertTrue(bbox.isHit(hittingRay, new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)));
    }
}