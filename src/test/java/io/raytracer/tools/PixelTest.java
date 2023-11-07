package io.raytracer.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PixelTest {

    @Test
    void interpolate() {
        Pixel aPixel = new Pixel(10, 10);
        Pixel bPixel = new Pixel(15, 14);
        Pixel interpolated = new Pixel(12, 11);

        assertEquals(interpolated, aPixel.interpolate(bPixel, 1.0/3));
    }
}