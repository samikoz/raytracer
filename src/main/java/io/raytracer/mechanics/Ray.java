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

    public Ray reflectFrom(IPoint point, IVector normalVector) {
        IVector reflectionDirection = this.getDirection().reflect(normalVector);
        Ray reflectedRay = new Ray(point, reflectionDirection);
        reflectedRay.recast = this.getRecast() + 1;
        return reflectedRay;
    }

    public Ray refractOn(RefractionPoint refpoint) {
        double refractedRatio = refpoint.refractiveIndexFrom / refpoint.refractiveIndexTo;
        double cosIncident = refpoint.eyeVector.dot(refpoint.normalVector);
        double sinRefractedSquared = Math.pow(refractedRatio, 2)*(1 - Math.pow(cosIncident, 2));
        assert sinRefractedSquared <= 1;
        double cosRefracted = Math.sqrt(1 - sinRefractedSquared);
        IVector refractedDirection = refpoint.normalVector.multiply(refractedRatio*cosIncident - cosRefracted)
                .subtract(refpoint.eyeVector.multiply(refractedRatio));
        Ray refractedRay = new Ray(refpoint.point, refractedDirection);
        refractedRay.recast = this.getRecast() + 1;
        return refractedRay;
    }

    @Override
    public IPoint getPosition(double parameter) {
        return origin.add(direction.multiply(parameter));
    }

    @Override
    public IRay getTransformed(ITransform t) {
        Ray transformed = new Ray(this.origin.transform(t), this.direction.transform(t));
        transformed.recast = this.recast;
        return transformed;
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

    @Override
    public String toString() {
        return String.format("Ray[%s, %s]", this.origin, this.direction);
    }
}
