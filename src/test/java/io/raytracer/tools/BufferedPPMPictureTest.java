package io.raytracer.tools;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BufferedPPMPictureTest {
    @Test
    void defaultCanvasColour(@TempDir Path tempDir) throws IOException {
        IPicture picture = new BufferedPPMPicture(2, 3, tempDir, PPMPicture::new);

        IColour defaultCanvasColour = picture.read(1, 1);
        IColour black = new LinearColour(0, 0, 0);

        assertEquals(defaultCanvasColour, black, "Default canvas colour should be black");
    }

    @Test
    void writeAndReadFromBufferedCanvas(@TempDir Path tempDir) throws IOException {
        IColour first = new LinearColour(0.5, 0.2, 0.3);
        IColour second = new LinearColour(0, 0, 0.4);
        BufferedPPMPicture picture = new BufferedPPMPicture(10, 20, tempDir, 2, PPMPicture::new);

        picture.write(new Pixel(0, 19), first);
        picture.write(new Pixel(2, 0), second);

        picture.loadPersisted();
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

        IPicture picture = new BufferedPPMPicture(5, 3, tempdir, PPMPicture::new);
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

        IPicture picture = new BufferedPPMPicture(5, 3, tempdir, PPMPicture::new);
        picture.write(new Pixel(0, 0), new LinearColour(1, 0, 0));
        picture.write(new Pixel(2, 1), new LinearColour(0, 0.5, 0));
        picture.write(new Pixel(4, 2), new LinearColour(-0.5, 0, 1));

        String expectedFirstRowData = "255 0 0 0 0 0 0 0 0 0 0 0 0 0 0\n";
        String expectedSecondRowData = "0 0 0 0 0 0 0 128 0 0 0 0 0 0 0\n";
        String expectedThirdRowData = "0 0 0 0 0 0 0 0 0 0 0 0 0 0 255";
        String expectedPixelData = expectedFirstRowData + expectedSecondRowData + expectedThirdRowData;
        picture.export(tempPath);
        String pixelData = String.join("\n", Files.readAllLines(tempPath));

        assertTrue(pixelData.endsWith(expectedPixelData));
    }

    @Test
    void getBlankPixelsReturnsUnpersistedPixels(@TempDir Path tempdir) throws IOException {
        IPicture firstPicture = new BufferedPPMPicture(2, 2, tempdir, 2, PPMPicture::new);
        firstPicture.write(new Pixel(0, 0), new LinearColour(0.1, 0.2, 0.3));
        firstPicture.write(new Pixel(1, 0), new LinearColour(0.2, 0.1, 0.3));
        firstPicture.write(new Pixel(0, 1), new LinearColour(0.3, 0.2, 0.1));
        IPicture secondPicture = new BufferedPPMPicture(2, 2, tempdir, 2, PPMPicture::new);
        Set<Pixel> unpersistedPixels = secondPicture.getBlankPixels().collect(Collectors.toSet());

        assertEquals(new HashSet<>(Arrays.asList(new Pixel(0, 1), new Pixel(1, 1))), unpersistedPixels);
    }

    @Test
    void persistedPixelsAccessibleAcrossBufferedPictures(@TempDir Path tempdir) throws IOException {
        IPicture firstPicture = new BufferedPPMPicture(2, 2, tempdir, 2, PPMPicture::new);
        firstPicture.write(new Pixel(0, 0), new LinearColour(0.1, 0.2, 0.3));
        firstPicture.write(new Pixel(1, 0), new LinearColour(0.2, 0.1, 0.3));
        firstPicture.write(new Pixel(0, 1), new LinearColour(0.3, 0.2, 0.1));
        BufferedPPMPicture secondPicture = new BufferedPPMPicture(2, 2, tempdir, 2, PPMPicture::new);
        secondPicture.loadPersisted();

        assertEquals(new LinearColour(0.2, 0.1, 0.3), secondPicture.read(1, 0));
        assertEquals(new LinearColour(0, 0, 0), secondPicture.read(0, 1));
    }
}