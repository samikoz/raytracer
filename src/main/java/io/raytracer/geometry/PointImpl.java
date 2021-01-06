package io.raytracer.geometry;

import lombok.NonNull;
import lombok.ToString;

import java.util.stream.IntStream;

@ToString
public class PointImpl extends TupleImpl implements Point {
    public PointImpl(double... coordinates) {
        super(coordinates);
    }

    private PointImpl(TupleImpl them) {
        this(them.toArray());
    }

    @Override
    public double distance(Point them) {
        return euclideanDistance(them);
    }

    @Override
    public PointImpl multiply(double scalar) {
        return new PointImpl(super.multiply(scalar));
    }

    @Override
    public PointImpl add(Vector them) {
        return new PointImpl(super.add(them));
    }

    @Override
    public VectorImpl subtract(@NonNull Point toSubtract) {
        Vector toMoveBack = new VectorImpl(
                IntStream.range(0, toSubtract.dim()).mapToDouble(toSubtract::get).toArray()
        );
        Point movedBack = subtract(toMoveBack);
        return new VectorImpl(IntStream.range(0, movedBack.dim()).mapToDouble(movedBack::get).toArray());
    }

    @Override
    public Point transform(Transformation t) {
        return t.act(this);
    }
}
