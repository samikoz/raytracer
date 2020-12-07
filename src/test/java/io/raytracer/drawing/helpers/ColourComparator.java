package io.raytracer.drawing.helpers;

import io.raytracer.drawing.Colour;

public class ColourComparator {
    public static String compareColourComponents(Colour expected, Colour actual) {
        return expected.getRed() + " should be " + actual.getRed() +
                ", " + expected.getGreen() + " should be " + actual.getGreen() +
                ", " + expected.getBlue() + " should be " + actual.getBlue();
    }
}
