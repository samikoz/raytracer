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
        IPicture sumPicture = new PPMPicture(size, size);
        for (int i = 0; i < 16; i++) {
            Stairs.main(new String[]{Integer.toString(i)});
            BufferedPPMPicture justExecuted = new BufferedPPMPicture(
                size, size, Paths.get(String.format("./buffs/refcstairsmod/buffmod_%s/", i)), size*size/2,
                PPMPicture::new);
            justExecuted.loadPersisted();
            IPicture accumulatedPicture = new AveragingPPMPicture(size, size, (i+1)*500);
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    IColour justColoured = justExecuted.read(x, y);
                    IColour summed = sumPicture.read(x, y).add(justColoured);
                    sumPicture.write(new Pixel(x, y), summed);
                    accumulatedPicture.write(new Pixel(x, y), summed);
                }
            }
            accumulatedPicture.export(Paths.get(String.format("outputs/centralMod_%d.ppm", i)));
        }
    }
}
