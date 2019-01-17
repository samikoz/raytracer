package io.raytracer.drawing;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ColourComparator {
    static String compareColourComponents(Colour expected, Colour actual) {
        return expected.getRed() + " should be " + actual.getRed() +
                ", " + expected.getGreen() + " should be " + actual.getGreen() +
                ", " + expected.getBlue() + " should be " + actual.getBlue();
    }
}

class ColourTest {

    @Test
    void add() {
        Colour first = new Unit3TupleColour(0.9, 0.6, 0.75);
        Colour second = new Unit3TupleColour(0.7, 0.1, 0.25);
        Colour expectedSum = new Unit3TupleColour(1.6, 0.7, 1);
        Colour summed = first.add(second);

        assertTrue(
            expectedSum.equalTo(summed),
            () -> "Sum of Colours should be a colour. " + ColourComparator.compareColourComponents(expectedSum, summed)
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
                ColourComparator.compareColourComponents(expectedDifference, subtracted)
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
                ColourComparator.compareColourComponents(expectedProduct, multiplied)
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
            () -> "Mixture of Colours should be a colour. " +
                ColourComparator.compareColourComponents(expectedProduct, mixed)
        );
    }
}

class CanvasTest{

    @Test
    void defaultCanvasColour() {
        Canvas canvas = new PPMCanvas(2, 3);

        Set<Colour> canvasColours = new HashSet<>();
        for (Colour c : canvas) {
            canvasColours.add(c);
        }

        assertAll(
            "Default canvas colour is all black.",
            () -> assertEquals(canvasColours.size(), 1, "Should be monocoloured by default."),
            () -> assertTrue(canvasColours.contains(new Unit3TupleColour(0, 0, 0)), "Should have black colour.")
        );
    }

    @Test
    void writeAndReadFromCanvas() {
        Colour first = new Unit3TupleColour(0.5, 0.2, 0.3);
        Colour second = new Unit3TupleColour(0, 0, 0.4);
        Canvas canvas = new PPMCanvas(10, 20);

        canvas.write(0, 19, first);
        canvas.write(2, 0, second);

        Colour firstRead = canvas.read(0, 19);
        Colour secondRead = canvas.read(2, 0);

        assertAll(
            "Reading should return written objects.",
            () -> assertTrue(
                firstRead.equalTo(first),
                () -> ColourComparator.compareColourComponents(firstRead, first)
            ),
            () -> assertTrue(
                secondRead.equalTo(second),
                () -> ColourComparator.compareColourComponents(secondRead, second)
            )
        );
    }
}