package io.raytracer.drawing;

import java.util.Arrays;

public class Unit3TupleColour implements Colour {
    private final double red;
    private final double green;
    private final double blue;

    final private static int exportScale = 255;

    public Unit3TupleColour(double red, double green, double blue) {
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

    private int normaliseComponent(double componentValue) {
        if (componentValue <= 0) {
            componentValue = 0;
        } else if (componentValue >= 1) {
            componentValue = 1;
        }
        return (int) Math.round(componentValue * exportScale);
    }

    @Override
    public String exportNormalised() {
        return Integer.toString(normaliseComponent(getRed())) + " " +
            Integer.toString(normaliseComponent(getGreen())) + " " +
            Integer.toString(normaliseComponent(getBlue()));
    }

    private double distance(Unit3TupleColour them) {
        return Math.sqrt(
            Math.pow(this.getRed() - them.getRed(), 2) +
            Math.pow(this.getGreen() - them.getGreen(), 2) +
            Math.pow(this.getBlue() - them.getBlue(), 2)
        );
    }

    @Override
    public int hashCode() {
        double[] coords = {this.getRed(), this.getGreen(), this.getBlue()};
        return Arrays.hashCode(coords);
    }

    @Override
    public boolean equals(Object them) {
        if (this.getClass() != them.getClass()) return false;

        Unit3TupleColour themColour = (Unit3TupleColour) them;
        return (this.distance(themColour) < 1e-3);
    }

    @Override
    public Unit3TupleColour add(Colour them) {
        return new Unit3TupleColour(
            this.getRed() + them.getRed(),
            this.getGreen() + them.getGreen(),
            this.getBlue() + them.getBlue()
        );
    }

    @Override
    public Colour multiply(double scalar) {
        return new Unit3TupleColour(
            scalar*this.getRed(),
            scalar*this.getGreen(),
            scalar*this.getBlue()
        );
    }

    @Override
    public Colour mix(Colour them) {
        return new Unit3TupleColour(
            this.getRed()*them.getRed(),
            this.getGreen()*them.getGreen(),
            this.getBlue()*them.getBlue()
        );
    }
}
