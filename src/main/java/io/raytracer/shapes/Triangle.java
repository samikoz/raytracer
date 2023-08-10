package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import lombok.NonNull;

public class Triangle extends Shape {
    public IPoint v1, v2, v3;
    public IVector e1, e2;
    public IVector normal;

    private static final double tolerance = 1e-3;

    public Triangle(IPoint v1, IPoint v2, IPoint v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.e1 = v2.subtract(v1);
        this.e2 = v3.subtract(v1);
        this.normal = this.e2.cross(this.e1).normalise();
    }

    public Triangle(IPoint v1, IPoint v2, IPoint v3, @NonNull Material material) {
        super(material);
    }

    @Override
    protected Intersection[] getLocalIntersections(IRay ray, double tmin, double tmax) {
        IVector dirCrossE2 = ray.getDirection().cross(this.e2);
        double det = dirCrossE2.dot(this.e1);
        if (Math.abs(det) < Triangle.tolerance) {
            return new Intersection[] {};
        }
        double f = 1.0 / det;
        IVector p1ToOrigin = ray.getOrigin().subtract(this.v1);
        double u = f*p1ToOrigin.dot(dirCrossE2);
        if (u < 0 || u > 1) {
            return new Intersection[] {};
        }
        IVector originCrossE1 = p1ToOrigin.cross(this.e1);
        double v = f*ray.getDirection().dot(originCrossE1);
        if (v < 0 || (u+v) > 1) {
            return new Intersection[] {};
        }
        double intersection = f*this.e2.dot(originCrossE1);
        if (intersection < tmin || intersection > tmax) {
            return new Intersection[] {};
        }
        return new Intersection[] { new Intersection(this , ray, f*this.e2.dot(originCrossE1), u, v) };
    }

    @Override
    protected IVector localNormalAt(IPoint point, double u, double v) {
        return this.normal;
    }
}
