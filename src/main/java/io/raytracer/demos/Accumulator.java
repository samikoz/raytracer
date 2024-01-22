package io.raytracer.demos;

import io.raytracer.tools.AveragingPPMPicture;
import io.raytracer.tools.BufferedPPMPicture;
import io.raytracer.tools.IColour;
import io.raytracer.tools.IPicture;
import io.raytracer.tools.PPMPicture;
import io.raytracer.tools.Pixel;

import java.io.IOException;
import java.nio.file.Paths;

public class Accumulator {
    public static void main(String[] args) throws IOException {
        int size = 1080;
        int rayCount = 500;
        String filenameTemplate = "./outputs/trash/em05_%02d.ppm";
        String finalFilenameTemplate = "./outputs/tori/em/em05_%02d.ppm";
        String bufferDirTemplate = "./buffs/em05buff/";
        int bufferCount = 2;

        IPicture sumPicture = new PPMPicture(size, size);
        int i = 0;
        while (true) {
            DemoSetup setup = DemoSetup.builder()
                    .rayCount(rayCount)
                    .xSize(size)
                    .ySize(size)
                    .filename(String.format(filenameTemplate, i))
                    .bufferDir(String.format(bufferDirTemplate, i))
                    .bufferFileCount(bufferCount)
                    .build();
            Tori renderer = new Tori(setup);
            renderer.render();

            BufferedPPMPicture justExecuted = new BufferedPPMPicture(
                size, size, Paths.get(String.format(bufferDirTemplate + "buff_%02d", i)), size*size/bufferCount,
                PPMPicture::new);
            justExecuted.loadPersisted();
            IPicture accumulatedPicture = new AveragingPPMPicture(size, size, (i+1)*rayCount);
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    IColour justColoured = justExecuted.read(x, y);
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
