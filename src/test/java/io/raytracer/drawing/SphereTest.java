package io.raytracer.drawing;

import io.raytracer.geometry.PointImpl;
import io.raytracer.geometry.ThreeTransformation;
import io.raytracer.geometry.Transformation;
import io.raytracer.geometry.Vector;
import io.raytracer.geometry.VectorImpl;
import io.raytracer.light.IntersectionList;
import io.raytracer.light.Ray;
import io.raytracer.light.RayImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SphereTest {
    @Test
    void getDefaultTransform() {
        Sphere sphere = new SphereImpl();

        assertEquals(new ThreeTransformation(), sphere.getTransform(),
                "Default transformation should be the identity.");
    }

    @Test
    void normalOnASphereOnXAxis() {
        Sphere sphere = new SphereImpl();
        Vector expectedNormal = new VectorImpl(1, 0, 0);
        Vector actualNormal = sphere.normal(new PointImpl(1, 0, 0));

        assertEquals(expectedNormal, actualNormal, "Normal to a sphere is a vector representing the point.");
    }

    @Test
    void normalOnASphereAtGenericPoint() {
        Sphere sphere = new SphereImpl();
        Vector expectedNormal = new VectorImpl(Math.sqrt(3) / 3, Math.sqrt(3) / 3, Math.sqrt(3) / 3);
        Vector actualNormal = sphere.normal(
                new PointImpl(Math.sqrt(3) / 3, Math.sqrt(3) / 3, Math.sqrt(3) / 3));

        assertEquals(expectedNormal, actualNormal, "Normal to a sphere is a vector representing the point.");
    }

    @Test
    void normalIsNormalised() {
        Sphere sphere = new SphereImpl();
        Vector normal = sphere.normal(new PointImpl(Math.sqrt(3) / 3, Math.sqrt(3) / 3, Math.sqrt(3) / 3));

        assertEquals(normal, normal.normalise(), "Normal vector should be normalised.");
    }

    @Test
    void normalOnATranslatedSphere() {
        Sphere sphere = new SphereImpl();
        sphere.setTransform(ThreeTransformation.translation(0, 1, 0));
        Vector actualNormal = sphere.normal(new PointImpl(0, 1.70711, -0.70711));
        Vector expectedNormal = new VectorImpl(0, 0.70711, -0.70711);

        assertEquals(expectedNormal, actualNormal);
    }

    @Test
    void normalOnATransformedSphere() {
        Sphere sphere = new SphereImpl();
        Transformation trans = ThreeTransformation.rotation_z(Math.PI / 5).scale(1, 0.5, 1);
        sphere.setTransform(trans);
        Vector actualNormal = sphere.normal(new PointImpl(0, Math.sqrt(2) / 2, -Math.sqrt(2) / 2));
        Vector expectedNormal = new VectorImpl(0, 0.97014, -0.24254);

        assertEquals(expectedNormal, actualNormal);
    }

    @Test
    void intersectCorrectTimes() {
        Ray ray = new RayImpl(new PointImpl(0, 0, -5), new VectorImpl(0, 0, 1));
        Sphere sphere = new SphereImpl();
        IntersectionList intersections = sphere.intersect(ray);

        assertEquals(2, intersections.count(), "Should have two intersections.");
        assertEquals(4.0, intersections.get(0).time, "The first intersection should be at 4.0.");
    }

    @Test
    void intersectionCorrectObject() {
        Ray ray = new RayImpl(new PointImpl(0, 0, -5), new VectorImpl(0, 0, 1));
        Sphere sphere = new SphereImpl();
        IntersectionList intersections = sphere.intersect(ray);

        assertEquals(sphere, intersections.get(0).object, "The first intersection should be with the sphere.");
    }

    @Test
    void intersectWhenRayTangent() {
        Ray ray = new RayImpl(new PointImpl(0, 1, -5), new VectorImpl(0, 0, 1));
        Sphere sphere = new SphereImpl();
        IntersectionList intersections = sphere.intersect(ray);

        assertEquals(2, intersections.count(), "Should have two (tangent) intersections.");
        assertEquals(5.0, intersections.get(1).time, "The second intersection should be at 5.0.");
    }

    @Test
    void intersectWhenRayMisses() {
        Ray ray = new RayImpl(new PointImpl(0, 2, -5), new VectorImpl(0, 0, 1));
        Sphere sphere = new SphereImpl();
        IntersectionList intersections = sphere.intersect(ray);

        assertEquals(0, intersections.count(), "Shouldn't have any intersections.");
    }

    @Test
    void intersectRayInside() {
        Ray ray = new RayImpl(new PointImpl(0, 0, 0), new VectorImpl(0, 0, 1));
        Sphere sphere = new SphereImpl();
        IntersectionList intersections = sphere.intersect(ray);

        assertEquals(2, intersections.count(), "Should have two intersections.");
        assertEquals(-1, intersections.get(0).time, "The first intersection should be at -1.0.");
    }

    @Test
    void intersectionBehindRay() {
        Ray ray = new RayImpl(new PointImpl(0, 0, 5), new VectorImpl(0, 0, 1));
        Sphere sphere = new SphereImpl();
        IntersectionList intersections = sphere.intersect(ray);

        assertEquals(2, intersections.count(), "Should have two intersections.");
        assertEquals(-4.0, intersections.get(1).time, "The second intersection should be at -4.0.");
    }

    @Test
    void intersectAfterScaling() {
        Ray ray = new RayImpl(new PointImpl(0, 0, -5), new VectorImpl(0, 0, 1));
        Sphere sphere = new SphereImpl();
        sphere.setTransform(ThreeTransformation.scaling(2, 2, 2));
        IntersectionList intersections = sphere.intersect(ray);

        assertEquals(2, intersections.count(), "Should have two intersections.");
        assertEquals(7, intersections.get(1).time, "The second intersection should be at 7.");
    }

    @Test
    void intersectAfterTranslation() {
        Ray ray = new RayImpl(new PointImpl(0, 0, -5), new VectorImpl(0, 0, 1));
        Sphere sphere = new SphereImpl();
        sphere.setTransform(ThreeTransformation.translation(5, 0, 0));
        IntersectionList intersections = sphere.intersect(ray);

        assertEquals(0, intersections.count(), "Should have no intersections.");
    }

}
