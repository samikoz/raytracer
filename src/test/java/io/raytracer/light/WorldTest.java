package io.raytracer.light;


import io.raytracer.drawing.Camera;
import io.raytracer.drawing.CameraImpl;
import io.raytracer.drawing.Canvas;
import io.raytracer.drawing.Colour;
import io.raytracer.drawing.ColourImpl;
import io.raytracer.drawing.Drawable;
import io.raytracer.drawing.Material;
import io.raytracer.drawing.Sphere;
import io.raytracer.drawing.SphereImpl;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.PointImpl;
import io.raytracer.geometry.SquareMatrixImpl;
import io.raytracer.geometry.ThreeTransformation;
import io.raytracer.geometry.Transformation;
import io.raytracer.geometry.Vector;
import io.raytracer.geometry.VectorImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WorldTest {
    static Sphere firstSphere;
    static Sphere secondSphere;
    static World defaultWorld;

    @BeforeAll
    static void setupMaterialAndPosition() {
        Material defaultMaterial = Material.builder()
                .colour(new ColourImpl(0.8, 1.0, 0.6)).diffuse(0.7).specular(0.2)
                .ambient(0.1).shininess(200).build();
        firstSphere = new SphereImpl(defaultMaterial);
        secondSphere = new SphereImpl(defaultMaterial);
        secondSphere.setTransform(ThreeTransformation.scaling(0.5, 0.5, 0.5));

        defaultWorld = new WorldImpl(
                new LightSourceImpl(new ColourImpl(1, 1, 1), new PointImpl(-10, 10, -10)));
        defaultWorld.put(firstSphere).put(secondSphere);
    }

    @Test
    void containsObjectsThatWerePut() {
        assertTrue(defaultWorld.contains(firstSphere) && defaultWorld.contains(secondSphere));
    }

    @Test
    void intersectingEmptyWorld() {
        Ray ray = new RayImpl(new PointImpl(0, 0, -5), new VectorImpl(0, 0, 1));
        World world = new WorldImpl(
                new LightSourceImpl(new ColourImpl(1, 1, 1), new PointImpl(-10, 10, -10)));

        assertEquals(0, world.intersect(ray).count(), "Empty world should have no intersections.");
    }

    @Test
    void intersecting() {
        Ray ray = new RayImpl(new PointImpl(0, 0, -5), new VectorImpl(0, 0, 1));
        IntersectionList intersections = defaultWorld.intersect(ray);

        assertEquals(intersections.get(0).time, 4);
        assertEquals(intersections.get(1).time, 4.5);
        assertEquals(intersections.get(2).time, 5.5);
        assertEquals(intersections.get(3).time, 6);
    }

    @Test
    void illuminatingWhenRayMisses() {
        Ray ray = new RayImpl(new PointImpl(0, 0, -5), new VectorImpl(0, 1, 0));
        Colour illuminated = defaultWorld.illuminate(ray);
        Colour expectedColour = new ColourImpl(0, 0, 0);

        assertEquals(expectedColour, illuminated, "A ray that misses should return black");
    }

    @Test
    void illuminatingWhenRayHits() {
        Ray ray = new RayImpl(new PointImpl(0, 0, -5), new VectorImpl(0, 0, 1));
        Colour illuminated = defaultWorld.illuminate(ray);
        Colour expectedColour = new ColourImpl(0.38066, 0.47583, 0.2855);

        assertEquals(expectedColour, illuminated);
    }

    @Test
    void illuminatingWhenIntersectionIsBehindRay() {
        Material material = Material.builder()
                .colour(new ColourImpl(0.8, 1.0, 0.6)).diffuse(0.7).specular(0.2)
                .ambient(1).shininess(200).build();
        Sphere outerObject = new SphereImpl(material);

        Sphere innerObject = new SphereImpl(material);
        innerObject.setTransform(ThreeTransformation.scaling(0.5, 0.5, 0.5));

        World world = new WorldImpl(
                new LightSourceImpl(new ColourImpl(1, 1, 1), new PointImpl(-10, 10, -10)));
        world.put(innerObject).put(outerObject);

        Ray ray = new RayImpl(new PointImpl(0, 0, 0.75), new VectorImpl(0, 0, -1));
        Colour illuminated = world.illuminate(ray);

        assertEquals(innerObject.getMaterial().colour, illuminated,
                "Illuminate should use the hit with the inner sphere");
    }

    @Test
    void getDefaultOrientationViewTransformation() {
        Point eyePosition = new PointImpl(0, 0, 0);
        Point lookPosition = new PointImpl(0, 0, -1);
        Vector upDirection = new VectorImpl(0, 1, 0);

        Transformation viewTransformation = defaultWorld.getViewTransformation(eyePosition, lookPosition, upDirection);
        Transformation identity = new ThreeTransformation();

        assertEquals(identity, viewTransformation, "Default view transformation is the identity");
    }

    @Test
    void getViewInPositiveZDirection() {
        Point eyePosition = new PointImpl(0, 0, 0);
        Point lookPosition = new PointImpl(0, 0, 1);
        Vector upDirection = new VectorImpl(0, 1, 0);

        Transformation viewTransformation = defaultWorld.getViewTransformation(eyePosition, lookPosition, upDirection);
        Transformation reflectionXZ = ThreeTransformation.scaling(-1, 1, -1);

        assertEquals(reflectionXZ, viewTransformation, "Look in the positive z direction should swap XZ axes");
    }

    @Test
    void getViewTransformationMovedBack() {
        Point eyePosition = new PointImpl(0, 0, 8);
        Point lookPosition = new PointImpl(0, 0, 0);
        Vector upDirection = new VectorImpl(0, 1, 0);

        Transformation viewTransformation = defaultWorld.getViewTransformation(eyePosition, lookPosition, upDirection);
        Transformation moveBackTranslation = ThreeTransformation.translation(0, 0, -8);

        assertEquals(moveBackTranslation, viewTransformation, "Moving the should move the world the other way");
    }

    @Test
    void getGeneralViewTransformation() {
        Point eyePosition = new PointImpl(1, 3, 2);
        Point lookPosition = new PointImpl(4, -2, 8);
        Vector upDirection = new VectorImpl(1, 1, 0);

        Transformation viewTransformation = defaultWorld.getViewTransformation(eyePosition, lookPosition, upDirection);
        Transformation expectedViewTransformation = ThreeTransformation.transformation(new SquareMatrixImpl(
                -0.50709, 0.50709, 0.67612, -2.36643,
                0.76772, 0.60609, 0.12122, -2.82843,
                -0.35857, 0.59761, -0.71714, 0.00000,
                0.00000, 0.00000, 0.00000, 1.00000
        ));

        assertEquals(expectedViewTransformation, viewTransformation);
    }

    @Test
    void renderDefaultWorld() {
        Point eyePosition = new PointImpl(0, 0, -5);
        Point lookPosition = new PointImpl(0, 0, 0);
        Vector upDirection = new VectorImpl(0, 1, 0);
        Transformation transform = defaultWorld.getViewTransformation(eyePosition, lookPosition, upDirection);
        Camera camera = new CameraImpl(11, 11, Math.PI / 2, transform);

        Canvas canvas = defaultWorld.render(camera);

        assertEquals(new ColourImpl(0.38066, 0.47583, 0.2855), canvas.read(5, 5),
                "Pixel in the middle of the canvas has correct colour.");
    }
}
