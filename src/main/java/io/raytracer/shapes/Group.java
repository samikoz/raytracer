package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.mechanics.IRay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Group extends Shape {
    private final List<Shape> children;

    Group() {
        super();
        this.children = new ArrayList<>();
    }

    public void add(Shape shape) {
        this.children.add(shape);
        shape.setParent(this);
    }

    @Override
    protected double[] getLocalIntersectionPositions(IRay ray) {
        return new double[0];
    }

    @Override
    protected IVector normalLocally(IPoint point) {
        throw new UnsupportedOperationException("normal cannot be requested from a group");
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        Group themGroup = (Group) them;
        return super.equals(them) && this.children.equals(themGroup.children);
    }

    @Override
    public int hashCode() {
        int childrenHash = Arrays.hashCode(this.children.stream().mapToInt(Shape::hashCode).toArray());
        return Arrays.hashCode(new int[] { childrenHash, super.hashCode() });
    }
}
