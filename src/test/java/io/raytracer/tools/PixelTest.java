package io.raytracer.tools;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PixelTest {
    @Test
    void interpolate() {
        Pixel aPixel = new Pixel(10, 10);
        Pixel bPixel = new Pixel(15, 14);
        Pixel interpolated = new Pixel(12, 11);

        assertEquals(interpolated, aPixel.interpolate(bPixel, 1.0/3));
    }

    @Test
    void materialise() {
        Camera camera = new SingleRayCamera(
                10, 10, Math.PI/2,
                new Point(0, 0, 0), new Vector(1, 0, 0), new Vector(0, 1, 0)
        );
        Pixel testPixel = new Pixel(3, 5);
        IPoint materialisedPoint = testPixel.materialise(camera, 5);
        IPoint expectedPoint = new Point(5, -0.5, 1.5);

        assertEquals(expectedPoint, materialisedPoint);
    }
}