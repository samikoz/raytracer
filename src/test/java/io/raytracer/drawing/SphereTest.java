package io.raytracer.drawing;

import io.raytracer.drawing.Material;
import io.raytracer.drawing.Sphere;
import io.raytracer.drawing.SphereImpl;
import io.raytracer.geometry.PointImpl;
import io.raytracer.geometry.ThreeTransformation;
import io.raytracer.geometry.Transformation;
import io.raytracer.geometry.Vector;
import io.raytracer.geometry.VectorImpl;
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
    void getDefaultMaterial() {
        Sphere sphere = new SphereImpl();

        assertEquals(new Material(), sphere.getMaterial(),
                "Sphere's default material should be the default material.");
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
}
