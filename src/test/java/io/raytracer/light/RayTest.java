package io.raytracer.light;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.PointImpl;
import io.raytracer.geometry.VectorImpl;
import io.raytracer.geometry.helpers.TupleComparator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

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
    void sphereDoubleIntersection() {
        Ray ray = new RayImpl(new PointImpl(0, 0, -5), new VectorImpl(0, 0, 1));
        Sphere sphere = new Sphere();
        double[] expectedIntersections = new double[] {4.0, 6.0};
        double[] actualIntersections = ray.intersect(sphere);

        assertArrayEquals(expectedIntersections, actualIntersections,
                "Should correctly compute ray intersections with the unit sphere.");
    }

    @Test
    void sphereTangentIntersections() {
        Ray ray = new RayImpl(new PointImpl(0, 1, -5), new VectorImpl(0, 0, 1));
        Sphere sphere = new Sphere();
        double[] expectedIntersections = new double[] {5.0, 5.0};
        double[] actualIntersections = ray.intersect(sphere);

        assertArrayEquals(expectedIntersections, actualIntersections,
                "Should correctly compute tangent ray intersections.");
    }

    @Test
    void sphereNoIntersections() {
        Ray ray = new RayImpl(new PointImpl(0, 2, -5), new VectorImpl(0, 0, 1));
        Sphere sphere = new Sphere();
        double[] expectedIntersections = new double[] {};
        double[] actualIntersections = ray.intersect(sphere);

        assertArrayEquals(expectedIntersections, actualIntersections,
                "Should correctly compute no intersections.");
    }

    @Test
    void sphereIntersectionsFromInside() {
        Ray ray = new RayImpl(new PointImpl(0, 0, 0), new VectorImpl(0, 0, 1));
        Sphere sphere = new Sphere();
        double[] expectedIntersections = new double[] {-1.0, 1.0};
        double[] actualIntersections = ray.intersect(sphere);

        assertArrayEquals(expectedIntersections, actualIntersections,
                "Should correctly compute intersections with ray inside the sphere.");
    }

    @Test
    void sphereIntersectionsWithSphereBehind() {
        Ray ray = new RayImpl(new PointImpl(0, 0, 5), new VectorImpl(0, 0, 1));
        Sphere sphere = new Sphere();
        double[] expectedIntersections = new double[] {-6.0, -4.0};
        double[] actualIntersections = ray.intersect(sphere);

        assertArrayEquals(expectedIntersections, actualIntersections,
                "Should correctly compute intersections with sphere behind the ray.");
    }
}
