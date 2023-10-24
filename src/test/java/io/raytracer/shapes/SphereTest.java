package io.raytracer.shapes;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Vector;
import io.raytracer.mechanics.BBox;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.Ray;
import io.raytracer.mechanics.TextureParameters;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SphereTest {
    @Test
    void normalOnASphereOnXAxis() {
        Sphere sphere = new Sphere();
        IVector expectedNormal = new Vector(1, 0, 0);
        Intersection testIntersection = new Intersection(
            sphere,
            new Ray(new Point(1, 0, 0), new Vector(0, 0, 1)),
            0, new TextureParameters()
        );
        IVector actualNormal = sphere.normal(testIntersection);

        assertEquals(expectedNormal, actualNormal, "Normal to a sphere is a vector representing the point.");
    }

    @Test
    void normalOnASphereAtGenericPoint() {
        Sphere sphere = new Sphere();
        IVector expectedNormal = new Vector(Math.sqrt(3) / 3, Math.sqrt(3) / 3, Math.sqrt(3) / 3);
        Intersection testIntersection = new Intersection(
            sphere,
            new Ray(new Point(Math.sqrt(3) / 3, Math.sqrt(3) / 3, Math.sqrt(3) / 3), new Vector(0, 0, 1)),
            0, new TextureParameters()
        );
        IVector actualNormal = sphere.normal(testIntersection);

        assertEquals(expectedNormal, actualNormal, "Normal to a sphere is a vector representing the point.");
    }

    @Test
    void normalIsNormalised() {
        Sphere sphere = new Sphere();
        Intersection testIntersection = new Intersection(
            sphere,
            new Ray(new Point(Math.sqrt(3) / 3, Math.sqrt(3) / 3, Math.sqrt(3) / 3), new Vector(0, 0, 1)),
            0, new TextureParameters()
        );
        IVector normal = sphere.normal(testIntersection);

        assertEquals(normal, normal.normalise(), "Normal vector should be normalised.");
    }

    @Test
    void normalOnATranslatedSphere() {
        Sphere sphere = new Sphere();
        sphere.setTransform(ThreeTransform.translation(0, 1, 0));
        Intersection testIntersection = new Intersection(
            sphere,
            new Ray(new Point(0, 1.70711, -0.70711), new Vector(0, 0, 1)),
            0, new TextureParameters()
        );
        IVector actualNormal = sphere.normal(testIntersection);
        IVector expectedNormal = new Vector(0, 0.70711, -0.70711);

        assertEquals(expectedNormal, actualNormal);
    }

    @Test
    void normalOnATransformedSphere() {
        Sphere sphere = new Sphere();
        ITransform trans = ThreeTransform.rotation_z(Math.PI / 5).scale(1, 0.5, 1);
        sphere.setTransform(trans);
        Intersection testIntersection = new Intersection(
            sphere,
            new Ray(new Point(0, Math.sqrt(2) / 2, -Math.sqrt(2) / 2), new Vector(0, 0, 1)),
            0, new TextureParameters()
        );
        IVector actualNormal = sphere.normal(testIntersection);
        IVector expectedNormal = new Vector(0, 0.97014, -0.24254);

        assertEquals(expectedNormal, actualNormal);
    }

    @Test
    void intersectCorrectParameters() {
        IRay ray = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));
        Sphere sphere = new Sphere();
        Intersection[] intersections = sphere.getIntersections(ray, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).toArray(new Intersection[] {});

        assertEquals(2, intersections.length, "Should have two intersections.");
        assertEquals(4.0, intersections[0].rayParameter, "The first intersection should be at 4.0.");
    }

    @Test
    void intersectionCorrectObject() {
        IRay ray = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));
        Sphere sphere = new Sphere();
        Intersection[] intersections = sphere.getIntersections(ray, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).toArray(new Intersection[] {});

        assertEquals(sphere, intersections[0].object, "The first intersection should be with the sphere.");
    }

    @Test
    void intersectWhenRayTangent() {
        IRay ray = new Ray(new Point(0, 1, -5), new Vector(0, 0, 1));
        Sphere sphere = new Sphere();
        Intersection[] intersections = sphere.getIntersections(ray, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).toArray(new Intersection[] {});

        assertEquals(2, intersections.length, "Should have two (tangent) intersections.");
        assertEquals(5.0, intersections[1].rayParameter, "The second intersection should be at 5.0.");
    }

    @Test
    void intersectWhenRayMisses() {
        IRay ray = new Ray(new Point(0, 2, -5), new Vector(0, 0, 1));
        Sphere sphere = new Sphere();
        Intersection[] intersections = sphere.getIntersections(ray, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).toArray(new Intersection[] {});

        assertEquals(0, intersections.length, "Shouldn't have any intersections.");
    }

    @Test
    void intersectRayInside() {
        IRay ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        Sphere sphere = new Sphere();
        Intersection[] intersections = sphere.getIntersections(ray, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).toArray(new Intersection[] {});

        assertEquals(2, intersections.length, "Should have two intersections.");
        assertEquals(-1, intersections[0].rayParameter, "The first intersection should be at -1.0.");
    }

    @Test
    void intersectionBehindRay() {
        IRay ray = new Ray(new Point(0, 0, 5), new Vector(0, 0, 1));
        Sphere sphere = new Sphere();
        Intersection[] intersections = sphere.getIntersections(ray, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).toArray(new Intersection[] {});

        assertEquals(2, intersections.length, "Should have two intersections.");
        assertEquals(-4.0, intersections[1].rayParameter, "The second intersection should be at -4.0.");
    }

    @Test
    void intersectAfterScaling() {
        IRay ray = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));
        Sphere sphere = new Sphere();
        sphere.setTransform(ThreeTransform.scaling(2, 2, 2));
        Intersection[] intersections = sphere.getIntersections(ray, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).toArray(new Intersection[] {});

        assertEquals(2, intersections.length, "Should have two intersections.");
        assertEquals(7, intersections[1].rayParameter, "The second intersection should be at 7.");
    }

    @Test
    void intersectAfterTranslation() {
        IRay ray = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));
        Sphere sphere = new Sphere();
        sphere.setTransform(ThreeTransform.translation(5, 0, 0));
        Intersection[] intersections = sphere.getIntersections(ray, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).toArray(new Intersection[] {});

        assertEquals(0, intersections.length, "Should have no intersections.");
    }

    @Test
    void boundingBox() {
        Sphere sphere = new Sphere();
        sphere.setTransform(ThreeTransform.scaling(2, 2, 2).translate(0, 2, 0));
        BBox box = sphere.getBoundingBox().get();

        assertEquals(-2, box.x.min, 1e-6);
        assertEquals(2, box.x.max, 1e-6);
        assertEquals(4, box.y.max, 1e-6);
        assertEquals(0, box.y.min, 1e-6);
        assertEquals(-2, box.z.min, 1e-6);
        assertEquals(2, box.z.max, 1e-6);
    }
}
