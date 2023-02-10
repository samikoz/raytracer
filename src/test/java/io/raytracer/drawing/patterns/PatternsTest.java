package io.raytracer.drawing.patterns;

import io.raytracer.drawing.Colour;
import io.raytracer.drawing.IColour;
import io.raytracer.geometry.Point;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PatternsTest {
    private final Colour black = new Colour(0, 0, 0);
    private final Colour white = new Colour(1, 1, 1);

    @Test
    void monopatternAlwaysReturnsTheSameColour() {
        Colour colour = new Colour(0.2, 0.3, 0.4);
        Monopattern pattern = new Monopattern(colour);

        IColour firstColour = pattern.colourAt(new Point(-1, 2, 15));
        IColour secondColour = pattern.colourAt(new Point(15, 0, 0.1));

        assertEquals(colour, firstColour);
        assertEquals(colour, secondColour);
    }

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

    @Test
    void gradientPatternLinearlyInterpolatesInX() {
        Pattern gradient = new GradientPattern(black, white);

        assertEquals(black, gradient.colourAt(new Point(0, 0, 0)));
        assertEquals(new Colour(0.2, 0.2, 0.2), gradient.colourAt(new Point(0.2, 0, 0)));
    }
}