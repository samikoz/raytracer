package io.raytracer.tools.patterns;

import io.raytracer.tools.Colour;
import io.raytracer.geometry.Point;
import io.raytracer.patterns.GradientPattern;
import io.raytracer.patterns.Pattern;
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