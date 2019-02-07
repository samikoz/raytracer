package io.raytracer.mathsy;

import java.util.Arrays;

class RealTuple implements Vector, Point {
    private int dim;
    private double[] coords;

    public RealTuple(double... coordinates) {
        dim = coordinates.length;
        coords = coordinates;
    }

    public int dim() {
        return dim;
    }

    @Override
    public double get(int coordinate) {
        assert coordinate < dim;
        return coords[coordinate];
    }

    @Override
    public double[] toArray() {
        return coords;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(coords);
    }

    @Override
    public boolean equals(Object them) {
        if (this.getClass() != them.getClass()) return false;

        RealTuple themTuple = (RealTuple) them;
        return (this.dim == themTuple.dim && this.distance(themTuple) < 1e-3);
    }

    private double distance(RealTuple them) {
        assert this.dim == them.dim;

        double coordsDifferenceSquares = 0;
        for (int coordIndex = 0; coordIndex < dim; coordIndex++) {
            coordsDifferenceSquares += Math.pow(this.get(coordIndex) - them.get(coordIndex), 2);
        }
        return Math.sqrt(coordsDifferenceSquares);
    }

    @Override
    public double distance(Vector them) {
        return distance((RealTuple) them);
    }

    @Override
    public double distance(Point them) {
        return distance((RealTuple) them);
    }

    @Override
    public double norm() {
        return distance(new RealTuple(0,0,0));
    }

    @Override
    public RealTuple normalise() {
        return this.multiply(1/this.norm());
    }

    private RealTuple add(RealTuple them) {
        assert this.dim == them.dim;

        double[] coordSum = new double[dim];
        for (int coordIndex = 0; coordIndex < dim; coordIndex++) {
            coordSum[coordIndex] += this.get(coordIndex) + them.get(coordIndex);
        }
        return new RealTuple(coordSum);
    }

    @Override
    public RealTuple add(Vector them) {
        return add((RealTuple) them);
    }

    private RealTuple subtract(RealTuple them) {
        assert this.dim == them.dim;

        double[] coordDiff = new double[dim];
        for (int coordIndex = 0; coordIndex < dim; coordIndex++) {
            coordDiff[coordIndex] += this.get(coordIndex) - them.get(coordIndex);
        }
        return new RealTuple(coordDiff);
    }

    @Override
    public RealTuple subtract(Vector them) {
        return subtract((RealTuple) them);
    }

    @Override
    public RealTuple subtract(Point them) {
        return subtract((RealTuple) them);
    }

    @Override
    public RealTuple negate() {
        double[] zeroes = new double[dim];
        return new RealTuple(zeroes).subtract(this);
    }

    public RealTuple multiply(double scalar) {
        double[] coordMult = new double[dim];
        for (int coordIndex = 0; coordIndex < dim; coordIndex++) {
            coordMult[coordIndex] += this.get(coordIndex)*scalar;
        }
        return new RealTuple(coordMult);
    }

    @Override
    public double dot(Vector them) {
        assert this.dim() == them.dim();

        double product = 0;
        for (int coordIndex = 0; coordIndex < dim; coordIndex++) {
            product += this.get(coordIndex)*them.get(coordIndex);
        }
        return product;
    }

    @Override
    public RealTuple cross(Vector them) {
        assert this.dim() == them.dim();
        assert dim == 3;

        return new RealTuple(
            this.get(1)*them.get(2) - this.get(2)*them.get(1),
            this.get(2)*them.get(0) - this.get(0)*them.get(2),
            this.get(0)*them.get(1) - this.get(1)*them.get(0)
        );
    }
}
