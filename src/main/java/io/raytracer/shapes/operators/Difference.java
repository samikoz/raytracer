package io.raytracer.shapes.operators;

import io.raytracer.mechanics.BBox;
import io.raytracer.shapes.Shape;

public class Difference extends Operator {

    public Difference(Shape main, Shape subtractor) {
        super(main, subtractor);
    }

    @Override
    protected boolean isIntersectionAdmitted(boolean leftHit, boolean insideLeft, boolean insideRight) {
        return (leftHit && !insideRight) || (!leftHit && insideLeft);
    }

    @Override
    protected BBox getLocalBoundingBox() {
        return this.left.getBoundingBox();
    }
}
