package io.raytracer.worldly;

import io.raytracer.drawing.IColour;
import io.raytracer.drawing.Colour;
import io.raytracer.drawing.patterns.Monopattern;
import io.raytracer.drawing.patterns.Pattern;
import io.raytracer.drawing.patterns.StripedPattern;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.Vector;
import io.raytracer.worldly.drawables.Drawable;
import io.raytracer.worldly.drawables.Sphere;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LightSourceTest {
    Material material;
    IPoint position;

    @BeforeEach
    void setupMaterialAndPosition() {
        material = Material.builder()
                .pattern(new Monopattern(new Colour(1, 1, 1))).ambient(0.1).diffuse(0.9).specular(0.9).shininess(200)
                .build();
        position = new Point(0, 0, 0);
    }

    @Test
    void illuminateWithLightEyeSurfaceNormalInLine() {
        IVector eyeVector = new Vector(0, 0, -1);
        IVector normalVector = new Vector(0, 0, -1);
        IVector reflectionVector = normalVector;
        boolean shadowed = false;
        ILightSource source = new LightSource(
                new Colour(1, 1, 1), new Point(0, 0, -10));
        MaterialPoint illuminated = new MaterialPoint(
                new Sphere(material), position, normalVector, reflectionVector, eyeVector, shadowed);
        IColour expectedResult = new Colour(1.9, 1.9, 1.9);
        IColour actualResult = source.illuminate(illuminated);

        assertEquals(expectedResult, actualResult,
                "Should correctly illuminate with eye between the light and the source.");
    }

    @Test
    void illuminateWithLightOppositeSurfaceEyeOffset45Degrees() {
        IVector eyeVector = new Vector(0, Math.sqrt(2) / 2, -Math.sqrt(2) / 2);
        IVector normalVector = new Vector(0, 0, -1);
        IVector reflectionVector = normalVector;
        boolean shadowed = false;
        ILightSource source = new LightSource(
                new Colour(1, 1, 1), new Point(0, 0, -10));
        MaterialPoint illuminated = new MaterialPoint(
                new Sphere(material), position, normalVector, reflectionVector, eyeVector, shadowed);
        IColour expectedResult = new Colour(1, 1, 1);
        IColour actualResult = source.illuminate(illuminated);

        assertEquals(expectedResult, actualResult,
                "Should correctly illuminate with eye between the light and the source, eye-offset 45.");
    }

    @Test
    void illuminateWithEyeOppositeSurfaceLightOffset45Degrees() {
        IVector eyeVector = new Vector(0, 0, -1);
        IVector normalVector = new Vector(0, 0, -1);
        IVector reflectionVector = new Vector(-1, -1, 0);
        boolean shadowed = false;
        ILightSource source = new LightSource(
                new Colour(1, 1, 1), new Point(0, 10, -10));
        MaterialPoint illuminated = new MaterialPoint(
                new Sphere(material), position, normalVector, reflectionVector, eyeVector, shadowed);
        IColour expectedResult = new Colour(0.7364, 0.7364, 0.7364);
        IColour actualResult = source.illuminate(illuminated);

        assertEquals(expectedResult, actualResult,
                "Should correctly illuminate with eye opposite the surface, source-offset 45.");
    }

    @Test
    void illuminateWithEyeInThePathOfReflectionVector() {
        IVector eyeVector = new Vector(0, -Math.sqrt(2) / 2, -Math.sqrt(2) / 2);
        IVector normalVector = new Vector(0, 0, -1);
        IVector reflectionVector = new Vector(-1, -1, 0);
        boolean shadowed = false;
        ILightSource source = new LightSource(
                new Colour(1, 1, 1), new Point(0, 10, -10));
        MaterialPoint illuminated = new MaterialPoint(
                new Sphere(material), position, normalVector, reflectionVector, eyeVector, shadowed);
        IColour expectedResult = new Colour(1.6364, 1.6364, 1.6364);
        IColour actualResult = source.illuminate(illuminated);

        assertEquals(expectedResult, actualResult,
                "Should correctly illuminate with eye in the path of the reflection vector.");
    }

    @Test
    void illuminateWithLightBehindTheSurface() {
        IVector eyeVector = new Vector(0, Math.sqrt(2) / 2, -Math.sqrt(2) / 2);
        IVector normalVector = new Vector(0, 0, -1);
        IVector reflectionVector = normalVector.negate();
        boolean shadowed = false;
        ILightSource source = new LightSource(
                new Colour(1, 1, 1), new Point(0, 0, 10));
        MaterialPoint illuminated = new MaterialPoint(
                new Sphere(material), position, normalVector, reflectionVector, eyeVector, shadowed);
        IColour expectedResult = new Colour(0.1, 0.1, 0.1);
        IColour actualResult = source.illuminate(illuminated);

        assertEquals(expectedResult, actualResult,
                "Should correctly compute illumination with the light behind the surface.");
    }

    @Test
    void illuminateWithTheSurfaceInTheShadow() {
        IVector eyeVector = new Vector(0, 0, -1);
        IVector normal = new Vector(0, 0, -1);
        IVector reflection = normal;
        boolean shadowed = true;
        ILightSource source = new LightSource(new Colour(1, 1, 1), new Point(0, 0, -10));
        MaterialPoint illuminated = new MaterialPoint(new Sphere(material), position, normal, reflection, eyeVector, shadowed);

        IColour expectedResult = new Colour(0.1, 0.1, 0.1);
        IColour actualResult = source.illuminate(illuminated);

        assertEquals(expectedResult, actualResult, "Should correctly compute illumination for points in shadows.");
    }

    @Test
    void illuminateWithPatternApplied() {
        IColour black = new Colour(0, 0, 0);
        IColour white = new Colour(1, 1,1);
        IVector eyeVector = new Vector(0, 0, -1);
        IVector normal = new Vector(0, 0, -1);
        IVector reflection = normal;
        boolean shadowed = false;
        Material patternedMaterial = Material.builder()
            .pattern(new StripedPattern(white, black))
            .ambient(1).diffuse(0).specular(0).shininess(200)
            .build();
        ILightSource source = new LightSource(new Colour(1, 1, 1), new Point(0, 0, -10));
        MaterialPoint firstIlluminated = new MaterialPoint(
            new Sphere(patternedMaterial), new Point(0.9, 0, 0), normal, reflection, eyeVector, shadowed);
        MaterialPoint secondIlluminated = new MaterialPoint(
            new Sphere(patternedMaterial), new Point(1.1, 0, 0), normal, reflection, eyeVector, shadowed);

        assertEquals(white, source.illuminate(firstIlluminated));
        assertEquals(black, source.illuminate(secondIlluminated));
    }

    @Test
    void getObjectColourForTransformedObject() {
        IColour white = new Colour(1, 1, 1);
        IColour black = new Colour(0, 0, 0);
        Material patternedMaterial = Material.builder()
                .pattern(new StripedPattern(white, black))
                .ambient(1).diffuse(0).specular(0).shininess(200)
                .build();
        Drawable sphere = new Sphere(patternedMaterial);
        sphere.setTransform(ThreeTransform.scaling(2, 2, 2));
        LightSource source = new LightSource(new Colour(1, 1, 1), new Point(0, 0, -10));

        IColour colour = source.getObjectColour(sphere, new Point(1.5, 0, 0));

        assertEquals(white, colour);
    }

    @Test
    void getObjectColourForTransformedPattern() {
        IColour white = new Colour(1, 1, 1);
        IColour black = new Colour(0, 0, 0);
        Pattern stripedPattern = new StripedPattern(white, black);
        stripedPattern.setTransform(ThreeTransform.scaling(2, 2, 2));
        Material patternedMaterial = Material.builder()
                .pattern(stripedPattern)
                .ambient(1).diffuse(0).specular(0).shininess(200)
                .build();
        Drawable sphere = new Sphere(patternedMaterial);
        LightSource source = new LightSource(new Colour(1, 1, 1), new Point(0, 0, -10));

        IColour colour = source.getObjectColour(sphere, new Point(1.5, 0, 0));

        assertEquals(white, colour);
    }

    @Test
    void getObjectColourForBothTransformedObjectAndPattern() {
        IColour white = new Colour(1, 1, 1);
        IColour black = new Colour(0, 0, 0);
        Pattern stripedPattern = new StripedPattern(white, black);
        stripedPattern.setTransform(ThreeTransform.translation(0.5, 0, 0));
        Material patternedMaterial = Material.builder()
                .pattern(stripedPattern)
                .ambient(1).diffuse(0).specular(0).shininess(200)
                .build();
        Drawable sphere = new Sphere(patternedMaterial);
        sphere.setTransform(ThreeTransform.scaling(2, 2, 2));
        LightSource source = new LightSource(new Colour(1, 1, 1), new Point(0, 0, -10));

        IColour firstColour = source.getObjectColour(sphere, new Point(1.6, 0, 0));
        IColour secondColour = source.getObjectColour(sphere, new Point(3.1, 0, 0));

        assertEquals(white, firstColour);
        assertEquals(black, secondColour);
    }
}
