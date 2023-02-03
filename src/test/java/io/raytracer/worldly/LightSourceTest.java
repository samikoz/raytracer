package io.raytracer.worldly;

import io.raytracer.drawing.IColour;
import io.raytracer.drawing.Colour;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LightSourceTest {
    Material material;
    IPoint position;

    @BeforeEach
    void setupMaterialAndPosition() {
        material = Material.builder()
                .colour(new Colour(1, 1, 1)).ambient(0.1).diffuse(0.9).specular(0.9).shininess(200)
                .build();
        position = new Point(0, 0, 0);
    }

    @Test
    void eyeBetweenLightAndSurface() {
        IVector eyeVector = new Vector(0, 0, -1);
        IVector normalVector = new Vector(0, 0, -1);
        boolean shadowed = false;
        ILightSource source = new LightSource(
                new Colour(1, 1, 1), new Point(0, 0, -10));
        MaterialPoint illuminated = new MaterialPoint(
                new Sphere(material), position, normalVector, eyeVector, shadowed);
        IColour expectedResult = new Colour(1.9, 1.9, 1.9);
        IColour actualResult = source.illuminate(illuminated);

        assertEquals(expectedResult, actualResult,
                "Should correctly illuminate with eye between the light and the source.");
    }

    @Test
    void eyeBetweenLightAndSurfaceEyeOffset45Deg() {
        IVector eyeVector = new Vector(0, Math.sqrt(2) / 2, -Math.sqrt(2) / 2);
        IVector normalVector = new Vector(0, 0, -1);
        boolean shadowed = false;
        ILightSource source = new LightSource(
                new Colour(1, 1, 1), new Point(0, 0, -10));
        MaterialPoint illuminated = new MaterialPoint(
                new Sphere(material), position, normalVector, eyeVector, shadowed);
        IColour expectedResult = new Colour(1, 1, 1);
        IColour actualResult = source.illuminate(illuminated);

        assertEquals(expectedResult, actualResult,
                "Should correctly illuminate with eye between the light and the source, eye-offset 45.");
    }

    @Test
    void eyeOppositeSurfaceSourceOffset45Deg() {
        IVector eyeVector = new Vector(0, 0, -1);
        IVector normalVector = new Vector(0, 0, -1);
        boolean shadowed = false;
        ILightSource source = new LightSource(
                new Colour(1, 1, 1), new Point(0, 10, -10));
        MaterialPoint illuminated = new MaterialPoint(
                new Sphere(material), position, normalVector, eyeVector, shadowed);
        IColour expectedResult = new Colour(0.7364, 0.7364, 0.7364);
        IColour actualResult = source.illuminate(illuminated);

        assertEquals(expectedResult, actualResult,
                "Should correctly illuminate with eye opposite the surface, source-offset 45.");
    }

    @Test
    void eyeInThePathOfTheReflectionVector() {
        IVector eyeVector = new Vector(0, -Math.sqrt(2) / 2, -Math.sqrt(2) / 2);
        IVector normalVector = new Vector(0, 0, -1);
        boolean shadowed = false;
        ILightSource source = new LightSource(
                new Colour(1, 1, 1), new Point(0, 10, -10));
        MaterialPoint illuminated = new MaterialPoint(
                new Sphere(material), position, normalVector, eyeVector, shadowed);
        IColour expectedResult = new Colour(1.6364, 1.6364, 1.6364);
        IColour actualResult = source.illuminate(illuminated);

        assertEquals(expectedResult, actualResult,
                "Should correctly illuminate with eye in the path of the reflection vector.");
    }

    @Test
    void lightBehindTheSurface() {
        IVector eyeVector = new Vector(0, Math.sqrt(2) / 2, -Math.sqrt(2) / 2);
        IVector normalVector = new Vector(0, 0, -1);
        boolean shadowed = false;
        ILightSource source = new LightSource(
                new Colour(1, 1, 1), new Point(0, 0, 10));
        MaterialPoint illuminated = new MaterialPoint(
                new Sphere(material), position, normalVector, eyeVector, shadowed);
        IColour expectedResult = new Colour(0.1, 0.1, 0.1);
        IColour actualResult = source.illuminate(illuminated);

        assertEquals(expectedResult, actualResult,
                "Should correctly compute illumination with the light behind the surface.");
    }

    @Test
    void lightWithTheSurfaceInTheShadow() {
        IVector eyeVector = new Vector(0, 0, -1);
        IVector normal = new Vector(0, 0, -1);
        boolean shadowed = true;
        ILightSource source = new LightSource(new Colour(1, 1, 1), new Point(0, 0, -10));
        MaterialPoint illuminated = new MaterialPoint(new Sphere(material), position, normal, eyeVector, shadowed);

        IColour expectedResult = new Colour(0.1, 0.1, 0.1);
        IColour actualResult = source.illuminate(illuminated);

        assertEquals(expectedResult, actualResult, "Should correctly compute illumination for points in shadows.");
    }
}
