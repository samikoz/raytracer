package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.IRay;
import lombok.NonNull;

public class Triangle extends Shape {
    public IPoint v1, v2, v3;
    public IVector e1, e2;
    public IVector normal;

    private static final double tolerance = 1e-3;

    Triangle(IPoint v1, IPoint v2, IPoint v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.e1 = v2.subtract(v1);
        this.e2 = v3.subtract(v1);
        this.normal = this.e2.cross(this.e1).normalise();
    }

    Triangle(IPoint v1, IPoint v2, IPoint v3, @NonNull Material material) {
        super(material);
    }

    @Override
    protected double[] getLocalIntersectionPositions(IRay ray) {
        IVector dirCrossE2 = ray.getDirection().cross(this.e2);
        double det = dirCrossE2.dot(this.e1);
        if (Math.abs(det) < Triangle.tolerance) {
            return new double[] {};
        }
        double f = 1.0 / det;
        IVector p1ToOrigin = ray.getOrigin().subtract(this.v1);
        double u = f*p1ToOrigin.dot(dirCrossE2);
        if (u < 0 || u > 1) {
            return new double[] {};
        }
        IVector originCrossE1 = p1ToOrigin.cross(this.e1);
        double v = f*ray.getDirection().dot(originCrossE1);
        if (v < 0 || (u+v) > 1) {
            return new double[] {};
        }
        return new double[] { f*this.e2.dot(originCrossE1) };
    }

    @Override
    protected IVector normalLocally(IPoint point) {
        return this.normal;
    }
}