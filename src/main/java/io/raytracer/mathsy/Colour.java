package io.raytracer.mathsy;

public interface Colour extends Tuple {
    double getRed();
    double getGreen();
    double getBlue();

    Colour add(Colour them);
    Colour subtract(Colour them);
    Colour multiply(double scalar);
    Colour mix(Colour them);
}
