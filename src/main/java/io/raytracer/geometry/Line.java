package io.raytracer.geometry;

import lombok.Getter;

import java.util.Arrays;

@Getter
public class Line implements ILine {
    private final IPoint origin;
    private final IVector direction;

    public Line(IPoint from, IVector direction) {
        this.origin = from;
        this.direction = direction;
    }

    public Line(IPoint from, IPoint to) {
        this.origin = from;
        this.direction = to.subtract(from);
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        Line themLine = (Line) them;
        return this.getOrigin().equals(themLine.getOrigin()) && this.getDirection().equals(themLine.getDirection());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new int[] { this.getOrigin().hashCode(), this.getDirection().hashCode()});
    }

    @Override
    public double intersect(IPlane plane) {
        IVector n = plane.getNormal();
        return -n.dot(this.getOrigin().subtract(plane.getPoint()))/(n.dot(this.getDirection()));
    }

    @Override
    public IPoint pointAt(double t) {
        return this.origin.add(this.direction.multiply(t));
    }

    @Override
    public IPoint closestTo(IPoint p) {
        double t = -this.direction.dot(new Vector(p).add(new Vector(this.origin)))/(this.direction.dot(this.direction));
        return this.pointAt(t);
    }
}
