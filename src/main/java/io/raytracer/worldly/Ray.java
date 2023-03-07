package io.raytracer.worldly;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.IVector;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class Ray implements IRay {
    @Getter private final IPoint origin;
    @Getter private final IVector direction;
    @Getter @Setter private int reflectionDepth;

    public Ray(@NonNull IPoint origin, @NonNull IVector direction) {
        this.origin = origin;
        this.direction = direction;
        this.reflectionDepth = 0;
    }

    public static Ray reflectFrom(MaterialPoint point) {
        Ray reflectedRay = new Ray(point.offsetPoint, point.reflectionVector);
        reflectedRay.reflectionDepth = point.inRay.getReflectionDepth() + 1;
        return reflectedRay;
    }

    @Override
    public IPoint getPosition(double parameter) {
        return origin.add(direction.multiply(parameter));
    }

    @Override
    public IRay getTransformed(ITransform t) {
        return new Ray(this.origin.transform(t), this.direction.transform(t));
    }
}
