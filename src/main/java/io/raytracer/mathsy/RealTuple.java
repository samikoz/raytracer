package io.raytracer.mathsy;

import java.util.Arrays;
import java.util.stream.IntStream;

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

        return Math.sqrt(IntStream.range(0, dim).mapToDouble(i -> Math.pow(this.get(i) - them.get(i), 2)).sum());
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

        return new RealTuple(IntStream.range(0, dim).mapToDouble(i -> this.get(i) + them.get(i)).toArray());
    }

    @Override
    public RealTuple add(Vector them) {
        return add((RealTuple) them);
    }

    private RealTuple subtract(RealTuple them) {
        assert this.dim == them.dim;

        return new RealTuple(IntStream.range(0, dim).mapToDouble(i -> this.get(i) - them.get(i)).toArray());
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
        return new RealTuple(IntStream.range(0, dim).mapToDouble(i -> this.get(i)*scalar).toArray());
    }

    @Override
    public double dot(Vector them) {
        assert this.dim() == them.dim();

        return IntStream.range(0, dim).mapToDouble(i -> this.get(i)*them.get(i)).sum();
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
