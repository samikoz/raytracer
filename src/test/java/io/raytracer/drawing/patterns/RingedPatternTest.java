package io.raytracer.drawing.patterns;

import io.raytracer.drawing.Colour;
import io.raytracer.geometry.Point;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RingedPatternTest {
    private final Colour black = new Colour(0, 0, 0);
    private final Colour white = new Colour(1, 1, 1);

    @Test
    void ringPatternExtendsInBothXAndZ() {
        Pattern ringed = new RingedPattern(white, black);

        assertEquals(white, ringed.colourAt(new Point(0, 0, 0)));
        assertEquals(black, ringed.colourAt(new Point(1, 0, 0)));
        assertEquals(black, ringed.colourAt(new Point(0, 0, 1)));
        assertEquals(black, ringed.colourAt(new Point(Math.sqrt(2) / 2, 0, Math.sqrt(2) / 2)));
    }
}