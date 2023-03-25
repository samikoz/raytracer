package io.raytracer.mechanics;

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
    @Getter @Setter private int recast;

    public Ray(@NonNull IPoint origin, @NonNull IVector direction) {
        this.origin = origin;
        this.direction = direction;
        this.recast = 0;
    }

    public static Ray reflectFrom(RayHit hitpoint) {
        Ray reflectedRay = new Ray(hitpoint.offsetAbove, hitpoint.reflectionVector);
        reflectedRay.recast = hitpoint.ray.getRecast() + 1;
        return reflectedRay;
    }

    public static Ray refractFrom(RayHit hitpoint) {
        double refractedRatio = hitpoint.refractiveIndexFrom / hitpoint.refractiveIndexTo;
        double cosIncident = hitpoint.eyeVector.dot(hitpoint.normalVector);
        double sinRefractedSquared = Math.pow(refractedRatio, 2)*(1 - Math.pow(cosIncident, 2));
        assert sinRefractedSquared <= 1;
        double cosRefracted = Math.sqrt(1 - sinRefractedSquared);
        IVector refractedDirection = hitpoint.normalVector.multiply(refractedRatio*cosIncident - cosRefracted)
                .subtract(hitpoint.eyeVector.multiply(refractedRatio));
        Ray refractedRay = new Ray(hitpoint.offsetBelow, refractedDirection);
        refractedRay.recast = hitpoint.ray.getRecast() + 1;
        return refractedRay;
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
            this.recast == themRay.recast
        );
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new int[] { this.origin.hashCode(), this.direction.hashCode(), this.recast});
    }
}
