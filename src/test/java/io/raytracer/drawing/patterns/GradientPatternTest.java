package io.raytracer.drawing.patterns;

import io.raytracer.drawing.Colour;
import io.raytracer.geometry.Point;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GradientPatternTest {
    private final Colour black = new Colour(0, 0, 0);
    private final Colour white = new Colour(1, 1, 1);

    @Test
    void gradientPatternLinearlyInterpolatesInX() {
        Pattern gradient = new GradientPattern(black, white);

        assertEquals(black, gradient.colourAt(new Point(0, 0, 0)));
        assertEquals(new Colour(0.2, 0.2, 0.2), gradient.colourAt(new Point(0.2, 0, 0)));
    }
}