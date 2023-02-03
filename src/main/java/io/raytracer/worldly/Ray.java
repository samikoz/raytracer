package io.raytracer.worldly;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.IVector;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Ray implements IRay {
    @NonNull private final IPoint origin;
    @NonNull private final IVector direction;

    @Override
    public IVector getDirection() {
        return this.direction;
    }

    @Override
    public IPoint getOrigin() {
        return this.origin;
    }

    @Override
    public IPoint position(double parameter) {
        return origin.add(direction.multiply(parameter));
    }

    @Override
    public IRay transform(ITransform t) {
        return new Ray(this.origin.transform(t), this.direction.transform(t));
    }
}