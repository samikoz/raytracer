package io.raytracer.tools;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Line;

import java.util.List;

public class Curve implements ICurve {
    private final int pointCount;
    private final List<IPoint> points;
    private final List<IVector> normals;

    private static final double delta = 1e-4;

    public Curve(List<IPoint> points, List<IVector> normals) {
        this.pointCount = points.size();
        this.points = points;
        this.normals = normals;
    }

    public IPoint pointAt(double t) {
        double scaled = t*(pointCount-1);
        double remainder = scaled - (int)scaled;
        IPoint firstPoint = this.points.get((int)scaled);
        if (remainder < delta) {
            return firstPoint;
        }
        IPoint secondPoint = this.points.get((int)scaled + 1);
        return new Line(firstPoint, secondPoint).pointAt(remainder);
    }

    public IVector normalAt(double t) {
        double scaled = t*(pointCount-1);
        double remainder = scaled - (int)scaled;
        IVector firstVector = this.normals.get((int)scaled);
        if (remainder < delta) {
            return firstVector;
        }
        IVector secondVector = this.normals.get((int)scaled + 1);
        return firstVector.add(secondVector.subtract(firstVector).multiply(remainder));
    }
}
