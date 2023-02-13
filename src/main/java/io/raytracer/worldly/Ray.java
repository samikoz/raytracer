package io.raytracer.worldly;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.IVector;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Ray implements IRay {
    @Getter @NonNull private final IPoint origin;
    @Getter @NonNull private final IVector direction;

    @Override
    public IPoint getPosition(double parameter) {
        return origin.add(direction.multiply(parameter));
    }

    @Override
    public IRay getTransformed(ITransform t) {
        return new Ray(this.origin.transform(t), this.direction.transform(t));
    }
}
