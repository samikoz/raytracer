package io.raytracer.light;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.PointImpl;
import io.raytracer.geometry.ThreeTransformation;
import io.raytracer.geometry.Vector;
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
        Sphere sphere = new SphereImpl();
        IntersectionList intersections = ray.intersect(sphere);

        assertEquals(2, intersections.count(), "Should have two intersections.");
        assertEquals(4.0, intersections.get(0).time, "The first intersection should be at 4.0.");
    }

    @Test
    void sphereDoubleIntersectionObjects() {
        Ray ray = new RayImpl(new PointImpl(0, 0, -5), new VectorImpl(0, 0, 1));
        Sphere sphere = new SphereImpl();
        IntersectionList intersections = ray.intersect(sphere);

        assertEquals(sphere, intersections.get(0).object, "The first intersection should be with the sphere.");
    }

    @Test
    void sphereTangentIntersections() {
        Ray ray = new RayImpl(new PointImpl(0, 1, -5), new VectorImpl(0, 0, 1));
        Sphere sphere = new SphereImpl();
        IntersectionList intersections = ray.intersect(sphere);

        assertEquals(2, intersections.count(), "Should have two (tangent) intersections.");
        assertEquals(5.0, intersections.get(1).time, "The second intersection should be at 5.0.");
    }

    @Test
    void sphereNoIntersections() {
        Ray ray = new RayImpl(new PointImpl(0, 2, -5), new VectorImpl(0, 0, 1));
        Sphere sphere = new SphereImpl();
        IntersectionList intersections = ray.intersect(sphere);

        assertEquals(0, intersections.count(), "Shouldn't have any intersections.");
    }

    @Test
    void sphereIntersectionsFromInside() {
        Ray ray = new RayImpl(new PointImpl(0, 0, 0), new VectorImpl(0, 0, 1));
        Sphere sphere = new SphereImpl();
        IntersectionList intersections = ray.intersect(sphere);

        assertEquals(2, intersections.count(), "Should have two intersections.");
        assertEquals(-1, intersections.get(0).time, "The first intersection should be at -1.0.");
    }

    @Test
    void sphereIntersectionsWithSphereBehind() {
        Ray ray = new RayImpl(new PointImpl(0, 0, 5), new VectorImpl(0, 0, 1));
        Sphere sphere = new SphereImpl();
        IntersectionList intersections = ray.intersect(sphere);

        assertEquals(2, intersections.count(), "Should have two intersections.");
        assertEquals(-4.0, intersections.get(1).time, "The second intersection should be at -4.0.");
    }

    @Test
    void translatingARay() {
        Vector direction = new VectorImpl(0, 1, 0);
        Ray ray = new RayImpl(new PointImpl(1, 2, 3), direction);
        Ray translatedRay = ray.transform(ThreeTransformation.translation(3, 4, 5));
        Point expectedOrigin = new PointImpl(4, 6, 8);

        assertEquals(expectedOrigin, translatedRay.getOrigin(), "Translation should translate ray's origin.");
        assertEquals(direction, translatedRay.getDirection(),
                "Translation should not affect ray's direction.");
    }

    @Test
    void scalingARay() {
        Ray ray = new RayImpl(new PointImpl(1, 2, 3), new VectorImpl(0, 1, 0));
        Ray scaledRay = ray.transform(ThreeTransformation.scaling(2, 3, 4));
        Point expectedOrigin = new PointImpl(2, 6, 12);
        Vector expectedDirection = new VectorImpl(0, 3, 0);

        assertEquals(expectedOrigin, scaledRay.getOrigin(), "Scaling should scale ray's origin.");
        assertEquals(expectedDirection, scaledRay.getDirection(), "Scaling should scale ray's direction.");
    }
}
