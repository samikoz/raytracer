package io.raytracer.drawing;

import io.raytracer.mathsy.Tuple;

public interface Colour extends Tuple {
    double getRed();
    double getGreen();
    double getBlue();

    Colour add(Colour them);
    Colour subtract(Colour them);
    Colour multiply(double scalar);
    Colour mix(Colour them);
}
