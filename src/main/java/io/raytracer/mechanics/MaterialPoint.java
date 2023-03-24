package io.raytracer.mechanics;

import io.raytracer.drawables.Drawable;
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
    public final double reflectance;
    public boolean shadowed;

    public MaterialPoint(@NonNull Drawable object, @NonNull IPoint point, @NonNull IRay incomingRay,
                         @NonNull IVector eyeVector, @NonNull IVector normal,
                         double refractiveIndexFrom, double refractiveIndexTo, boolean shadowed) {

        this.object = object;
        this.point = point;
        this.normalVector = normal.normalise();
        this.offsetAbove = this.point.add(this.normalVector.multiply(1e-6));
        this.offsetBelow = this.point.subtract(this.normalVector.multiply(1e-6));
        this.inRay = incomingRay;
        this.eyeVector = eyeVector.normalise();
        this.reflectionVector = this.inRay.getDirection().reflect(normalVector);
        this.refractiveIndexFrom = refractiveIndexFrom;
        this.refractiveIndexTo = refractiveIndexTo;
        this.shadowed = shadowed;

        this.reflectance = this.getReflectance();
    }

    private double getReflectance() {
        double cos = this.eyeVector.dot(this.normalVector);
        if (this.refractiveIndexFrom > this.refractiveIndexTo) {
            double refractiveRatio = this.refractiveIndexFrom / this.refractiveIndexTo;
            double sinRefractedSquared = Math.pow(refractiveRatio, 2)*(1 - Math.pow(cos, 2));
            if (sinRefractedSquared > 1) {
                return 1;
            }
            cos = Math.sqrt(1 - sinRefractedSquared);
        }
        double r = Math.pow(((this.refractiveIndexFrom - this.refractiveIndexTo) / (this.refractiveIndexFrom + this.refractiveIndexTo)), 2);
        return r + (1-r)*Math.pow(1-cos, 5);
    }
}
