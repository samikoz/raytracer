package io.raytracer.drawing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ColourTest {
    @Test
    void equalityUpToADelta() {
        IColour first = new Colour(0.5, 0.2, 0);
        IColour second = new Colour(0.5, 0.2 + 1e-4, 0);

        assertEquals(first, second, "Equality of colours should be evaluated up to a small delta.");
    }

    @Test
    void exportNormalised() {
        IColour c = new Colour(0, 0.5, 1);

        assertEquals("0 128 255", c.exportNormalised(), "Exporting should integer-scale by 255");
    }

    @Test
    void add() {
        IColour first = new Colour(0.9, 0.6, 0.75);
        IColour second = new Colour(0.7, 0.1, 0.25);
        IColour expectedSum = new Colour(1.6, 0.7, 1);
        IColour summed = first.add(second);

        assertEquals(expectedSum, summed);
    }

    @Test
    void multiply() {
        IColour first = new Colour(0.2, 0.3, 0.4);
        IColour expectedProduct = new Colour(0.4, 0.6, 0.8);
        IColour multiplied = first.multiply(2);

        assertEquals(expectedProduct, multiplied);
    }

    @Test
    void mix() {
        IColour first = new Colour(1, 0.2, 0.4);
        IColour second = new Colour(0.9, 1, 0.1);
        IColour expectedProduct = new Colour(0.9, 0.2, 0.04);
        IColour mixed = first.mix(second);

        assertEquals(expectedProduct, mixed);
    }

    @Test
    void interpolate() {
        IColour first = new Colour(1, 0, 0.2);
        IColour second = new Colour(1, 1, 0.6);

        IColour expectedInterpolation = new Colour(1, 0.5, 0.4);

        assertEquals(expectedInterpolation, first.interpolate(second, 0.5));
    }

    @Test
    void interpolateOutsideZeroOneRange() {
        IColour first = new Colour(1, 0, 0.2);
        IColour second = new Colour(1, 1, 0.6);

        assertEquals(first, first.interpolate(second, -1));
        assertEquals(second, first.interpolate(second, 2));
    }
}
