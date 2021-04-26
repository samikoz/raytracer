package io.raytracer.light;

import io.raytracer.drawing.Drawable;
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
    public IlluminatedPoint getIlluminatedPoint(Intersection intersection) {
        Point intersectionPoint = position(intersection.time);
        IlluminatedPoint illuminated = new IlluminatedPoint(
                intersection.time,
                intersection.object,
                intersectionPoint,
                intersection.object.normal(intersectionPoint),
                this.direction.negate(),
                false
        );
        if (illuminated.normalVector.dot(illuminated.eyeVector) < 0) {
            illuminated.inside = true;
            illuminated.normalVector = illuminated.normalVector.negate();
        }
        return illuminated;
    }

    @Override
    public Ray transform(Transformation t) {
        return new RayImpl(this.origin.transform(t), this.direction.transform(t));
    }
}
