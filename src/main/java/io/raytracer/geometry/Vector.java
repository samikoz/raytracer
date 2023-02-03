package io.raytracer.geometry;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class Vector extends Tuple implements IVector {
    public Vector(double... coordinates) {
        super(coordinates);
    }

    private Vector(Tuple them) {
        this(them.toArray());
    }

    @Override
    public Vector multiply(double scalar) {
        return new Vector(super.multiply(scalar));
    }

    @Override
    public Vector add(IVector them) {
        return new Vector(super.add(them));
    }

    @Override
    public double distance(IVector them) {
        return euclideanDistance(them);
    }

    @Override
    public double norm() {
        return distance(new Vector(DoubleStream.generate(() -> 0).limit(dim()).toArray()));
    }

    @Override
    public Vector normalise() {
        return new Vector(this.multiply(1 / this.norm()));
    }

    @Override
    public double dot(IVector them) {
        assert this.dim() == them.dim();

        return IntStream.range(0, dim()).mapToDouble(i -> this.get(i) * them.get(i)).sum();
    }

    @Override
    public Vector cross(IVector them) {
        assert this.dim() == 3;
        assert them.dim() == 3;

        return new Vector(
                this.get(1) * them.get(2) - this.get(2) * them.get(1),
                this.get(2) * them.get(0) - this.get(0) * them.get(2),
                this.get(0) * them.get(1) - this.get(1) * them.get(0)
        );
    }

    @Override
    public IVector reflect(IVector reflector) {
        return this.subtract(reflector.multiply(2 * this.dot(reflector)));
    }

    @Override
    public IVector transform(ITransform t) {
        return t.act(this);
    }
}
