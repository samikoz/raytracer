package io.raytracer.mathsy;

import java.util.Arrays;
import java.util.stream.IntStream;

class RealTuple implements Tuple {
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
    public int hashCode() {
        return Arrays.hashCode(coords);
    }

    @Override
    public boolean equals(Object them) {
        if (this.getClass() != them.getClass()) return false;

        Tuple themTuple = (Tuple) them;
        return (this.dim == themTuple.dim() && this.euclideanDistance(themTuple) < 1e-3);
    }

    double euclideanDistance(Tuple them) {
        assert this.dim == them.dim();

        return Math.sqrt(IntStream.range(0, dim).mapToDouble(i -> Math.pow(this.get(i) - them.get(i), 2)).sum());
    }

    RealTuple add(Tuple them) {
        assert this.dim == them.dim();

        return new RealTuple(IntStream.range(0, dim).mapToDouble(i -> this.get(i) + them.get(i)).toArray());
    }

    RealTuple subtract(Tuple them) {
        assert this.dim == them.dim();

        return new RealTuple(IntStream.range(0, dim).mapToDouble(i -> this.get(i) - them.get(i)).toArray());
    }

    @Override
    public RealTuple multiply(double scalar) {
        return new RealTuple(IntStream.range(0, dim).mapToDouble(i -> this.get(i)*scalar).toArray());
    }

    @Override
    public RealTuple negate() {
        return this.multiply(-1);
    }
}
