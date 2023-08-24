package io.raytracer.demos;

import io.raytracer.tools.LinearColour;
import io.raytracer.tools.PPMPicture;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Postprod {
    public static void main(String[] args) throws IOException {
        int size = 1080;
        int trimSize = 768;
        String postprodFile = "postPillars.ppm";

        PPMPicture pillars = PPMPicture.load(new File("pillarsSq.ppm"));
        PPMPicture postPillars = new PPMPicture(size, size);

        IntStream.range(0, size).mapToObj(y ->
                IntStream.range(0, size).mapToObj(x ->
                        Stream.of(Arrays.asList(x, y))
                ).flatMap(x -> x)
        ).flatMap(y -> y).forEach(pixelLocation -> {
            if (pixelLocation.get(1) < (size - trimSize)/2 || pixelLocation.get(1) >= (trimSize + size)/2  ) {
                postPillars.write(pixelLocation.get(0), pixelLocation.get(1), new LinearColour(1, 1, 1));
            }
            else {
                postPillars.write(pixelLocation.get(0), pixelLocation.get(1), pillars.read(pixelLocation.get(0), pixelLocation.get(1)));
            }
        });

        PrintWriter writer = new PrintWriter(new FileWriter(postprodFile));
        postPillars.export(writer);
    }
}
