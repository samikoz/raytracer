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
        MaterialPoint illuminated = new MaterialPoint(
                this.object,
                intersectionPoint,
                surfaceNormal,
                this.ray.direction().reflect(surfaceNormal),
                this.ray.direction().negate(),
                false
        );
        if (illuminated.normalVector.dot(illuminated.eyeVector) < 0) {
            illuminated.normalVector = illuminated.normalVector.negate();
        }
        return illuminated;
    }
}
