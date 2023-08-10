package io.raytracer.shapes;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.Ray;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

        Intersection[] intersectPositions = plane.getLocalIntersections(parallelRay, 0, Double.POSITIVE_INFINITY);

        assertEquals(0, intersectPositions.length);
    }

    @Test
    void noIntersectionsForCoplanarRay() {
        Plane plane = new Plane();
        IRay coplanarRay = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));

        Intersection[] intersectPositions = plane.getLocalIntersections(coplanarRay, 0, Double.POSITIVE_INFINITY);

        assertEquals(0, intersectPositions.length);
    }

    @Test
    void intersectionsWhenRayCrossingFromAbove() {
        Plane plane = new Plane();
        IRay ray = new Ray(new Point(0, 1, 0), new Vector(0, -1, 0));

        Intersection[] intersectPositions = plane.getLocalIntersections(ray, 0, Double.POSITIVE_INFINITY);

        assertEquals(1, intersectPositions.length);
        assertEquals(1, intersectPositions[0].rayParameter);
    }

    @Test
    void intersectionsWhenRayCrossingFromBelow() {
        Plane plane = new Plane();
        IRay ray = new Ray(new Point(0, -1, 0), new Vector(0, 1, 0));

        Intersection[] intersectPositions = plane.getLocalIntersections(ray, 0, Double.POSITIVE_INFINITY);

        assertEquals(1, intersectPositions.length);
        assertEquals(1, intersectPositions[0].rayParameter);
    }
}