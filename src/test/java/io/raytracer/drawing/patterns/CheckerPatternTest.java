package io.raytracer.drawing.patterns;

import io.raytracer.drawing.Colour;
import io.raytracer.geometry.Point;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckerPatternTest {
    private final Colour black = new Colour(0, 0, 0);
    private final Colour white = new Colour(1, 1, 1);

    @Test
    void checkerPatternRepeatsInThreeDimensions() {
        Pattern checker = new CheckerPattern(white, black);
        double belowOne = 0.99;
        double aboveOne = 1.01;

        assertEquals(white, checker.colourAt(new Point(0, 0, 0)));
        assertEquals(white, checker.colourAt(new Point(belowOne, 0, 0)));
        assertEquals(black, checker.colourAt(new Point(aboveOne, 0, 0)));
        assertEquals(white, checker.colourAt(new Point(1 + aboveOne, 0, 0)));
        assertEquals(black, checker.colourAt(new Point(2 + aboveOne, 0, 0)));

        assertEquals(white, checker.colourAt(new Point(0, belowOne, 0)));
        assertEquals(black, checker.colourAt(new Point(0, aboveOne, 0)));

        assertEquals(white, checker.colourAt(new Point(0, 0, belowOne)));
        assertEquals(black, checker.colourAt(new Point(0, 0, aboveOne)));
    }
}