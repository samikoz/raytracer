package io.raytracer.demos;

import io.raytracer.tools.IPicture;
import io.raytracer.tools.PPMPicture;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class EyeAverage {
    public static void main(String[] args) throws IOException {
        String filename = "eye_averaged.ppm";

        PPMPicture leftPicture = PPMPicture.load(new File("prison_left.ppm"));
        PPMPicture rightPicture = PPMPicture.load(new File("prison_right.ppm"));
        List<IPicture> pictures = new ArrayList<>(2);
        pictures.add(leftPicture);
        pictures.add(rightPicture);

        PPMPicture averaged = new PPMPicture(leftPicture.getWidth(), leftPicture.getHeight());
        averaged.average(pictures);
        averaged.export(new PrintWriter(new FileWriter(filename)));
    }
}