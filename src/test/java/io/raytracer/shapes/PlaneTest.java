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

class PlaneTest {
    @Test
    void normalToPlaneIsConstant() {
        Plane plane = new Plane();

        assertEquals(new Vector(0, 1, 0), plane.localNormalAt(new Point(0, 0, 0), 0, 0));
        assertEquals(new Vector(0, 1, 0), plane.localNormalAt(new Point(10, 0, -150), 0, 0));
    }

    @Test
    void noIntersectionsForParallelRay() {
        Plane plane = new Plane();
        IRay parallelRay = new Ray(new Point(0, 10, 0), new Vector(0, 0, 1));

        Intersection[] intersectPositions = plane.getLocalIntersections(parallelRay, Interval.positiveReals());

        assertEquals(0, intersectPositions.length);
    }

    @Test
    void noIntersectionsForCoplanarRay() {
        Plane plane = new Plane();
        IRay coplanarRay = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));

        Intersection[] intersectPositions = plane.getLocalIntersections(coplanarRay, Interval.positiveReals());

        assertEquals(0, intersectPositions.length);
    }

    @Test
    void intersectionsWhenRayCrossingFromAbove() {
        Plane plane = new Plane();
        IRay ray = new Ray(new Point(0, 1, 0), new Vector(0, -1, 0));

        Intersection[] intersectPositions = plane.getLocalIntersections(ray, Interval.positiveReals());

        assertEquals(1, intersectPositions.length);
        assertEquals(1, intersectPositions[0].rayParameter);
    }

    @Test
    void intersectionsWhenRayCrossingFromBelow() {
        Plane plane = new Plane();
        IRay ray = new Ray(new Point(0, -1, 0), new Vector(0, 1, 0));

        Intersection[] intersectPositions = plane.getLocalIntersections(ray, Interval.positiveReals());

        assertEquals(1, intersectPositions.length);
        assertEquals(1, intersectPositions[0].rayParameter);
    }

    @Test
    void planeBBox() {
        Plane plane = new Plane();
        BBox bbox = plane.getBoundingBox();

        IRay parallelRay = new Ray(new Point(1e-2, 1e-2, 1e-2), new Vector(-1, 0, -1));
        IRay hittingRay = new Ray(new Point(50, 15, -120), new Vector(23, -1, -10));

        assertFalse(bbox.isHit(parallelRay, Interval.allReals()));
        assertTrue(bbox.isHit(hittingRay, Interval.allReals()));
    }
}