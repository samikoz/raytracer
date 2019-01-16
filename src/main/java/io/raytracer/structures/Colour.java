package io.raytracer.structures;

public interface Colour {
    double getRed();
    double getGreen();
    double getBlue();

    Colour add(Colour them);
    Colour subtract(Colour them);
    Colour multiply(double scalar);
    Colour mix(Colour them);
}
