package io.raytracer.mechanics;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.shapes.Shape;
import lombok.NonNull;

public class DebugRay extends Ray {
    public DebugRay(@NonNull IPoint origin, @NonNull IVector direction) {
        super(origin, direction);
    }

    public static DebugRay fromRay(IRay ray) {
        DebugRay debugRay = new DebugRay(ray.getOrigin(), ray.getDirection());
        debugRay.setRecast(ray.getRecast());
        return debugRay;
    }

    @Override
    public Intersection intersect(Shape shape, double parameter) {
        Intersection i = super.intersect(shape, parameter);
        System.out.print(this.getRecastTabulation());
        System.out.printf("intersected %s at %s%n", i.object, i.rayParameter);
        return i;
    }

    @Override
    public IRay reflectFrom(IPoint point, IVector normalVector) {
        IRay reflectedRay = super.reflectFrom(point, normalVector);
        DebugRay debugRay = DebugRay.fromRay(reflectedRay);
        System.out.print(this.getRecastTabulation());
        System.out.printf("reflecting as %s%n", reflectedRay);
        return debugRay;
    }

    @Override
    public IRay refractOn(RefractionPoint refpoint) {
        IRay refractedRay = super.refractOn(refpoint);
        DebugRay debugRay = DebugRay.fromRay(refractedRay);
        System.out.print(this.getRecastTabulation());
        System.out.printf("refracting as %s%n", refractedRay);
        return debugRay;
    }

    private String getRecastTabulation() {
        return new String(new char[this.getRecast()]).replace("\0", "\t") + String.format("(%s)", this.getRecast());
    }
}
