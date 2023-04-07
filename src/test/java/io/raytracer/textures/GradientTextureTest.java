package io.raytracer.textures;

import io.raytracer.tools.Colour;
import io.raytracer.geometry.Point;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GradientTextureTest {
    private final Colour black = new Colour(0, 0, 0);
    private final Colour white = new Colour(1, 1, 1);

    @Test
    void gradientTextureLinearlyInterpolatesInX() {
        Texture gradient = new GradientTexture(black, white);

        assertEquals(black, gradient.colourAt(new Point(0, 0, 0)));
        assertEquals(new Colour(0.2, 0.2, 0.2), gradient.colourAt(new Point(0.2, 0, 0)));
    }
}