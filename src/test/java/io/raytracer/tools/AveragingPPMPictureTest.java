package io.raytracer.tools;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class AveragingPPMPictureTest {
    @Test
    void correctPPMExportPixelData(@TempDir Path tempdir) throws IOException {
        Path tempPath = tempdir.resolve("test.mth");

        IPicture picture = new AveragingPPMPicture(5, 3, 2);
        picture.write(new Pixel(0, 0), new LinearColour(1, 0, 0));
        picture.write(new Pixel(2, 1), new LinearColour(0, 0.5, 0));
        picture.write(new Pixel(4, 2), new LinearColour(-0.5, 0, 1));

        String expectedFirstRowData = "128 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n";
        String expectedSecondRowData = "0 0 0 0 0 0 0 64 0 0 0 0 0 0 0\n";
        String expectedThirdRowData = "0 0 0 0 0 0 0 0 0 0 0 0 0 0 128";
        String expectedPixelData = expectedFirstRowData + expectedSecondRowData + expectedThirdRowData;
        picture.export(tempPath);
        String pixelData = String.join("\n", Files.readAllLines(tempPath));

        assertTrue(pixelData.endsWith(expectedPixelData));
    }

}