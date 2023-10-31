package io.raytracer.mechanics;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.IVector;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Arrays;

@Getter
public class Ray implements IRay {
    private final IPoint origin;
    private final IVector direction;
    @Setter private int recast;

    public Ray(@NonNull IPoint origin, @NonNull IVector direction) {
        this.origin = origin;
        this.direction = direction;
        this.recast = 0;
    }

    public IRay recast(IPoint from, IVector direction) {
        Ray recastRay = new Ray(from, direction);
        recastRay.recast = this.getRecast() + 1;
        return recastRay;
    }

    @Override
    public IPoint getPosition(double parameter) {
        return origin.add(direction.multiply(parameter));
    }

    @Override
    public IRay getTransformed(ITransform t) {
        if (t.isId()) {
            return this;
        }
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
