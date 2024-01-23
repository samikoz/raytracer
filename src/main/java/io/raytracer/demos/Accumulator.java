package io.raytracer.demos;

import io.raytracer.tools.IColour;
import io.raytracer.tools.IPicture;
import io.raytracer.tools.PPMPicture;
import io.raytracer.tools.Pixel;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

public class Accumulator {
    public static void main(String[] args) throws IOException {
        int size = 1080;
        int rayCount = 500;
        String finalFilenameTemplate = "./outputs/tori/em/em07_%02d.ppm";
        String bufferDirTemplate = "./buffs/em07buff/";
        int bufferCount = 2;

        IPicture sumPicture = new PPMPicture(size, size);

        int i = 1;
        while (true) {
            DemoSetup setup = DemoSetup.builder()
                    .rayCount(rayCount)
                    .xSize(size)
                    .ySize(size)
                    .bufferDir(String.format(bufferDirTemplate + "buff_%02d", i))
                    .bufferFileCount(bufferCount)
                    .build();
            Tori renderer = new Tori(setup);
            IPicture singleRendered = renderer.render();

            IPicture accumulatedPicture = new PPMPicture(size, size, i*rayCount);
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    IColour justColoured = singleRendered.read(x, y);
                    IColour summed = sumPicture.read(x, y).add(justColoured);
                    sumPicture.write(new Pixel(x, y), summed);
                    accumulatedPicture.write(new Pixel(x, y), summed);
                }
            }
            accumulatedPicture.export(Paths.get(String.format(finalFilenameTemplate, i)));
            i++;
        }
    }
}
