package io.raytracer.drawing.patterns;

import io.raytracer.drawing.Colour;
import io.raytracer.drawing.IColour;
import io.raytracer.geometry.Point;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MonopatternTest {

    @Test
    void monopatternAlwaysReturnsTheSameColour() {
        Colour colour = new Colour(0.2, 0.3, 0.4);
        Monopattern pattern = new Monopattern(colour);

        IColour firstColour = pattern.colourAt(new Point(-1, 2, 15));
        IColour secondColour = pattern.colourAt(new Point(15, 0, 0.1));

        assertEquals(colour, firstColour);
        assertEquals(colour, secondColour);
    }
}