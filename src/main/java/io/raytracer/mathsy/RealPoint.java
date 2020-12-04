package io.raytracer.mathsy;

import java.util.stream.IntStream;

public class RealPoint extends TupleImpl implements Point {
    public RealPoint(double... coordinates) {
        super(coordinates);
    }

    private RealPoint(TupleImpl them) {
        this(them.toArray());
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
    public RealVector subtract(Point toSubtract) {
        Vector toMoveBack = new RealVector(
            IntStream.range(0, toSubtract.dim()).mapToDouble(toSubtract::get).toArray()
        );
        Point movedBack = subtract(toMoveBack);
        return new RealVector(IntStream.range(0, movedBack.dim()).mapToDouble(movedBack::get).toArray());
    }
}
