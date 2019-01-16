package io.raytracer.structures;

public class UnitColour extends Real3Tuple implements Colour  {
    private double red;
    private double green;
    private double blue;

    public UnitColour(double red, double green, double blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public double getRed() {
        return red;
    }

    @Override
    public double getGreen() {
        return green;
    }

    @Override
    public double getBlue() {
        return blue;
    }

    @Override
    public Colour add(Colour them) {
        return null;
    }

    @Override
    public Colour subtract(Colour them) {
        return null;
    }

    @Override
    public Colour multiply(double scalar) {
        return null;
    }

    @Override
    public Colour mix(Colour them) {
        return null;
    }
}
