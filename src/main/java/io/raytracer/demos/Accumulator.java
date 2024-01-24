package io.raytracer.demos;

import io.raytracer.tools.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

public class Accumulator {
    public static void main(String[] args) throws IOException {
        int size = 1080;
        int rayCount = 500;
        String finalFilenameTemplate = "./outputs/tori/em/em07_%02d.ppm";
        String bufferDirTemplate = "./buffs/em07aggBuff/";

        int renderIndex = Accumulator.getInitialBufferIndex(Paths.get(bufferDirTemplate));
        IPicture sumPicture;
        if (renderIndex == 0) {
            sumPicture = new PPMPicture(size, size);
        } else {
            sumPicture = new BufferedPPMPicture(size, size, Paths.get(String.format(bufferDirTemplate + "buff_%02d", renderIndex)), PPMPicture::new).getUnbuffered();
        }
        while (true) {
            renderIndex++;
            DemoSetup setup = DemoSetup.builder()
                    .rayCount(rayCount)
                    .xSize(size)
                    .ySize(size)
                    .build();
            Tori renderer = new Tori(setup);
            IPicture rollingRender = renderer.render();

            int finalI = renderIndex;
            IPicture accumulatedPicture = new BufferedPPMPicture(size, size, Paths.get(String.format(bufferDirTemplate + "buff_%02d", renderIndex)), (x, y) -> new PPMPicture(x, y, finalI*rayCount));
            sumPicture.add(rollingRender);
            accumulatedPicture.add(sumPicture);
            accumulatedPicture.export(Paths.get(String.format(finalFilenameTemplate, renderIndex)));
        }
    }

    private static int getInitialBufferIndex(Path bufferDir) throws IOException {
        Files.createDirectories(bufferDir);
        try (Stream<Path> stream = Files.list(bufferDir)) {
            return stream.map(path -> path.getFileName().toString()).map(s -> s.substring(s.length() - 2)).mapToInt(Integer::parseInt).max().orElse(0);
        }
    }
}
