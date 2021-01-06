package io.raytracer.light;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.Transformation;
import io.raytracer.geometry.Vector;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RayImpl implements Ray {
    @NonNull private final Point origin;
    @NonNull private final Vector direction;

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
