package io.raytracer.drawing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


class CanvasTest{
    @Test
    void defaultCanvasColour() {
        Canvas canvas = new PPMCanvas(2, 3);

        Colour defaultCanvasColour = canvas.read(1, 1);
        Colour black = new ColourImpl(0,0,0);

        assertEquals(defaultCanvasColour, black, "Default canvas colour should be black");
    }

    @Test
    void writeAndReadFromCanvas() {
        Colour first = new ColourImpl(0.5, 0.2, 0.3);
        Colour second = new ColourImpl(0, 0, 0.4);
        Canvas canvas = new PPMCanvas(10, 20);

        canvas.write(0, 19, first);
        canvas.write(2, 0, second);

        Colour firstRead = canvas.read(0, 19);
        Colour secondRead = canvas.read(2, 0);

        assertAll(
            "Reading should return written objects.",
            () -> assertEquals(firstRead, first,
                () -> ColourComparator.compareColourComponents(firstRead, first)
            ),
            () -> assertEquals(secondRead, second,
                () -> ColourComparator.compareColourComponents(secondRead, second)
            )
        );
    }

    @Test
    void correctPPMExportHeader() {
        Canvas canvas = new PPMCanvas(5, 3);
        String expectedPPMFileStart = "P3\n5 3\n255\n";

        assertTrue(
            canvas.export().startsWith(expectedPPMFileStart),
            "Exported canvas header should have correct format"
        );
    }

    @Test
    void correctPPMExportPixelData() {
        Canvas canvas = new PPMCanvas(5, 3);
        canvas.write(0, 0, new ColourImpl(1.5, 0,0));
        canvas.write(2, 1, new ColourImpl(0, 0.5, 0));
        canvas.write(4, 2, new ColourImpl(-0.5, 0, 1));

        String expectedFirstRowData = "255 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n";
        String expectedSecondRowData = "0 0 0 0 0 0 0 128 0 0 0 0 0 0 0\n";
        String expectedThirdRowData = "0 0 0 0 0 0 0 0 0 0 0 0 0 0 255\n";
        String expectedPixelData = expectedFirstRowData + expectedSecondRowData + expectedThirdRowData;

        assertTrue(
            canvas.export().endsWith(expectedPixelData),
            "Canvas should export correct pixel data."
        );
    }

    @Test
    void noLinesLongerThan70InExportedString() {
        Canvas canvas = new PPMCanvas(10, 2);
        Colour aColour = new ColourImpl(1, 0.8, 0.6);

        for (int i = 0; i < 10; ++i) {
            canvas.write(i, 0, aColour);
            canvas.write(i, 1, aColour);
        }

        String expectedPreLineBreak = "255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204";
        String expectedPostLineBreak = "153 255 204 153 255 204 153 255 204 153 255 204 153";

        assertTrue(
            canvas.export().endsWith(expectedPreLineBreak + "\n" + expectedPostLineBreak + "\n"),
            "Exporting should correctly break lines longer than 70 characters."
        );
    }
}
