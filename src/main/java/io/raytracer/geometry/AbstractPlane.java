package io.raytracer.geometry;

import io.raytracer.mechanics.BBox;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.TextureParameters;
import io.raytracer.shapes.Shape;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;

@Getter
public class AbstractPlane extends Shape implements IPlane {
    private IPoint point;
    private IVector normal;

    private static final double tolerance = 1e-5;

    public AbstractPlane(IVector normal, IPoint point) {
        this.normal = normal;
        this.point = point;
    }

    public AbstractPlane(IVector normal) {
        this.normal = normal;
        this.point = new Point(0 ,0, 0);
    }

    @Override
    public void setTransform(ITransform transform) {
        throw new UnsupportedOperationException("use AbstractPlane .rotate(), .translate() methods");
    }

    @Override
    public boolean doesContain(IPoint point) {
        return Math.abs(new Vector(point).dot(this.normal) - new Vector(this.point).dot(this.normal)) < AbstractPlane.tolerance;
    }

    @Override
    public IPlane translate(IVector translation) {
        this.point = this.point.add(translation);
        return this;
    }

    @Override
    public IPlane rotate_x(double angle) {
        this.normal = ThreeTransform.rotation_x(angle).act(this.normal);
        return this;
    }

    @Override
    public IPlane rotate_y(double angle) {
        this.normal = ThreeTransform.rotation_y(angle).act(this.normal);
        return this;
    }

    @Override
    public IPlane rotate_z(double angle) {
        this.normal = ThreeTransform.rotation_z(angle).act(this.normal);
        return this;
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
        throw new UnsupportedOperationException("unsupported now!");
    }

    @Override
    public IVector normal(Intersection i) {
        return this.normal;
    }

    @Override
    protected IVector localNormalAt(IPoint point, TextureParameters p) {
        throw new UnsupportedOperationException("normal overridden level higher");
    }
}
