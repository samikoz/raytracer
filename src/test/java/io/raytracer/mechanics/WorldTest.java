package io.raytracer.mechanics;


import io.raytracer.tools.ICamera;
import io.raytracer.tools.Camera;
import io.raytracer.tools.IPicture;
import io.raytracer.tools.IColour;
import io.raytracer.tools.Colour;
import io.raytracer.textures.MonocolourTexture;
import io.raytracer.textures.TestTexture;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Vector;
import io.raytracer.shapes.Shape;
import io.raytracer.shapes.Plane;
import io.raytracer.shapes.Sphere;
import io.raytracer.materials.Material;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WorldTest {
    static Material outerMaterial;
    static Material innerMaterial;
    static Sphere outerSphere;
    static Sphere innerSphere;
    static World defaultWorld;

    @BeforeEach
    void setupMaterialAndPosition() {
        outerMaterial = Material.builder()
                .texture(new MonocolourTexture(new Colour(0.8, 1.0, 0.6))).diffuse(0.7).specular(0.2)
                .ambient(0.1).shininess(200.0).build();
        innerMaterial = Material.builder().build();
        outerSphere = new Sphere(outerMaterial);
        innerSphere = new Sphere(innerMaterial);
        innerSphere.setTransform(ThreeTransform.scaling(0.5, 0.5, 0.5));

        defaultWorld = new World();
        defaultWorld.put(new LightSource(new Colour(1, 1, 1), new Point(-10, 10, -10)));
        defaultWorld.put(outerSphere).put(innerSphere);
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
        Material outer = outerMaterial.toBuilder().ambient(1.0).build();
        Sphere outerObject = new Sphere(outer);

        Material inner = innerMaterial.toBuilder().ambient(1.0).build();
        Sphere innerObject = new Sphere(inner);
        innerObject.setTransform(ThreeTransform.scaling(0.5, 0.5, 0.5));

        World world = new World();
        world.put(new LightSource(new Colour(1, 1, 1), new Point(-10, 10, -10)));
        world.put(innerObject).put(outerObject);

        IRay ray = new Ray(new Point(0, 0, 0.75), new Vector(0, 0, -1));
        IColour illuminated = world.illuminate(ray);

        assertEquals(innerObject.getMaterial().texture.colourAt(new Point(0, 0, 0)), illuminated,
                "Illuminate should use the hit with the inner sphere");
    }

    @Test
    void illuminatingWhenIntersectionIsInTheShadow() {
        Material material = Material.builder().texture(new MonocolourTexture(new Colour(1, 1, 1))).ambient(0.1).diffuse(0.9)
                .specular(0.9).shininess(200.0).build();
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
    void illuminatingThroughNonShadowingSurface() {
        Material material = Material.builder().texture(new MonocolourTexture(new Colour(1, 1, 1)))
            .ambient(0.1).diffuse(0.2).specular(0.2).shininess(200.0).build();
        Sphere sphere = new Sphere(material);
        sphere.setTransform(ThreeTransform.translation(0, 0, 10));
        Plane plane = new Plane();
        plane.setTransform(ThreeTransform.rotation_x(Math.PI / 2));
        plane.setCastingShadows(false);
        ILightSource light = new LightSource(new Colour(1, 1, 1), new Point(0, 0, -10));
        World world = new World();
        world.put(sphere).put(plane).put(light);

        IRay ray = new Ray(new Point(0, 0, 5), new Vector(0, 0, 1));
        IColour expectedColour = new Colour(0.5, 0.5, 0.5);
        IColour actualColour = world.illuminate(ray);

        assertEquals(expectedColour, actualColour, "non-shadowing objects should not cast shadows");
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

    @Test
    void getReflectedColourFromNonReflectiveSurface() {
        IRay ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));

        Optional<RayHit> hit = RayHit.fromIntersections(defaultWorld.intersect(ray));
        assertTrue(hit.isPresent());
        RayHit hitpoint = hit.get();
        IColour reflectedColour = defaultWorld.getReflectedColour(hitpoint);
        
        assertEquals(new Colour(0, 0, 0), reflectedColour);
    }

    @Test
    void getReflectedColourFromReflectiveSurface() {
        IRay ray = new Ray(new Point(0, 0, -3), new Vector(0, -Math.sqrt(2)/2, Math.sqrt(2)/2));
        Shape sphere = new Sphere(outerMaterial);
        Material planeMaterial = Material.builder()
                .diffuse(0.9).specular(0.9).ambient(0.1).shininess(200.0).reflectivity(0.5)
                .texture(new MonocolourTexture(new Colour(1, 1, 1))).build();
        Shape plane = new Plane(planeMaterial);
        plane.setTransform(ThreeTransform.translation(0, -1, 0));
        
        World testWorld = new World();
        testWorld.put(new LightSource(new Colour(1, 1, 1), new Point(-10, 10, -10)));
        testWorld.put(sphere).put(plane);

        Optional<RayHit> hit = RayHit.fromIntersections(testWorld.intersect(ray));
        assertTrue(hit.isPresent());
        RayHit hitpoint = hit.get();

        assertEquals(new Colour(0.19032, 0.2379, 0.14274), testWorld.getReflectedColour(hitpoint));
    }

    @Test
    void illuminatingWithReflectiveMaterial() {
        IRay ray = new Ray(new Point(0, 0, -3), new Vector(0, -Math.sqrt(2)/2, Math.sqrt(2)/2));
        Material sphereMaterial = Material.builder()
                .diffuse(0.7).specular(0.2).ambient(0.1).shininess(200.0)
                .texture(new MonocolourTexture(new Colour(0.8, 1.0, 0.6))).build();
        Shape sphere = new Sphere(sphereMaterial);
        Material planeMaterial = Material.builder()
                .diffuse(0.9).specular(0.9).ambient(0.1).shininess(200.0).reflectivity(0.5)
                .texture(new MonocolourTexture(new Colour(1, 1, 1))).build();
        Shape plane = new Plane(planeMaterial);
        plane.setTransform(ThreeTransform.translation(0, -1, 0));

        World testWorld = new World();
        testWorld.put(new LightSource(new Colour(1, 1, 1), new Point(-10, 10, -10)));
        testWorld.put(sphere).put(plane);

        assertEquals(new Colour(0.87677, 0.92436, 0.82918), testWorld.illuminate(ray));
    }

    @Test
    void illuminatingTwoParallelMirrors() {
        Material planeMaterial = Material.builder().reflectivity(1.0).build();
        Shape leftPlane = new Plane(planeMaterial);
        leftPlane.setTransform(ThreeTransform.translation(0, -1, 0));
        Shape rightPlane = new Plane(planeMaterial);
        rightPlane.setTransform(ThreeTransform.translation(0, 1, 0));
        World world = new World();
        world.put(new LightSource(new Colour(1, 1, 1), new Point(0, 0, 0)));
        world.put(leftPlane).put(rightPlane);
        IRay ray = new Ray(new Point(0, 0, 0), new Vector(0, 1, 0));

        assertEquals(new Colour(0, 0, 0), world.illuminate(ray));
    }

    @Test
    void getRefractedColourFromOpaqueSurface() {
        IRay ray = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));

        Optional<RayHit> hit = RayHit.fromIntersections(defaultWorld.intersect(ray));
        assertTrue(hit.isPresent());
        RayHit hitpoint = hit.get();
        IColour refractedColour = defaultWorld.getRefractedColour(hitpoint);

        assertEquals(new Colour(0, 0, 0), refractedColour);
    }

    @Test
    void getRefractedColourForTotalInternalReflection() {
        IRay ray = new Ray(new Point(0, 0, Math.sqrt(2) / 2), new Vector(0, 1, 0));
        Material sphereMaterial = Material.builder()
                .diffuse(0.7).specular(0.2).ambient(0.1).shininess(200.0).transparency(1.0).refractiveIndex(1.5)
                .texture(new MonocolourTexture(new Colour(0.8, 1.0, 0.6))).build();
        Shape sphere = new Sphere(sphereMaterial);
        World world = new World();
        world.put(new LightSource(new Colour(1, 1, 1), new Point(-10, 10, -10))).put(sphere);

        Optional<RayHit> hit = RayHit.fromIntersections(world.intersect(ray));
        assertTrue(hit.isPresent());
        RayHit hitpoint = hit.get();
        IColour refractedColour = world.getRefractedColour(hitpoint);

        assertEquals(new Colour(0, 0, 0), refractedColour);
    }

    @Test
    void getRefractedColour() {
        Shape outer = new Sphere(outerMaterial.toBuilder().ambient(1.0).texture(new TestTexture()).build());
        Shape inner = new Sphere(innerMaterial.toBuilder().transparency(1.0).refractiveIndex(1.5).build());
        inner.setTransform(ThreeTransform.scaling(0.5, 0.5, 0.5));
        World world = new World();
        world.put(outer).put(inner).put(new LightSource(new Colour(1, 1, 1), new Point(-10, 10, -10)));
        IRay ray = new Ray(new Point(0, 0, 0.1), new Vector(0, 1, 0));

        Optional<RayHit> hit = RayHit.fromIntersections(world.intersect(ray));
        assertTrue(hit.isPresent());
        RayHit hitpoint = hit.get();
        IColour refractedColour = world.getRefractedColour(hitpoint);

        assertEquals(new Colour(0, 0.99888, 0.04725), refractedColour);
    }

    @Test
    void illuminatingWithTransparentMaterial() {
        Material planeMaterial = Material.builder().texture(new MonocolourTexture(new Colour(1, 1, 1)))
            .diffuse(0.9).specular(0.9).shininess(200.0).ambient(0.1).transparency(0.5).refractiveIndex(1.5).build();
        Shape floor = new Plane(planeMaterial);
        floor.setTransform(ThreeTransform.translation(0, -1, 0));
        Material belowMaterial = Material.builder().texture(new MonocolourTexture(new Colour(1, 0, 0)))
            .diffuse(0.9).specular(0.9).shininess(200.0).ambient(0.5).build();
        Shape below = new Sphere(belowMaterial);
        below.setTransform(ThreeTransform.translation(0, -3.5, -0.5));
        IWorld world = new World().put(new LightSource(new Colour(1, 1, 1), new Point(-10, 10, -10)));
        world.put(floor).put(below).put(outerSphere);
        IRay ray = new Ray(new Point(0, 0, -3), new Vector(0, -Math.sqrt(2)/2, Math.sqrt(2)/2));

        IColour expectedColor = new Colour(0.93642, 0.68642, 0.68642);
        IColour actualColor = world.illuminate(ray);

        assertEquals(expectedColor, actualColor);
    }

    @Test
    void illuminatingWithTransparentAndReflectiveMaterial() {
        Material planeMaterial = Material.builder().texture(new MonocolourTexture(new Colour(1, 1, 1)))
            .diffuse(0.9).specular(0.9).shininess(200.0).ambient(0.1).transparency(0.5).reflectivity(0.5)
            .refractiveIndex(1.5).build();
        Shape floor = new Plane(planeMaterial);
        floor.setTransform(ThreeTransform.translation(0, -1, 0));
        Material belowMaterial = Material.builder().texture(new MonocolourTexture(new Colour(1, 0, 0)))
            .diffuse(0.9).specular(0.9).shininess(200.0).ambient(0.5).build();
        Shape below = new Sphere(belowMaterial);
        below.setTransform(ThreeTransform.translation(0, -3.5, -0.5));
        IWorld world = new World().put(new LightSource(new Colour(1, 1, 1), new Point(-10, 10, -10)));
        world.put(floor).put(below).put(outerSphere);
        IRay ray = new Ray(new Point(0, 0, -3), new Vector(0, -Math.sqrt(2)/2, Math.sqrt(2)/2));

        IColour expectedColor = new Colour(0.93391, 0.69643, 0.69243);
        IColour actualColor = world.illuminate(ray);

        assertEquals(expectedColor, actualColor);
    }
}
