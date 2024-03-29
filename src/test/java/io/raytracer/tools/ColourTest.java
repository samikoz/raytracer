package io.raytracer.tools;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ColourTest {
    @Test
    void equalityUpToADelta() {
        IColour first = new LinearColour(0.5, 0.2, 0);
        IColour second = new LinearColour(0.5, 0.2 + 1e-4, 0);

        assertEquals(first, second, "Equality of colours should be evaluated up to a small delta.");
    }

    @Test
    void exportLinear() {
        IColour c = new LinearColour(0, 0.5, 1);

        assertEquals("0 128 255", c.export(), "Exporting should integer-scale by 255 and gamma-correct");
    }

    @Test
    void add() {
        IColour first = new LinearColour(0.9, 0.6, 0.75);
        IColour second = new LinearColour(0.7, 0.1, 0.25);
        IColour expectedSum = new LinearColour(1.6, 0.7, 1);
        IColour summed = first.add(second);

        assertEquals(expectedSum, summed);
    }

    @Test
    void multiply() {
        IColour first = new LinearColour(0.2, 0.3, 0.4);
        IColour expectedProduct = new LinearColour(0.4, 0.6, 0.8);
        IColour multiplied = first.multiply(2);

        assertEquals(expectedProduct, multiplied);
    }

    @Test
    void mix() {
        IColour first = new LinearColour(1, 0.2, 0.4);
        IColour second = new LinearColour(0.9, 1, 0.1);
        IColour expectedProduct = new LinearColour(0.9, 0.2, 0.04);
        IColour mixed = first.mix(second);

        assertEquals(expectedProduct, mixed);
    }

    @Test
    void interpolate() {
        IColour first = new LinearColour(1, 0, 0.2);
        IColour second = new LinearColour(1, 1, 0.6);

        IColour expectedInterpolation = new LinearColour(1, 0.5, 0.4);

        assertEquals(expectedInterpolation, first.interpolate(second, 0.5));
    }

    @Test
    void interpolateOutsideZeroOneRange() {
        IColour first = new LinearColour(1, 0, 0.2);
        IColour second = new LinearColour(1, 1, 0.6);

        assertEquals(first, first.interpolate(second, -1));
        assertEquals(second, first.interpolate(second, 2));
    }
}
