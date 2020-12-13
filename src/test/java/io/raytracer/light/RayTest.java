package io.raytracer.light;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.PointImpl;
import io.raytracer.geometry.VectorImpl;
import io.raytracer.geometry.helpers.TupleComparator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RayTest {
    @Test
    void rayPosition() {
        Ray ray = new RayImpl(new PointImpl(2, 3, 4), new VectorImpl(1, 0, 0));
        Point expectedPosition = new PointImpl(3, 3, 4);
        Point actualPosition = ray.position(1);

        TupleComparator.assertTuplesEqual(expectedPosition, actualPosition,
                "Should correctly compute position of a ray.");
    }

    @Test
    void sphereDoubleIntersectionTimes() {
        Ray ray = new RayImpl(new PointImpl(0, 0, -5), new VectorImpl(0, 0, 1));
        Sphere sphere = new Sphere();
        IntersectionList intersections = ray.intersect(sphere);

        assertEquals(2, intersections.count(), "Should have two intersections.");
        assertEquals(4.0, intersections.get(0).time, "The first intersection should be at 4.0.");
    }

    @Test
    void sphereDoubleIntersectionObjects() {
        Ray ray = new RayImpl(new PointImpl(0, 0, -5), new VectorImpl(0, 0, 1));
        Sphere sphere = new Sphere();
        IntersectionList intersections = ray.intersect(sphere);

        assertEquals(sphere, intersections.get(0).object, "The first intersection should be with the sphere.");
    }

    @Test
    void sphereTangentIntersections() {
        Ray ray = new RayImpl(new PointImpl(0, 1, -5), new VectorImpl(0, 0, 1));
        Sphere sphere = new Sphere();
        IntersectionList intersections = ray.intersect(sphere);

        assertEquals(2, intersections.count(), "Should have two (tangent) intersections.");
        assertEquals(5.0, intersections.get(1).time, "The second intersection should be at 5.0.");
    }

    @Test
    void sphereNoIntersections() {
        Ray ray = new RayImpl(new PointImpl(0, 2, -5), new VectorImpl(0, 0, 1));
        Sphere sphere = new Sphere();
        IntersectionList intersections = ray.intersect(sphere);

        assertEquals(0, intersections.count(), "Shouldn't have any intersections.");
    }

    @Test
    void sphereIntersectionsFromInside() {
        Ray ray = new RayImpl(new PointImpl(0, 0, 0), new VectorImpl(0, 0, 1));
        Sphere sphere = new Sphere();
        IntersectionList intersections = ray.intersect(sphere);

        assertEquals(2, intersections.count(), "Should have two intersections.");
        assertEquals(-1, intersections.get(0).time, "The first intersection should be at -1.0.");
    }

    @Test
    void sphereIntersectionsWithSphereBehind() {
        Ray ray = new RayImpl(new PointImpl(0, 0, 5), new VectorImpl(0, 0, 1));
        Sphere sphere = new Sphere();
        IntersectionList intersections = ray.intersect(sphere);

        assertEquals(2, intersections.count(), "Should have two intersections.");
        assertEquals(-4.0, intersections.get(1).time, "The second intersection should be at -4.0.");
    }
}
