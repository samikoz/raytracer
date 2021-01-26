package io.raytracer.light;

import io.raytracer.drawing.Colour;
import io.raytracer.drawing.ColourImpl;
import io.raytracer.drawing.Material;
import io.raytracer.drawing.SphereImpl;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.PointImpl;
import io.raytracer.geometry.Vector;
import io.raytracer.geometry.VectorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LightingTest {
    Material material;
    Point position;

    @BeforeEach
    void setupMaterialAndPosition() {
        material = new Material();
        position = new PointImpl(0, 0, 0);
    }

    @Test
    void eyeBetweenLightAndSurface() {
        Vector eyeVector = new VectorImpl(0, 0, -1);
        Vector normalVector = new VectorImpl(0, 0, -1);
        LightSource source = new LightSource(
                new ColourImpl(1, 1, 1), new PointImpl(0, 0, -10));
        IlluminatedPoint illuminated = new IlluminatedPoint(0, new SphereImpl(), position, normalVector, eyeVector);
        Colour expectedResult = new ColourImpl(1.9, 1.9, 1.9);
        Colour actualResult = Lighting.illuminate(source, material, illuminated);

        assertEquals(expectedResult, actualResult,
                "Should correctly illuminate with eye between the light and the source.");
    }

    @Test
    void eyeBetweenLightAndSurfaceEyeOffset45Deg() {
        Vector eyeVector = new VectorImpl(0, Math.sqrt(2) / 2, -Math.sqrt(2) / 2);
        Vector normalVector = new VectorImpl(0, 0, -1);
        LightSource source = new LightSource(
                new ColourImpl(1, 1, 1), new PointImpl(0, 0, -10));
        IlluminatedPoint illuminated = new IlluminatedPoint(0, new SphereImpl(), position, normalVector, eyeVector);
        Colour expectedResult = new ColourImpl(1, 1, 1);
        Colour actualResult = Lighting.illuminate(source, material, illuminated);

        assertEquals(expectedResult, actualResult,
                "Should correctly illuminate with eye between the light and the source, eye-offset 45.");
    }

    @Test
    void eyeOppositeSurfaceSourceOffset45Deg() {
        Vector eyeVector = new VectorImpl(0, 0, -1);
        Vector normalVector = new VectorImpl(0, 0, -1);
        LightSource source = new LightSource(
                new ColourImpl(1, 1, 1), new PointImpl(0, 10, -10));
        IlluminatedPoint illuminated = new IlluminatedPoint(0, new SphereImpl(), position, normalVector, eyeVector);
        Colour expectedResult = new ColourImpl(0.7364, 0.7364, 0.7364);
        Colour actualResult = Lighting.illuminate(source, material, illuminated);

        assertEquals(expectedResult, actualResult,
                "Should correctly illuminate with eye opposite the surface, source-offset 45.");
    }

    @Test
    void eyeInThePathOfTheReflectionVector() {
        Vector eyeVector = new VectorImpl(0, -Math.sqrt(2) / 2, -Math.sqrt(2) / 2);
        Vector normalVector = new VectorImpl(0, 0, -1);
        LightSource source = new LightSource(
                new ColourImpl(1, 1, 1), new PointImpl(0, 10, -10));
        IlluminatedPoint illuminated = new IlluminatedPoint(0, new SphereImpl(), position, normalVector, eyeVector);
        Colour expectedResult = new ColourImpl(1.6364, 1.6364, 1.6364);
        Colour actualResult = Lighting.illuminate(source, material, illuminated);

        assertEquals(expectedResult, actualResult,
                "Should correctly illuminate with eye in the path of the reflection vector.");
    }

    @Test
    void lightBehindTheSurface() {
        Vector eyeVector = new VectorImpl(0, Math.sqrt(2) / 2, -Math.sqrt(2) / 2);
        Vector normalVector = new VectorImpl(0, 0, -1);
        LightSource source = new LightSource(
                new ColourImpl(1, 1, 1), new PointImpl(0, 0, 10));
        IlluminatedPoint illuminated = new IlluminatedPoint(0, new SphereImpl(), position, normalVector, eyeVector);
        Colour expectedResult = new ColourImpl(0.1, 0.1, 0.1);
        Colour actualResult = Lighting.illuminate(source, material, illuminated);

        assertEquals(expectedResult, actualResult,
                "Should correctly compute illumination with the light behind the surface.");
    }
}
