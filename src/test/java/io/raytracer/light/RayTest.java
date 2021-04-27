package io.raytracer.light;

import io.raytracer.drawing.Sphere;
import io.raytracer.drawing.SphereImpl;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.PointImpl;
import io.raytracer.geometry.ThreeTransformation;
import io.raytracer.geometry.Vector;
import io.raytracer.geometry.VectorImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RayTest {
    @Test
    void rayPosition() {
        Ray ray = new RayImpl(new PointImpl(2, 3, 4), new VectorImpl(1, 0, 0));
        Point expectedPosition = new PointImpl(3, 3, 4);
        Point actualPosition = ray.position(1);

        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    void gettingIlluminatedPoint() {
        RayImpl ray = new RayImpl(new PointImpl(0, 0, -5), new VectorImpl(0, 0, 1));
        Sphere sphere = new SphereImpl();
        Intersection intersection = new Intersection(4, sphere);
        IlluminatedPoint illuminated = ray.getIlluminatedPoint(intersection);

        assertEquals(sphere, illuminated.object);
        assertEquals(new PointImpl(0, 0, -1), illuminated.point);
        assertEquals(new VectorImpl(0, 0, -1), illuminated.eyeVector);
        assertEquals(new VectorImpl(0, 0, -1), illuminated.normalVector);
    }

    @Test
    void pointIlluminatedFromInside() {
        RayImpl ray = new RayImpl(new PointImpl(0, 0, 0), new VectorImpl(0, 0, 1));
        Sphere sphere = new SphereImpl();
        Intersection intersection = new Intersection(1, sphere);
        IlluminatedPoint illuminated = ray.getIlluminatedPoint(intersection);

        assertEquals(new PointImpl(0, 0, 1), illuminated.point);
        assertEquals(new VectorImpl(0, 0, -1), illuminated.eyeVector);
        assertEquals(new VectorImpl(0, 0, -1), illuminated.normalVector,
                "Normal vector should be inverted");
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
