package io.raytracer.mathsy;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class RealVector extends RealTuple implements Vector {
    public RealVector(double... coordinates) {
        super(coordinates);
    }

    private RealVector(RealTuple them) {
        this(them.toArray());
    }

    @Override
    public RealVector multiply(double scalar) {
        return new RealVector(super.multiply(scalar));
    }

    @Override
    public RealVector add(Vector them) {
        return new RealVector(super.add(them));
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
