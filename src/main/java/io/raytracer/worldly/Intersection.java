package io.raytracer.worldly;

import io.raytracer.geometry.IPoint;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.NonNull;

@ToString
@AllArgsConstructor
public class Intersection {
    public IRay ray;
    public double rayParameter;
    @NonNull public IDrawable object;

    MaterialPoint getMaterialPoint() {
        IPoint intersectionPoint = this.ray.position(this.rayParameter);
        MaterialPoint illuminated = new MaterialPoint(
                this.object,
                intersectionPoint,
                this.object.normal(intersectionPoint),
                this.ray.getDirection().negate(),
                false
        );
        if (illuminated.normalVector.dot(illuminated.eyeVector) < 0) {
            illuminated.normalVector = illuminated.normalVector.negate();
        }
        return illuminated;
    }
}
