package io.raytracer.geometry;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class VectorImpl extends TupleImpl implements Vector {
    public VectorImpl(double... coordinates) {
        super(coordinates);
    }

    private VectorImpl(TupleImpl them) {
        this(them.toArray());
    }

    @Override
    public VectorImpl multiply(double scalar) {
        return new VectorImpl(super.multiply(scalar));
    }

    @Override
    public VectorImpl add(Vector them) {
        return new VectorImpl(super.add(them));
    }

    @Override
    public double distance(Vector them) {
        return euclideanDistance(them);
    }

    @Override
    public double norm() {
        return distance(new VectorImpl(DoubleStream.generate(() -> 0).limit(dim()).toArray()));
    }

    @Override
    public VectorImpl normalise() {
        return new VectorImpl(this.multiply(1 / this.norm()));
    }

    @Override
    public double dot(Vector them) {
        assert this.dim() == them.dim();

        return IntStream.range(0, dim()).mapToDouble(i -> this.get(i) * them.get(i)).sum();
    }

    @Override
    public Vector transform(Transformation t) {
        return t.act(this);
    }
}
