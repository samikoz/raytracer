package io.raytracer.worldly;


import io.raytracer.drawing.ICamera;
import io.raytracer.drawing.Camera;
import io.raytracer.drawing.IPicture;
import io.raytracer.drawing.IColour;
import io.raytracer.drawing.Colour;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Vector;
import io.raytracer.worldly.drawables.Sphere;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WorldTest {
    static Sphere firstSphere;
    static Sphere secondSphere;
    static World defaultWorld;

    @BeforeAll
    static void setupMaterialAndPosition() {
        Material defaultMaterial = Material.builder()
                .colour(new Colour(0.8, 1.0, 0.6)).diffuse(0.7).specular(0.2)
                .ambient(0.1).shininess(200).build();
        firstSphere = new Sphere(defaultMaterial);
        secondSphere = new Sphere(defaultMaterial);
        secondSphere.setTransform(ThreeTransform.scaling(0.5, 0.5, 0.5));

        defaultWorld = new World();
        defaultWorld.put(new LightSource(new Colour(1, 1, 1), new Point(-10, 10, -10)));
        defaultWorld.put(firstSphere).put(secondSphere);
    }

    @Test
    void illuminatingEmptyWorld() {
        IRay ray = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));
        World world = new World();
        world.put(new LightSource(new Colour(1, 1, 1), new Point(-10, 10, -10)));
        IColour expectedColour = new Colour(0, 0, 0);

        assertEquals(expectedColour, world.illuminate(ray), "Empty world has no illumination");
    }

    @Test
    void illuminatingWhenRayMisses() {
        IRay ray = new Ray(new Point(0, 0, -5), new Vector(0, 1, 0));
        IColour illuminated = defaultWorld.illuminate(ray);
        IColour expectedColour = new Colour(0, 0, 0);

        assertEquals(expectedColour, illuminated, "A ray that misses should return black");
    }

    @Test
    void illuminatingWhenRayHits() {
        IRay ray = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));
        IColour illuminated = defaultWorld.illuminate(ray);
        IColour expectedColour = new Colour(0.38066, 0.47583, 0.2855);

        assertEquals(expectedColour, illuminated);
    }

    @Test
    void illuminatingWhenIntersectionIsBehindRay() {
        Material material = Material.builder()
                .colour(new Colour(0.8, 1.0, 0.6)).diffuse(0.7).specular(0.2)
                .ambient(1).shininess(200).build();
        Sphere outerObject = new Sphere(material);

        Sphere innerObject = new Sphere(material);
        innerObject.setTransform(ThreeTransform.scaling(0.5, 0.5, 0.5));

        World world = new World();
        world.put(new LightSource(new Colour(1, 1, 1), new Point(-10, 10, -10)));
        world.put(innerObject).put(outerObject);

        IRay ray = new Ray(new Point(0, 0, 0.75), new Vector(0, 0, -1));
        IColour illuminated = world.illuminate(ray);

        assertEquals(innerObject.getMaterial().colour, illuminated,
                "Illuminate should use the hit with the inner sphere");
    }

    @Test
    void illuminatingWhenIntersectionIsInTheShadow() {
        Material material = Material.builder().colour(new Colour(1, 1, 1)).ambient(0.1).diffuse(0.9)
                .specular(0.9).shininess(200).build();
        Sphere firstSphere = new Sphere(material);
        Sphere secondSphere = new Sphere(material);
        secondSphere.setTransform(ThreeTransform.translation(0, 0, 10));
        ILightSource light = new LightSource(new Colour(1, 1, 1), new Point(0, 0, -10));
        World world = new World();
        world.put(firstSphere).put(secondSphere).put(light);

        IRay ray = new Ray(new Point(0, 0, 5), new Vector(0, 0, 1));
        IColour expectedColour = new Colour(0.1, 0.1, 0.1);
        IColour actualColour = world.illuminate(ray);

        assertEquals(expectedColour, actualColour, "Illuminate shaded point should return only ambient value");
    }

    @Test
    void renderDefaultWorld() {
        IPoint eyePosition = new Point(0, 0, -5);
        IVector lookDirection = new Vector(0, 0, 1);
        IVector upDirection = new Vector(0, 1, 0);
        ICamera camera = new Camera(11, 11, Math.PI / 2, eyePosition, lookDirection, upDirection);

        IPicture picture = defaultWorld.render(camera);

        assertEquals(new Colour(0.38066, 0.47583, 0.2855), picture.read(5, 5),
                "Pixel in the middle of the canvas has correct colour.");
    }

    @Test
    void noShadowWhenNothingBetweenPointAndLight() {
        IPoint point = new Point(0, 10, 0);

        assertFalse(defaultWorld.isShadowed(point));
    }

    @Test
    void shadowWhenSphereBetweenPointAndLight() {
        IPoint point = new Point(10, -10, 10);

        assertTrue(defaultWorld.isShadowed(point));
    }

    @Test
    void shadowWhenLightBetweenPointAndSphere() {
        IPoint point = new Point(-20, 20, -20);

        assertFalse(defaultWorld.isShadowed(point));
    }

    @Test
    void shadowWhenPointBetweenTheLightAndSphere() {
        IPoint point = new Point(-2, 2, -2);

        assertFalse(defaultWorld.isShadowed(point));
    }
}
