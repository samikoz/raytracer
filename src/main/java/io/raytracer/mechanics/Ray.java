package io.raytracer.mechanics;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Line;
import lombok.NonNull;

public class Ray extends Line implements IRay {
    public Ray(@NonNull IPoint origin, @NonNull IVector direction) {
        super(origin, direction);
    }

    @Override
    public IRay getTransformed(ITransform t) {
        return new Ray(this.getOrigin().transform(t), this.getDirection().transform(t));
    }

    @Override
    public String toString() {
        return String.format("Ray[%s, %s]", this.getOrigin(), this.getDirection());
    }
}
