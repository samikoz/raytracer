package io.raytracer.mechanics;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Line;
import lombok.NonNull;

import java.util.Arrays;

public class Ray extends Line implements IRay{
    public Ray(@NonNull IPoint origin, @NonNull IVector direction) {
        super(origin, direction);
    }

    @Override
    public IVector getDirection() {
        return this.direction;
    }

    @Override
    public IPoint getOrigin() {
        return this.origin;
    }

    @Override
    public IRay getTransformed(ITransform t) {
        return new Ray(this.origin.transform(t), this.direction.transform(t));
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        Ray themRay = (Ray) them;
        return this.origin.equals(themRay.origin) && this.direction.equals(themRay.direction);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new int[] { this.origin.hashCode(), this.direction.hashCode()});
    }

    @Override
    public String toString() {
        return String.format("Ray[%s, %s]", this.origin, this.direction);
    }
}
