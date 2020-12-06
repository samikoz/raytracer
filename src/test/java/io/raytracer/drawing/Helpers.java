package io.raytracer.drawing;

class ColourComparator {
    static String compareColourComponents(Colour expected, Colour actual) {
        return expected.getRed() + " should be " + actual.getRed() +
                ", " + expected.getGreen() + " should be " + actual.getGreen() +
                ", " + expected.getBlue() + " should be " + actual.getBlue();
    }
}
