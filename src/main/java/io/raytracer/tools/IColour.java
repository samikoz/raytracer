package io.raytracer.tools;


public interface IColour {
    double getRed();
    double getGreen();
    double getBlue();

    double getBrightness();

    IColour multiply(double scalar);

    IColour add(IColour them);
    default IColour subtract(IColour them) {
        return add(them.multiply(-1));
    }

    IColour mix(IColour them);

    default IColour interpolate(IColour them, double factor) {
        factor = Math.max(Math.min(1, factor), 0);
        return this.add(them.subtract(this).multiply(factor));
    }

    String export();
}
