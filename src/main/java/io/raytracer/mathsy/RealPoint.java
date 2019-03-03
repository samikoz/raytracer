package io.raytracer.mathsy;

import java.util.stream.IntStream;

public class RealPoint extends RealTuple implements Point {
    public RealPoint(double... coordinates) {
        super(coordinates);
    }

    private RealPoint(RealTuple them) {
        this(IntStream.range(0, them.dim()).mapToDouble(them::get).toArray());
    }

    @Override
    public double distance(Point them) {
        return euclideanDistance(them);
    }

    @Override
    public RealPoint multiply(double scalar) {
        return new RealPoint(super.multiply(scalar));
    }

    @Override
    public RealPoint add(Vector them) {
        return new RealPoint(super.add(them));
    }

    @Override
    public RealPoint subtract(Vector them) {
        return new RealPoint(super.subtract(them));
    }
    @Override
    public RealVector subtract(Point them) {
        return new RealVector(super.subtract(them));
    }

}
