package io.raytracer.geometry;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.function.IntToDoubleFunction;

class Tuple implements ITuple {
    private final int dim;
    private final double[] coords;

    public Tuple(double... coordinates) {
        dim = coordinates.length;
        coords = coordinates;
    }

    private int[] roundCoordinates(ITuple t) {
        return IntStream.range(0, dim).map(i -> (int)(t.get(i)*1000)).toArray();
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.roundCoordinates(this));
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        ITuple themTuple = (ITuple) them;
        return (this.dim == themTuple.dim() && Arrays.equals(this.roundCoordinates(this), this.roundCoordinates(themTuple)));
    }

    @Override
    public int dim() {
        return dim;
    }

    @Override
    public double get(int coordinate) {
        assert coordinate < dim;
        return coords[coordinate];
    }

    double[] toArray() {
        return IntStream.range(0, dim).mapToDouble(this::get).toArray();
    }

    Tuple applyCoordinatewise(IntToDoubleFunction action) {
        return new Tuple(IntStream.range(0, dim).mapToDouble(action).toArray());
    }

    double euclideanDistance(ITuple them) {
        assert this.dim == them.dim();

        return Math.sqrt(IntStream.range(0, dim).mapToDouble(i -> Math.pow(this.get(i) - them.get(i), 2)).sum());
    }

    Tuple add(ITuple them) {
        assert this.dim == them.dim();

        return applyCoordinatewise(i -> this.get(i) + them.get(i));
    }

    public Tuple multiply(double scalar) {
        return applyCoordinatewise(i -> get(i) * scalar);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + Arrays.toString(this.coords);
    }
}
