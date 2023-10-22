package io.raytracer.tools;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

class PPMPictureTest {
    @Test
    void defaultCanvasColour() {
        IPicture picture = new PPMPicture(2, 3);

        IColour defaultCanvasColour = picture.read(1, 1);
        IColour black = new LinearColour(0, 0, 0);

        assertEquals(defaultCanvasColour, black, "Default canvas colour should be black");
    }

    @Test
    void writeAndReadFromCanvas() {
        IColour first = new LinearColour(0.5, 0.2, 0.3);
        IColour second = new LinearColour(0, 0, 0.4);
        IPicture picture = new PPMPicture(10, 20);

        picture.write(0, 19, first);
        picture.write(2, 0, second);

        IColour firstRead = picture.read(0, 19);
        IColour secondRead = picture.read(2, 0);

        assertAll("Reading from canvas should return written objects.",
                () -> assertEquals(firstRead, first),
                () -> assertEquals(secondRead, second)
        );
    }

    @Test
    void correctPPMExportHeader(@TempDir Path tempdir) throws IOException {
        Path tempPath = tempdir.resolve("test.mth");

        IPicture picture = new PPMPicture(5, 3);
        String expectedPPMFileStart = "P3\n5 3\n255";

        picture.export(tempPath);
        String readLine = Files.readAllLines(tempPath).stream().limit(3).collect(Collectors.joining("\n"));
        assertTrue(readLine.startsWith(expectedPPMFileStart),
                "Exported canvas header should have the correct format"
        );
    }

    @Test
    void correctPPMExportPixelData(@TempDir Path tempdir) throws IOException {
        Path tempPath = tempdir.resolve("test.mth");

        IPicture picture = new PPMPicture(5, 3);
        picture.write(0, 0, new GammaColour(1, 0, 0));
        picture.write(2, 1, new GammaColour(0, 0.5, 0));
        picture.write(4, 2, new GammaColour(-0.5, 0, 1));

        String expectedFirstRowData = "255 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n";
        String expectedSecondRowData = "0 0 0 0 0 0 0 180 0 0 0 0 0 0 0\n";
        String expectedThirdRowData = "0 0 0 0 0 0 0 0 0 0 0 0 0 0 255";
        String expectedPixelData = expectedFirstRowData + expectedSecondRowData + expectedThirdRowData;
        picture.export(tempPath);
        String pixelData = String.join("\n", Files.readAllLines(tempPath));

        assertTrue(pixelData.endsWith(expectedPixelData));
    }

    @Test
    void noLinesLongerThan70InExportedString(@TempDir Path tempdir) throws IOException {
        Path tempPath = tempdir.resolve("test.mth");

        IPicture picture = new PPMPicture(10, 2);
        IColour aColour = new GammaColour(1, 0.8, 0.6);

        for (int i = 0; i < 10; ++i) {
            picture.write(i, 0, aColour);
            picture.write(i, 1, aColour);
        }

        String expectedPreLineBreak = "255 228 198 255 228 198 255 228 198 255 228 198 255 228 198 255 228";
        String expectedPostLineBreak = "198 255 228 198 255 228 198 255 228 198 255 228 198";

        picture.export(tempPath);
        String pixelData = String.join("\n", Files.readAllLines(tempPath));

        assertTrue(pixelData.endsWith(expectedPreLineBreak + "\n" + expectedPostLineBreak),
                "Exporting should correctly break lines longer than 70 characters."
        );
    }
}
