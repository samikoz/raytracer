package io.raytracer.shapes.operators;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.mechanics.BBox;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import io.raytracer.shapes.Shape;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;


@RequiredArgsConstructor
public abstract class Operator extends Shape {
    protected Shape left, right;

    abstract protected boolean filterIntersection(Intersection inter);

    @Override
    protected Intersection[] getLocalIntersections(IRay ray, double tmin, double tmax) {
        Intersection[] leftIntersections = this.left.intersect(ray, tmin, tmax);
        Intersection[] rightIntersections = this.right.intersect(ray, tmin, tmax);
        Intersection[] combined = Arrays.copyOf(this.left.intersect(ray, tmin, tmax), leftIntersections.length + rightIntersections.length);
        System.arraycopy(rightIntersections, 0, combined, leftIntersections.length, rightIntersections.length);
        return Arrays.stream(combined).sorted().filter(this::filterIntersection).toArray(Intersection[]::new);
    }

    @Override
    protected IVector localNormalAt(IPoint point, double u, double v) {
        return null;
    }

    @Override
    protected Optional<BBox> getLocalBoundingBox() {
        return Optional.empty();
    }
}
