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

        assertEquals(
            expectedSum, summed,
            () -> "Sum of Colours should be a colour. " + ColourComparator.compareColourComponents(expectedSum, summed)
        );
    }

    @Test
    void multiply() {
        Colour first = new Unit3TupleColour(0.2, 0.3, 0.4);
        Colour expectedProduct = new Unit3TupleColour(0.4, 0.6, 0.8);
        Colour multiplied = first.multiply(2);

        assertEquals(
            expectedProduct, multiplied,
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

        assertEquals(
            expectedProduct, mixed,
            () -> "Mixture of Colours should be a colour. " +
                ColourComparator.compareColourComponents(expectedProduct, mixed)
        );
    }
}

class CanvasTest{

    @Test
    void defaultCanvasColour() {
        Canvas canvas = new PPMCanvas(2, 3);

        Colour defaultCanvasColour = canvas.read(1, 1);
        Colour black = new Unit3TupleColour(0,0,0);

        assertEquals(defaultCanvasColour, black, "Default canvas colour should be black");
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
            () -> assertEquals(
                firstRead, first,
                () -> ColourComparator.compareColourComponents(firstRead, first)
            ),
            () -> assertEquals(
                secondRead, second,
                () -> ColourComparator.compareColourComponents(secondRead, second)
            )
        );
    }

    @Test
    void correctPPMExportHeader() {
        Canvas canvas = new PPMCanvas(5, 3);
        String expectedPPMFileStart = "P3\n5 3\n255\n";

        assertTrue(
            canvas.exportToPPM().startsWith(expectedPPMFileStart),
            "Exported canvas header should have correct format"
        );
    }

    @Test
    void correctPPMExportPixelData() {
        Canvas canvas = new PPMCanvas(5, 3);
        canvas.write(0, 0, new Unit3TupleColour(1.5, 0,0));
        canvas.write(2, 1, new Unit3TupleColour(0, 0.5, 0));
        canvas.write(4, 2, new Unit3TupleColour(-0.5, 0, 1));

        String expectedFirstRowData = "255 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n";
        String expectedSecondRowData = "0 0 0 0 0 0 0 128 0 0 0 0 0 0 0\n";
        String expectedThirdRowData = "0 0 0 0 0 0 0 0 0 0 0 0 0 0 255\n";
        String expectedPixelData = expectedFirstRowData + expectedSecondRowData + expectedThirdRowData;

        assertTrue(
            canvas.exportToPPM().endsWith(expectedPixelData),
            "Canvas should export correct pixel data."
        );
    }

    @Test
    void noLinesLongerThan70InExportedString() {
        Canvas canvas = new PPMCanvas(10, 2);
        Colour aColour = new Unit3TupleColour(1, 0.8, 0.6);

        for (int i = 0; i < 10; ++i) {
            canvas.write(i, 0, aColour);
            canvas.write(i, 1, aColour);
        }

        String expectedPreLineBreak = "255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204";
        String expectedPostLineBreak = "153 255 204 153 255 204 153 255 204 153 255 204 153";

        assertTrue(
            canvas.exportToPPM().endsWith(expectedPreLineBreak + "\n" + expectedPostLineBreak + "\n"),
            "Exporting should correctly break lines longer than 70 characters."
        );
    }
}
