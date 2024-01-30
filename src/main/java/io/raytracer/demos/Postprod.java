package io.raytracer.demos;

import io.raytracer.tools.PPMPicture;
import io.raytracer.tools.Pixel;

import java.io.IOException;
import java.nio.file.Paths;

public class Postprod {
    public static void main(String[] args) throws IOException {
        int size = 1080;
        PPMPicture left = PPMPicture.load(Paths.get("outputs/holes/.ppm"));
        PPMPicture right = PPMPicture.load(Paths.get("outputs/holes/right20.ppm"));
        PPMPicture mirrored = new PPMPicture(size, size);
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (x < size/2) {
                    mirrored.write(new Pixel(x, y), left.read(x, y));
                }
                else {
                    mirrored.write(new Pixel(x, y), right.read(x, y));
                }
            }
        }
        mirrored.export(Paths.get("outputs/holes/testNose03.ppm"));
    }
}
