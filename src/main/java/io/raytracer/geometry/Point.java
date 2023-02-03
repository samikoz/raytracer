package io.raytracer.geometry;

import lombok.NonNull;

import java.util.stream.IntStream;

public class Point extends Tuple implements IPoint {
    public Point(double... coordinates) {
        super(coordinates);
    }

    private Point(Tuple them) {
        this(them.toArray());
    }

    @Override
    public double distance(IPoint them) {
        return euclideanDistance(them);
    }

    @Override
    public Point multiply(double scalar) {
        return new Point(super.multiply(scalar));
    }

    @Override
    public Point add(IVector them) {
        return new Point(super.add(them));
    }

    @Override
    public IVector subtract(@NonNull IPoint toSubtract) {
        IVector toMoveBack = new Vector(
                IntStream.range(0, toSubtract.dim()).mapToDouble(toSubtract::get).toArray()
        );
        IPoint movedBack = subtract(toMoveBack);
        return new Vector(IntStream.range(0, movedBack.dim()).mapToDouble(movedBack::get).toArray());
    }

    @Override
    public IPoint transform(ITransform t) {
        return t.act(this);
    }
}
