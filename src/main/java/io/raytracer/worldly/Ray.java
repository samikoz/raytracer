package io.raytracer.worldly;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.IVector;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Arrays;

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
        Ray reflectedRay = new Ray(point.offsetAbove, point.reflectionVector);
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

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        Ray themRay = (Ray) them;
        return (
            this.origin.equals(themRay.origin) && this.direction.equals(themRay.direction) &&
            this.reflectionDepth == themRay.reflectionDepth
        );
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new int[] { this.origin.hashCode(), this.direction.hashCode(), this.reflectionDepth });
    }
}
