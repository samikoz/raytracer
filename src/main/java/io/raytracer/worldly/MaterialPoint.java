package io.raytracer.worldly;

import io.raytracer.worldly.drawables.Drawable;
import lombok.Getter;
import lombok.NonNull;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;

public class MaterialPoint {
    public final Drawable object;
    public final IPoint point;
    public final IPoint offsetPoint;
    public final IVector normalVector;
    public final IVector reflectionVector;
    public final IVector eyeVector;
    public boolean shadowed;

    public MaterialPoint(@NonNull Drawable object, @NonNull IPoint point, @NonNull IVector normal,
                         @NonNull IVector reflection, @NonNull IVector eyeVector, @NonNull boolean shadowed) {

        this.object = object;
        this.point = point;
        this.normalVector = normal;
        this.offsetPoint = this.point.add(this.normalVector.multiply(1e-6));
        this.reflectionVector = reflection;
        this.eyeVector = eyeVector;
        this.shadowed = shadowed;
    }
}
