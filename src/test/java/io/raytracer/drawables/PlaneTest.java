package io.raytracer.drawables;

import io.raytracer.drawables.Plane;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Ray;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaneTest {
    @Test
    void normalToPlaneIsConstant() {
        Plane plane = new Plane();

        assertEquals(new Vector(0, 1, 0), plane.normalLocally(new Point(0, 0, 0)));
        assertEquals(new Vector(0, 1, 0), plane.normalLocally(new Point(10, 0, -150)));
    }

    @Test
    void noIntersectionsForParallelRay() {
        Plane plane = new Plane();
        IRay parallelRay = new Ray(new Point(0, 10, 0), new Vector(0, 0, 1));

        double[] intersectPositions = plane.getLocalIntersectionPositions(parallelRay);

        assertEquals(0, intersectPositions.length);
    }

    @Test
    void noIntersectionsForCoplanarRay() {
        Plane plane = new Plane();
        IRay coplanarRay = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));

        double[] intersectPositions = plane.getLocalIntersectionPositions(coplanarRay);

        assertEquals(0, intersectPositions.length);
    }

    @Test
    void intersectionsWhenRayCrossingFromAbove() {
        Plane plane = new Plane();
        IRay ray = new Ray(new Point(0, 1, 0), new Vector(0, -1, 0));

        double[] intersectPositions = plane.getLocalIntersectionPositions(ray);

        assertEquals(1, intersectPositions.length);
        assertEquals(1, intersectPositions[0]);
    }

    @Test
    void intersectionsWhenRayCrossingFromBelow() {
        Plane plane = new Plane();
        IRay ray = new Ray(new Point(0, -1, 0), new Vector(0, 1, 0));

        double[] intersectPositions = plane.getLocalIntersectionPositions(ray);

        assertEquals(1, intersectPositions.length);
        assertEquals(1, intersectPositions[0]);
    }
}