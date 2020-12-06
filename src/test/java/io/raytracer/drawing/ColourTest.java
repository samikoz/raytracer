package io.raytracer.drawing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class ColourTest {
    @Test
    void add() {
        Colour first = new ColourImpl(0.9, 0.6, 0.75);
        Colour second = new ColourImpl(0.7, 0.1, 0.25);
        Colour expectedSum = new ColourImpl(1.6, 0.7, 1);
        Colour summed = first.add(second);

        assertEquals(
            expectedSum, summed,
            () -> "Sum of Colours should be a colour. " + ColourComparator.compareColourComponents(expectedSum, summed)
        );
    }

    @Test
    void multiply() {
        Colour first = new ColourImpl(0.2, 0.3, 0.4);
        Colour expectedProduct = new ColourImpl(0.4, 0.6, 0.8);
        Colour multiplied = first.multiply(2);

        assertEquals(
            expectedProduct, multiplied,
            () -> "Colour multiplied by scalar should be a colour. " +
                ColourComparator.compareColourComponents(expectedProduct, multiplied)
        );
    }

    @Test
    void mix() {
        Colour first = new ColourImpl(1, 0.2, 0.4);
        Colour second = new ColourImpl(0.9, 1, 0.1);
        Colour expectedProduct = new ColourImpl(0.9, 0.2, 0.04);
        Colour mixed = first.mix(second);

        assertEquals(
            expectedProduct, mixed,
            () -> "Mixture of Colours should be a colour. " +
                ColourComparator.compareColourComponents(expectedProduct, mixed)
        );
    }
}
