package io.raytracer.textures;

import io.raytracer.tools.LinearColour;
import io.raytracer.geometry.Point;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GradientTextureTest {
    private final LinearColour black = new LinearColour(0, 0, 0);
    private final LinearColour white = new LinearColour(1, 1, 1);

    @Test
    void gradientTextureLinearlyInterpolatesInX() {
        Texture gradient = new GradientTexture(black, white);

        assertEquals(black, gradient.colourAt(new Point(0, 0, 0)));
        assertEquals(new LinearColour(0.2, 0.2, 0.2), gradient.colourAt(new Point(0.2, 0, 0)));
    }
}