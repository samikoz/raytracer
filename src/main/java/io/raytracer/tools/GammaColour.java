package io.raytracer.tools;

import java.util.stream.Stream;

public class GammaColour extends LinearColour {

    public GammaColour(double red, double green, double blue) {
        super(red, green, blue);
    }

    public GammaColour(double greyscale) {
        super(greyscale, greyscale, greyscale);
    }

    private double gammaCorrect(double value) {
        return Math.sqrt(value);
    }

    @Override
    public String export() {
        return Stream.of(this.getRed(), this.getGreen(), this.getBlue())
            .mapToDouble(this::clamp).map(this::gammaCorrect).mapToInt(this::scale)
            .mapToObj(String::valueOf).reduce((c1, c2) -> c1 + " " + c2).get();
    }
}
