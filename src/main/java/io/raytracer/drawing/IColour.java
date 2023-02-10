package io.raytracer.drawing;

public interface IColour {
    double getRed();
    double getGreen();
    double getBlue();

    IColour multiply(double scalar);

    IColour add(IColour them);
    default IColour subtract(IColour them) {
        return add(them.multiply(-1));
    }

    IColour mix(IColour them);

    IColour interpolate(IColour them, double factor);

    String exportNormalised();
}
