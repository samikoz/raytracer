package io.raytracer.worldly;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.worldly.drawables.Drawable;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.NonNull;

@ToString
@AllArgsConstructor
public class Intersection {
    public IRay ray;
    public double rayParameter;
    @NonNull public Drawable object;

    MaterialPoint getMaterialPoint() {
        IPoint intersectionPoint = this.ray.position(this.rayParameter);
        IVector surfaceNormal = this.object.normal(intersectionPoint);
        IVector eyeVector = this.ray.direction().negate();
        if (surfaceNormal.dot(eyeVector) < 0) {
            surfaceNormal = surfaceNormal.negate();
        }
        return new MaterialPoint(
            this.object,
            intersectionPoint,
            surfaceNormal,
            this.ray.direction().reflect(surfaceNormal),
            eyeVector,
            false
        );
    }
}
