package io.raytracer.mathsy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColourTest {
    static String compareColourComponents(Colour expected, Colour actual) {
        return expected.getRed() + " should be " + actual.getRed() +
            ", " + expected.getGreen() + " should be " + actual.getGreen() +
            ", " + expected.getBlue() + " should be " + actual.getBlue();
    }

    @Test
    void add() {
        Colour first = new Unit3TupleColour(0.9, 0.6, 0.75);
        Colour second = new Unit3TupleColour(0.7, 0.1, 0.25);
        Colour expectedSum = new Unit3TupleColour(1.6, 0.7, 1);
        Colour summed = first.add(second);

        assertTrue(
            expectedSum.equalTo(summed),
            () -> "Sum of Colours should be a colour. " + compareColourComponents(expectedSum, summed)
        );
    }

    @Test
    void subtract() {
        Colour first = new Unit3TupleColour(0.9, 0.6, 0.75);
        Colour second = new Unit3TupleColour(0.7, 0.1, 0.25);
        Colour expectedDifference = new Unit3TupleColour(0.2, 0.5, 0.5);
        Colour subtracted = first.subtract(second);

        assertTrue(
            expectedDifference.equalTo(subtracted),
            () -> "Subtraction of Colours should be a colour. " +
                compareColourComponents(expectedDifference, subtracted)
        );
    }

    @Test
    void multiply() {
        Colour first = new Unit3TupleColour(0.2, 0.3, 0.4);
        Colour expectedProduct = new Unit3TupleColour(0.4, 0.6, 0.8);
        Colour multiplied = first.multiply(2);

        assertTrue(
            expectedProduct.equalTo(multiplied),
            () -> "Colour multiplied by scalar should be a colour. " +
                compareColourComponents(expectedProduct, multiplied)
        );
    }

    @Test
    void mix() {
        Colour first = new Unit3TupleColour(1, 0.2, 0.4);
        Colour second = new Unit3TupleColour(0.9, 1, 0.1);
        Colour expectedProduct = new Unit3TupleColour(0.9, 0.2, 0.04);
        Colour mixed = first.mix(second);

        assertTrue(
            expectedProduct.equalTo(mixed),
            () -> "Mixture of Colours should be a colour. " + compareColourComponents(expectedProduct, mixed)
        );
    }
}