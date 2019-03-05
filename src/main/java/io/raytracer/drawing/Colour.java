package io.raytracer.drawing;

public interface Colour {
    double getRed();
    double getGreen();
    double getBlue();

    Colour multiply(double scalar);

    Colour add(Colour them);
    default Colour subtract(Colour them) {
        return add(them.multiply(-1));
    }

    Colour mix(Colour them);

    String exportNormalised();
}
