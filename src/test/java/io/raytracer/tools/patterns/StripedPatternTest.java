package io.raytracer.tools.patterns;

import io.raytracer.tools.Colour;
import io.raytracer.tools.IColour;
import io.raytracer.geometry.Point;
import io.raytracer.patterns.Pattern;
import io.raytracer.patterns.StripedPattern;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StripedPatternTest {
    private final Colour black = new Colour(0, 0, 0);
    private final Colour white = new Colour(1, 1, 1);

    @Test
    void stripedPatternIsConstantInY() {
        Pattern striped = new StripedPattern(white, black);
        IColour firstColour = striped.colourAt(new Point(0, 0, 0));
        IColour secondColour = striped.colourAt(new Point(0, 1, 0));
        IColour thirdColour = striped.colourAt(new Point(0, 2.3, 0));

        assertEquals(white, firstColour);
        assertEquals(white, secondColour);
        assertEquals(white, thirdColour);
    }

    @Test
    void stripedPatternIsConstantInZ() {
        Pattern striped = new StripedPattern(white, black);
        IColour firstColour = striped.colourAt(new Point(0, 0, 0));
        IColour secondColour = striped.colourAt(new Point(0, 0, 1));
        IColour thirdColour = striped.colourAt(new Point(0, 0, -2.3));

        assertEquals(white, firstColour);
        assertEquals(white, secondColour);
        assertEquals(white, thirdColour);
    }

    @Test
    void stripedPatternAlternatesInX() {
        Pattern striped = new StripedPattern(white, black);
        IColour firstColour = striped.colourAt(new Point(0, 0, 0));
        IColour secondColour = striped.colourAt(new Point(1, 0, 0));
        IColour thirdColour = striped.colourAt(new Point(2.3, 0, 0));

        assertEquals(white, firstColour);
        assertEquals(black, secondColour);
        assertEquals(white, thirdColour);
    }
}