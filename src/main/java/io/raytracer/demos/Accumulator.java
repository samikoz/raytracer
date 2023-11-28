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
        IPicture sumPicture = new PPMPicture(1080, 1080);
        for (int i = 0; i < 10; i++) {
            Stairs.main(new String[] {Integer.toString(i)});
            BufferedPPMPicture justExecuted = new BufferedPPMPicture(
                1080, 1080, Paths.get(String.format("./buffs/refcstairs/buffcentral_%s/", i)), 1080*1080/5,
                PPMPicture::new);
            justExecuted.loadPersisted();
            IPicture accumulatedPicture = new AveragingPPMPicture(1080, 1080, (i+1)*500);
            for (int y = 0; y < 1080; y++) {
                for (int x = 0; x < 1080; x++) {
                    IColour justColoured = justExecuted.read(x, y);
                    IColour summed = sumPicture.read(x, y).add(justColoured);
                    sumPicture.write(new Pixel(x, y), summed);
                    accumulatedPicture.write(new Pixel(x, y), summed);
                }
            }
            accumulatedPicture.export(Paths.get(String.format("outputs/refCentral_%d.ppm", i)));
        }
    }
}
