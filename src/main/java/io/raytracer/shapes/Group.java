package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Group extends Shape {
    public final List<Shape> children;

    public Group() {
        super();
        this.children = new ArrayList<>();
    }

    public Group add(Shape shape) {
        this.children.add(shape);
        shape.setParent(this);
        return this;
    }

    @Override
    public Intersection[] intersect(IRay ray) {
        IRay transformedRay = ray.getTransformed(this.getInverseTransform());
        return this.children.stream().map(child -> child.intersect(transformedRay))
            .flatMap(Arrays::stream).sorted().map(i -> ray.intersect(i.object, i.rayParameter)).toArray(Intersection[]::new);
    }

    @Override
    protected double[] getLocalIntersectionPositions(IRay ray, double tmin, double tmax) {
        throw new UnsupportedOperationException("a group doesn't have own local intersections");
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
