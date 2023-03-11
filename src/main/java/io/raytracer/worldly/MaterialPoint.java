package io.raytracer.worldly;

import io.raytracer.worldly.drawables.Drawable;
import lombok.NonNull;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;

public class MaterialPoint {
    public final Drawable object;
    public final IPoint point;
    public final IPoint offsetAbove;
    public final IPoint offsetBelow;
    public final IRay inRay;
    public final IVector eyeVector;
    public final IVector normalVector;
    public final IVector reflectionVector;
    public final double refractiveIndexFrom;
    public final double refractiveIndexTo;
    public boolean shadowed;

    public MaterialPoint(@NonNull Drawable object, @NonNull IPoint point, @NonNull IRay incomingRay,
                         @NonNull IVector eyeVector, @NonNull IVector normal,
                         double refractiveIndexFrom, double refractiveIndexTo, boolean shadowed) {

        this.object = object;
        this.point = point;
        this.normalVector = normal;
        this.offsetAbove = this.point.add(this.normalVector.multiply(1e-6));
        this.offsetBelow = this.point.subtract(this.normalVector.multiply(1e-6));
        this.inRay = incomingRay;
        this.eyeVector = eyeVector;
        this.reflectionVector = this.inRay.getDirection().reflect(normalVector);
        this.refractiveIndexFrom = refractiveIndexFrom;
        this.refractiveIndexTo = refractiveIndexTo;
        this.shadowed = shadowed;
    }
}
