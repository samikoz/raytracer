package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.BBox;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import lombok.NonNull;

import java.util.Arrays;
import java.util.Optional;

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
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.e1 = v2.subtract(v1);
        this.e2 = v3.subtract(v1);
        this.normal = this.e2.cross(this.e1).normalise();
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new double[] {v1.hashCode(), v2.hashCode(), v3.hashCode(),});
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        Triangle themTriangle = (Triangle) them;
        return this.v1.equals(themTriangle.v1) && this.v2.equals(themTriangle.v2) && this.v3.equals(themTriangle.v3);
    }

    @Override
    public void setTransform(ITransform transform) {
        this.v1 = transform.act(this.v1);
        this.v2 = transform.act(this.v2);
        this.v3 = transform.act(this.v3);
        this.e1 = v2.subtract(v1);
        this.e2 = v3.subtract(v1);
        this.normal = this.e2.cross(this.e1).normalise();
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

    @Override
    protected Optional<BBox> getLocalBoundingBox() {
        return Optional.of(new BBox(
            new Point(
                Math.min(Math.min(this.v1.get(0), this.v2.get(0)), this.v3.get(0)),
                Math.min(Math.min(this.v1.get(1), this.v2.get(1)), this.v3.get(1)),
                Math.min(Math.min(this.v1.get(2), this.v2.get(2)), this.v3.get(2))
            ),
            new Point(
                Math.max(Math.max(this.v1.get(0), this.v2.get(0)), this.v3.get(0)),
                Math.max(Math.max(this.v1.get(1), this.v2.get(1)), this.v3.get(1)),
                Math.max(Math.max(this.v1.get(2), this.v2.get(2)), this.v3.get(2))
            )
        ));
    }
}
