package io.raytracer.drawing;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;

class PictureTest {
    @Test
    void defaultCanvasColour() {
        Picture picture = new PPMPicture(2, 3);

        Colour defaultCanvasColour = picture.read(1, 1);
        Colour black = new ColourImpl(0, 0, 0);

        assertEquals(defaultCanvasColour, black, "Default canvas colour should be black");
    }

    @Test
    void writeAndReadFromCanvas() {
        Colour first = new ColourImpl(0.5, 0.2, 0.3);
        Colour second = new ColourImpl(0, 0, 0.4);
        Picture picture = new PPMPicture(10, 20);

        picture.write(0, 19, first);
        picture.write(2, 0, second);

        Colour firstRead = picture.read(0, 19);
        Colour secondRead = picture.read(2, 0);

        assertAll("Reading from canvas should return written objects.",
                () -> assertEquals(firstRead, first),
                () -> assertEquals(secondRead, second)
        );
    }

    @Test
    void correctPPMExportHeader() {
        Picture picture = new PPMPicture(5, 3);
        String expectedPPMFileStart = "P3\n5 3\n255\n";

        assertTrue(picture.export().startsWith(expectedPPMFileStart),
                "Exported canvas header should have the correct format"
        );
    }

    @Test
    void correctPPMExportPixelData() {
        Picture picture = new PPMPicture(5, 3);
        picture.write(0, 0, new ColourImpl(1, 0, 0));
        picture.write(2, 1, new ColourImpl(0, 0.5, 0));
        picture.write(4, 2, new ColourImpl(-0.5, 0, 1));

        String expectedFirstRowData = "255 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n";
        String expectedSecondRowData = "0 0 0 0 0 0 0 128 0 0 0 0 0 0 0\n";
        String expectedThirdRowData = "0 0 0 0 0 0 0 0 0 0 0 0 0 0 255\n";
        String expectedPixelData = expectedFirstRowData + expectedSecondRowData + expectedThirdRowData;

        assertTrue(picture.export().endsWith(expectedPixelData));
    }

    @Test
    void noLinesLongerThan70InExportedString() {
        Picture picture = new PPMPicture(10, 2);
        Colour aColour = new ColourImpl(1, 0.8, 0.6);

        for (int i = 0; i < 10; ++i) {
            picture.write(i, 0, aColour);
            picture.write(i, 1, aColour);
        }

        String expectedPreLineBreak = "255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204";
        String expectedPostLineBreak = "153 255 204 153 255 204 153 255 204 153 255 204 153";

        assertTrue(picture.export().endsWith(expectedPreLineBreak + "\n" + expectedPostLineBreak + "\n"),
                "Exporting should correctly break lines longer than 70 characters."
        );
    }
}
