package io.raytracer.drawing;

import java.util.Arrays;

public class Unit3TupleColour implements Colour {
    private double red;
    private double green;
    private double blue;

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
    public Colour subtract(Colour them) {
        return new Unit3TupleColour(
            this.getRed() - them.getRed(),
            this.getGreen() - them.getGreen(),
            this.getBlue() - them.getBlue()
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
