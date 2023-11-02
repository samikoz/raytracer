package io.raytracer.shapes.operators;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Interval;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.RayHit;
import io.raytracer.mechanics.TextureParameters;
import io.raytracer.shapes.Shape;
import io.raytracer.tools.IColour;

import java.util.ArrayList;
import java.util.List;


public abstract class Operator extends Shape {
    protected final Shape left, right;

    protected Operator(Shape left, Shape right) {
        super();
        this.left = left;
        this.right = right;
    }

    private Intersection[] filterIntersection(Iterable<Intersection> inters) {
        boolean insideLeft = false;
        boolean insideRight = false;
        List<Intersection> filtered = new ArrayList<>();

        for (Intersection i : inters) {
            boolean leftHit = this.left.doesInclude(i.object);

            if (this.isIntersectionAdmitted(leftHit, insideLeft, insideRight)) {
                filtered.add(i);
            }

            if (leftHit) {
                insideLeft = !insideLeft;
            }
            else {
                insideRight = !insideRight;
            }
        }
        return filtered.toArray(new Intersection[] {});
    }

    @Override
    public boolean doesInclude(Shape them) {
        return this.left.doesInclude(them) || this.right.doesInclude(them);
    }

    abstract protected boolean isIntersectionAdmitted(boolean leftHit, boolean insideLeft, boolean insideRight);

    @Override
    protected Intersection[] getLocalIntersections(IRay ray, Interval rayDomain) {
        List<Intersection> leftIntersections = this.left.intersect(ray, rayDomain);
        List<Intersection> rightIntersections = this.right.intersect(ray, rayDomain);
        leftIntersections.addAll(rightIntersections);
        leftIntersections.sort(Intersection::compareTo);
        return filterIntersection(leftIntersections);
    }

    @Override
    public IColour getIntrinsicColour(RayHit hit) {
        return hit.object.getIntrinsicColour(hit);
    }

    @Override
    protected IVector localNormalAt(IPoint point, TextureParameters p) {
        throw new UnsupportedOperationException("no need to compute normal on an operator!");
    }
}
