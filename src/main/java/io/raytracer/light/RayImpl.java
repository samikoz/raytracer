package io.raytracer.light;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.Transformation;
import io.raytracer.geometry.Vector;

public class RayImpl implements Ray{
    private final Point origin;
    private final Vector direction;

    public RayImpl(Point origin, Vector direction) {
        this.origin = origin;
        this.direction = direction;
    }

    @Override
    public Vector getDirection() {
        return this.direction;
    }

    @Override
    public Point getOrigin() {
        return this.origin;
    }

    @Override
    public Point position(double time) {
        return origin.add(direction.multiply(time));
    }

    @Override
    public IntersectionList intersect(Drawable object) {
        return object.intersect(this);
    }

    @Override
    public Ray transform(Transformation t) {
        return new RayImpl(this.origin.transform(t), this.direction.transform(t));
    }
}