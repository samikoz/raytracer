package io.raytracer.shapes;

import io.raytracer.geometry.ILine;
import io.raytracer.geometry.IPlane;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Interval;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.BBox;
import io.raytracer.mechanics.FullSpace;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.TextureParameters;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;

@Getter
public class Plane extends Shape implements IPlane {
    private IPoint point;
    private IVector normal;

    private static final double tolerance = 1e-5;

    public Plane(IVector normal, IPoint point) {
        super();
        this.normal = normal;
        this.point = point;
    }

    public Plane(IVector normal) {
        super();
        this.normal = normal;
        this.point = new Point(0 ,0, 0);
    }

    public Plane(IVector normal, IPoint point, Material material) {
        super(material);
        this.normal = normal;
        this.point = point;
    }

    public Plane(IVector normal, Material material) {
        super(material);
        this.normal = normal;
        this.point = new Point(0, 0, 0);
    }

    @Override
    public void setTransform(ITransform transform) {
        this.normal = transform.act(this.normal);
        this.point = transform.act(this.point);
    }

    @Override
    public boolean doesContain(IPoint point) {
        return Math.abs(new Vector(point).dot(this.normal) - new Vector(this.point).dot(this.normal)) < Plane.tolerance;
    }

    @Override
    public IPoint reflect(IPoint p) {
        return p.subtract(this.normal.multiply(2*p.subtract(this.point).dot(this.normal)));
    }

    @Override
    public IPoint intersect(ILine line) {
        return line.pointAt(line.intersect(this));
    }

    @Override
    public ArrayList<Intersection> intersect(IRay ray, Interval rayDomain) {
        return new ArrayList<>(Collections.singletonList(new Intersection(this, ray, ray.intersect(this), new TextureParameters())));
    }

    @Override
    protected Intersection[] getLocalIntersections(IRay ray, Interval rayDomain) {
        throw new UnsupportedOperationException("intersect overridden level higher");
    }

    @Override
    protected BBox getLocalBoundingBox() {
        return new FullSpace();
    }

    @Override
    public IVector normal(Intersection i) {
        return this.normal;
    }

    @Override
    protected IVector localNormalAt(IPoint point, TextureParameters p) {
        throw new UnsupportedOperationException("normal overridden level higher");
    }

    @Override
    public String toString() {
        return String.format("Plane(%s,%s)", this.normal.toString(), this.point.toString());
    }
}
