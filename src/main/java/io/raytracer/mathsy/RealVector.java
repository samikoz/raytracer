package io.raytracer.mathsy;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class RealVector extends RealTuple implements Vector {
    public RealVector(double... coordinates) {
        super(coordinates);
    }

    RealVector(RealTuple them) {
        this(IntStream.range(0, them.dim()).mapToDouble(them::get).toArray());
    }

    @Override
    public RealVector multiply(double scalar) {
        return new RealVector(super.multiply(scalar));
    }

    @Override
    public RealVector negate() {
        return new RealVector(super.negate());
    }

    @Override
    public RealVector add(Vector them) {
        return new RealVector(super.add(them));
    }

    @Override
    public RealVector subtract(Vector them) {
        return new RealVector(super.subtract(them));
    }

    @Override
    public double distance(Vector them) {
        return euclideanDistance(them);
    }

    @Override
    public double norm() {
        return distance(new RealVector(DoubleStream.generate(() -> 0).limit(dim()).toArray()));
    }

    @Override
    public RealVector normalise() {
        return new RealVector(this.multiply(1/this.norm()));
    }

    @Override
    public double dot(Vector them) {
        assert this.dim() == them.dim();

        return IntStream.range(0, dim()).mapToDouble(i -> this.get(i)*them.get(i)).sum();
    }
}
