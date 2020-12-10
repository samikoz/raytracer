package io.raytracer.light;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;

public class RayImpl implements Ray{
    private final Point origin;
    private final Vector direction;

    public RayImpl(Point origin, Vector direction) {
        this.origin = origin;
        this.direction = direction;
    }

    @Override
    public Point position(double time) {
        return origin.add(direction.multiply(time));
    }
}
