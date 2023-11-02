package io.raytracer.shapes;

import io.raytracer.geometry.Interval;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.mechanics.BBox;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.Ray;
import io.raytracer.mechanics.TextureParameters;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TriangleTest {
    @Test
    void correctTriangleProperties() {
        Point v1 = new Point(0, 1, 0);
        Point v2 = new Point(-1, 0, 0);
        Point v3 = new Point(1, 0, 0);
        Triangle t = new Triangle(v1, v2, v3);

        assertEquals(v1, t.v1);
        assertEquals(v2, t.v2);
        assertEquals(v3, t.v3);
        assertEquals(new Vector(-1, -1, 0), t.e1);
        assertEquals(new Vector(1, -1, 0), t.e2);
        assertEquals(new Vector(0, 0, -1), t.normal);
    }

    @Test
    void normalLocallyAlwaysReturnsTriangleNormal() {
        Triangle t = new Triangle(
            new Point(0, 1, 0),
            new Point(-1, 0, 0),
            new Point(1, 0, 0)
        );

        assertEquals(t.normal, t.localNormalAt(new Point(0, 0.5, 0),new TextureParameters()));
        assertEquals(t.normal, t.localNormalAt(new Point(-0.5, 0.75, 0),new TextureParameters() ));
        assertEquals(t.normal, t.localNormalAt(new Point(0.5, 0.25, 0), new TextureParameters()));
    }

    @Test
    void intersectingRayParallelToTriangle() {
        Triangle t = new Triangle(
            new Point(0, 1, 0),
            new Point(-1, 0, 0),
            new Point(1, 0, 0)
        );
        Ray ray = new Ray(new Point(0, -1, -2), new Vector(0, 1, 0));

        assertEquals(0, t.getLocalIntersections(ray, Interval.positiveReals()).length);
    }

    @Test
    void intersectingRayBeyondP1P3Edge() {
        Triangle t = new Triangle(
            new Point(0, 1, 0),
            new Point(-1, 0, 0),
            new Point(1, 0, 0)
        );
        Ray ray = new Ray(new Point(1, 1, -2), new Vector(0, 0, 1));
        Intersection[] intersections = t.getLocalIntersections(ray, Interval.positiveReals());

        assertEquals(0, intersections.length);
    }

    @Test
    void intersectingRayBeyondP1P2Edge() {
        Triangle t = new Triangle(
            new Point(0, 1, 0),
            new Point(-1, 0, 0),
            new Point(1, 0, 0)
        );
        Ray ray = new Ray(new Point(-1, 1, -2), new Vector(0, 0, 1));
        Intersection[] intersections = t.getLocalIntersections(ray, Interval.positiveReals());

        assertEquals(0, intersections.length);
    }

    @Test
    void intersectingRayBeyondP2P3Edge() {
        Triangle t = new Triangle(
            new Point(0, 1, 0),
            new Point(-1, 0, 0),
            new Point(1, 0, 0)
        );
        Ray ray = new Ray(new Point(0, -1, -2), new Vector(0, 0, 1));
        Intersection[] intersections = t.getLocalIntersections(ray, Interval.positiveReals());

        assertEquals(0, intersections.length);
    }

    @Test
    void rayHitsTriangle() {
        Triangle t = new Triangle(
            new Point(0, 1, 0),
            new Point(-1, 0, 0),
            new Point(1, 0, 0)
        );
        Ray ray = new Ray(new Point(0, 0.5, -2), new Vector(0, 0, 1));
        Intersection[] intersections = t.getLocalIntersections(ray, Interval.positiveReals());

        assertEquals(1, intersections.length);
        assertEquals(2, intersections[0].rayParameter);
    }

    @Test
    void triangleBBox() {
        Triangle t = new Triangle(
            new Point(-1, 2, 3),
            new Point(1, -1, 0),
            new Point(2, 2, 2)
        );
        BBox bbox = t.getBoundingBox();

        assertEquals(new Interval(-1, 2), bbox.x);
        assertEquals(new Interval(-1, 2), bbox.y);
        assertEquals(new Interval(0, 3), bbox.z);
    }
}