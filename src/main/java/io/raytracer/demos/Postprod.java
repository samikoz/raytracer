package io.raytracer.demos;

import io.raytracer.tools.PPMPicture;

import java.io.IOException;
import java.nio.file.Paths;

public class Postprod {
    public static void main(String[] args) throws IOException {
        PPMPicture base = PPMPicture.load(Paths.get("stairsHorizontalMerged.ppm"));
        PPMPicture embedder = PPMPicture.load(Paths.get("stairsHorizontal3.ppm"));

        base.embed(embedder, (x, y) -> y >= 810 && y < 1080-156);
        base.export(Paths.get("stairsHorizontalMerged2.ppm"));
    }
}
