package io.raytracer.mechanics;

import io.raytracer.drawables.Plane;
import io.raytracer.tools.IColour;
import io.raytracer.tools.Colour;
import io.raytracer.textures.MonocolourTexture;
import io.raytracer.textures.Texture;
import io.raytracer.textures.StripedTexture;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.Vector;
import io.raytracer.drawables.Drawable;
import io.raytracer.drawables.Sphere;
import io.raytracer.materials.Material;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LightSourceTest {
    Material material;
    Drawable plane;

    @BeforeEach
    void setupMaterialAndHitpoint() {
        material = Material.builder()
                .texture(new MonocolourTexture(new Colour(1, 1, 1))).ambient(0.1).diffuse(0.9).specular(0.9).shininess(200.0)
                .build();
        plane = new Plane(material);
        plane.setTransform(ThreeTransform.rotation_x(Math.PI / 2));

    }

    @Test
    void illuminateWithLightEyeSurfaceNormalInLine() {
        ILightSource source = new LightSource(new Colour(1, 1, 1), new Point(0, 0, -10));
        IRay ray = new Ray(new Point(0, 0, -1), new Vector(0, 0, 1));
        Optional<RayHit> hitMaybe = RayHit.fromIntersections(new Intersection[] { new Intersection(ray, 1, plane) });
        assertTrue(hitMaybe.isPresent());
        RayHit hitpoint = hitMaybe.get();
        IColour expectedResult = new Colour(1.9, 1.9, 1.9);
        IColour actualResult = source.illuminate(hitpoint);

        assertEquals(expectedResult, actualResult, "Should correctly illuminate with eye between the light and the source.");
    }

    @Test
    void illuminateWithLightOppositeSurfaceEyeOffset45Degrees() {
        ILightSource source = new LightSource(new Colour(1, 1, 1), new Point(0, 0, -10));
        IRay ray = new Ray(new Point(0, Math.sqrt(2) / 2, -Math.sqrt(2) / 2), new Vector(0, -Math.sqrt(2) / 2, Math.sqrt(2) / 2));
        Optional<RayHit> hitMaybe = RayHit.fromIntersections(new Intersection[] { new Intersection(ray, 1, plane) });
        assertTrue(hitMaybe.isPresent());
        RayHit hitpoint = hitMaybe.get();
        IColour expectedResult = new Colour(1, 1, 1);
        IColour actualResult = source.illuminate(hitpoint);

        assertEquals(expectedResult, actualResult, "Should correctly illuminate with eye between the light and the source, eye-offset 45.");
    }

    @Test
    void illuminateWithEyeOppositeSurfaceLightOffset45Degrees() {
        ILightSource source = new LightSource(new Colour(1, 1, 1), new Point(0, 10, -10));
        IRay ray = new Ray(new Point(0, 0, -1), new Vector(0, 0, 1));
        Optional<RayHit> hitMaybe = RayHit.fromIntersections(new Intersection[] { new Intersection(ray, 1, plane) });
        assertTrue(hitMaybe.isPresent());
        RayHit hitpoint = hitMaybe.get();
        IColour expectedResult = new Colour(0.7364, 0.7364, 0.7364);
        IColour actualResult = source.illuminate(hitpoint);

        assertEquals(expectedResult, actualResult, "Should correctly illuminate with eye opposite the surface, source-offset 45.");
    }

    @Test
    void illuminateWithEyeInThePathOfReflectionVector() {
        ILightSource source = new LightSource(new Colour(1, 1, 1), new Point(0, 10, -10));
        IRay ray = new Ray(new Point(0, -Math.sqrt(2) / 2, -Math.sqrt(2) / 2), new Vector(0, Math.sqrt(2) / 2, Math.sqrt(2) / 2));
        Optional<RayHit> hitMaybe = RayHit.fromIntersections(new Intersection[] { new Intersection(ray, 1, plane) });
        assertTrue(hitMaybe.isPresent());
        RayHit hitpoint = hitMaybe.get();
        IColour expectedResult = new Colour(1.6364, 1.6364, 1.6364);
        IColour actualResult = source.illuminate(hitpoint);

        assertEquals(expectedResult, actualResult,
                "Should correctly illuminate with eye in the path of the reflection vector.");
    }

    @Test
    void illuminateWithLightBehindTheSurface() {
        ILightSource source = new LightSource(new Colour(1, 1, 1), new Point(0, 0, 10));
        IRay ray = new Ray(new Point(0, Math.sqrt(2) / 2, -Math.sqrt(2) / 2), new Vector(0, -Math.sqrt(2) / 2, Math.sqrt(2) / 2));
        Optional<RayHit> hitMaybe = RayHit.fromIntersections(new Intersection[] { new Intersection(ray, 1, plane) });
        assertTrue(hitMaybe.isPresent());
        RayHit hitpoint = hitMaybe.get();
        IColour expectedResult = new Colour(0.1, 0.1, 0.1);
        IColour actualResult = source.illuminate(hitpoint);

        assertEquals(expectedResult, actualResult,
                "Should correctly compute illumination with the light behind the surface.");
    }

    @Test
    void illuminateWithTheSurfaceInTheShadow() {
        ILightSource source = new LightSource(new Colour(1, 1, 1), new Point(0, 0, -10));
        IRay ray = new Ray(new Point(-1, 0, 0), new Vector(1, 0, 0));
        Optional<RayHit> hitMaybe = RayHit.fromIntersections(new Intersection[] { new Intersection(ray, 1, plane) });
        assertTrue(hitMaybe.isPresent());
        RayHit hitpoint = hitMaybe.get();
        hitpoint.shadowed = true;

        IColour expectedResult = new Colour(0.1, 0.1, 0.1);
        IColour actualResult = source.illuminate(hitpoint);

        assertEquals(expectedResult, actualResult, "Should correctly compute illumination for points in shadows.");
    }

    @Test
    void illuminateWithTextureApplied() {
        IColour black = new Colour(0, 0, 0);
        IColour white = new Colour(1, 1,1);
        Material textureedMaterial = Material.builder()
            .texture(new StripedTexture(white, black))
            .ambient(1.0).diffuse(0.0).specular(0.0).shininess(200.0)
            .build();
        Drawable stripedPlane = new Plane(textureedMaterial);
        ILightSource source = new LightSource(new Colour(1, 1, 1), new Point(0, 0, -10));
        IRay firstRay = new Ray(new Point(0.9, 0, -1), new Vector(0, 0, 1));
        Optional<RayHit> firstHitMaybe = RayHit.fromIntersections(new Intersection[] { new Intersection(firstRay, 1, stripedPlane) });
        assertTrue(firstHitMaybe.isPresent());
        RayHit firstHitpoint = firstHitMaybe.get();
        IRay secondRay = new Ray(new Point(1.1, 0, -1), new Vector(0, 0, 1));
        Optional<RayHit> secondHitMaybe = RayHit.fromIntersections(new Intersection[] { new Intersection(secondRay, 1, stripedPlane) });
        assertTrue(secondHitMaybe.isPresent());
        RayHit secondHitpoint = secondHitMaybe.get();

        assertEquals(white, source.illuminate(firstHitpoint));
        assertEquals(black, source.illuminate(secondHitpoint));
    }

    @Test
    void getObjectColourForTransformedObject() {
        IColour white = new Colour(1, 1, 1);
        IColour black = new Colour(0, 0, 0);
        Material textureedMaterial = Material.builder()
                .texture(new StripedTexture(white, black))
                .ambient(1.0).diffuse(0.0).specular(0.0).shininess(200.0)
                .build();
        Drawable sphere = new Sphere(textureedMaterial);
        sphere.setTransform(ThreeTransform.scaling(2, 2, 2));
        LightSource source = new LightSource(new Colour(1, 1, 1), new Point(0, 0, -10));

        IColour colour = source.getObjectColour(sphere, new Point(1.5, 0, 0));

        assertEquals(white, colour);
    }

    @Test
    void getObjectColourForTransformedTexture() {
        IColour white = new Colour(1, 1, 1);
        IColour black = new Colour(0, 0, 0);
        Texture stripedTexture = new StripedTexture(white, black);
        stripedTexture.setTransform(ThreeTransform.scaling(2, 2, 2));
        Material textureedMaterial = Material.builder()
                .texture(stripedTexture)
                .ambient(1.0).diffuse(0.0).specular(0.0).shininess(200.0)
                .build();
        Drawable sphere = new Sphere(textureedMaterial);
        LightSource source = new LightSource(new Colour(1, 1, 1), new Point(0, 0, -10));

        IColour colour = source.getObjectColour(sphere, new Point(1.5, 0, 0));

        assertEquals(white, colour);
    }

    @Test
    void getObjectColourForBothTransformedObjectAndTexture() {
        IColour white = new Colour(1, 1, 1);
        IColour black = new Colour(0, 0, 0);
        Texture stripedTexture = new StripedTexture(white, black);
        stripedTexture.setTransform(ThreeTransform.translation(0.5, 0, 0));
        Material textureedMaterial = Material.builder()
                .texture(stripedTexture)
                .ambient(1.0).diffuse(0.0).specular(0.0).shininess(200.0)
                .build();
        Drawable sphere = new Sphere(textureedMaterial);
        sphere.setTransform(ThreeTransform.scaling(2, 2, 2));
        LightSource source = new LightSource(new Colour(1, 1, 1), new Point(0, 0, -10));

        IColour firstColour = source.getObjectColour(sphere, new Point(1.6, 0, 0));
        IColour secondColour = source.getObjectColour(sphere, new Point(3.1, 0, 0));

        assertEquals(white, firstColour);
        assertEquals(black, secondColour);
    }
}
