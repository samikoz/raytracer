package io.raytracer.shapes.operators;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.mechanics.BBox;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.RayHit;
import io.raytracer.shapes.Shape;
import io.raytracer.tools.IColour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
        return filtered.toArray(new Intersection[0]);
    }

    @Override
    public boolean doesInclude(Shape them) {
        return this.left.doesInclude(them) || this.right.doesInclude(them);
    }

    abstract protected boolean isIntersectionAdmitted(boolean leftHit, boolean insideLeft, boolean insideRight);

    @Override
    protected Intersection[] getLocalIntersections(IRay ray, double tmin, double tmax) {
        Intersection[] leftIntersections = this.left.getIntersections(ray, tmin, tmax).toArray(new Intersection[] {});
        Intersection[] rightIntersections = this.right.getIntersections(ray, tmin, tmax).toArray(new Intersection[] {});
        Intersection[] combined = Arrays.copyOf(leftIntersections, leftIntersections.length + rightIntersections.length);
        System.arraycopy(rightIntersections, 0, combined, leftIntersections.length, rightIntersections.length);
        List<Intersection> combinedSorted = Arrays.stream(combined).sorted().collect(Collectors.toList());
        return this.filterIntersection(combinedSorted);
    }

    @Override
    public IColour getIntrinsicColour(RayHit hit) {
        return hit.object.getIntrinsicColour(hit);
    }

    @Override
    protected IVector localNormalAt(IPoint point, double u, double v) {
        throw new UnsupportedOperationException("no need to compute normal on an operator!");
    }

    @Override
    protected Optional<BBox> getLocalBoundingBox() {
        return Optional.empty();
    }
}
