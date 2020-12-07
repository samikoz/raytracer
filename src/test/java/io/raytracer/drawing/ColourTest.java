package io.raytracer.drawing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.raytracer.drawing.helpers.ColourComparator;
import org.junit.jupiter.api.Test;

class ColourTest {
    @Test
    void equalityUpToADelta() {
        Colour first = new ColourImpl(0.5, 0.2, 0);
        Colour second = new ColourImpl(0.5, 0.2 + 1e-4, 0);

        assertEquals(first, second, "Equality of colours should be evaluated up to a small delta.");
    }

    @Test
    void exportNormalised() {
        Colour c = new ColourImpl(0, 0.5, 1);

        assertEquals("0 128 255", c.exportNormalised(), "Exporting should integer-scale by 255");
    }

    @Test
    void add() {
        Colour first = new ColourImpl(0.9, 0.6, 0.75);
        Colour second = new ColourImpl(0.7, 0.1, 0.25);
        Colour expectedSum = new ColourImpl(1.6, 0.7, 1);
        Colour summed = first.add(second);

        assertEquals(expectedSum, summed,
            () -> "Sum of Colours should be a colour. " + ColourComparator.compareColourComponents(expectedSum, summed));
    }

    @Test
    void multiply() {
        Colour first = new ColourImpl(0.2, 0.3, 0.4);
        Colour expectedProduct = new ColourImpl(0.4, 0.6, 0.8);
        Colour multiplied = first.multiply(2);

        assertEquals(
            expectedProduct, multiplied,
            () -> "Colour multiplied by scalar should be a colour. " +
                ColourComparator.compareColourComponents(expectedProduct, multiplied));
    }

    @Test
    void mix() {
        Colour first = new ColourImpl(1, 0.2, 0.4);
        Colour second = new ColourImpl(0.9, 1, 0.1);
        Colour expectedProduct = new ColourImpl(0.9, 0.2, 0.04);
        Colour mixed = first.mix(second);

        assertEquals(expectedProduct, mixed,
            () -> "Mixture of Colours should be a colour. " +
                ColourComparator.compareColourComponents(expectedProduct, mixed));
    }
}
