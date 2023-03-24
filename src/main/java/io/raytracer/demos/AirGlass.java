package io.raytracer.demos;

import java.io.IOException;

public class AirGlass {
    public static void main(String[] args) throws IOException {
        int secondsCount = 3;
        int framesPerSecond = 32;
        double angleDiff = 2*Math.PI / (secondsCount*framesPerSecond);
        for (int i = 0; i < secondsCount*framesPerSecond; i++) {
            double angle = i*angleDiff;
            AirGlassSnapshot.render(angle, String.format("airglass_%02d", i));
            System.out.printf("rendered %02d out of %02d%n", i, secondsCount*framesPerSecond);
        }
    }
}
