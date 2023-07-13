package io.raytracer.mechanics;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.shapes.Shape;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;


@ToString
public class RayHit extends Intersection {
    public final IPoint point;
    public final IPoint offsetAbove;
    public final IPoint offsetBelow;
    public final IVector eyeVector;
    public final IVector normalVector;
    public final double refractiveIndexFrom;
    public final double refractiveIndexTo;
    public final double reflectance;
    public boolean shadowed;

    private RayHit(Intersection i, double refractiveFrom, double refractiveTo) {
        super(i.ray, i.rayParameter, i.object);

        IPoint intersectionPoint = this.ray.getPosition(this.rayParameter);
        IVector surfaceNormal = this.object.normal(intersectionPoint);
        IVector eyeVector = this.ray.getDirection().negate();
        if (surfaceNormal.dot(eyeVector) < 0) {
            surfaceNormal = surfaceNormal.negate();
        }
        this.point = intersectionPoint;
        this.eyeVector = eyeVector.normalise();
        this.normalVector = surfaceNormal.normalise();
        this.offsetAbove = this.point.add(this.normalVector.multiply(1e-6));
        this.offsetBelow = this.point.subtract(this.normalVector.multiply(1e-6));
        this.refractiveIndexFrom = refractiveFrom;
        this.refractiveIndexTo = refractiveTo;
        this.shadowed = false;

        this.reflectance = this.getReflectance();
    }

    static Optional<RayHit> fromIntersections(Collection<Intersection> inters) {
        Optional<Intersection> firstPositive = inters.stream().filter(i -> i.rayParameter > 0).min(Comparator.comparingDouble(i -> i.rayParameter));
        if (!firstPositive.isPresent()) { return Optional.empty(); }
        Intersection hitIntersection = firstPositive.get();
        double[] refractiveIndices = RayHit.findRefractiveIndices(inters, hitIntersection);
        return Optional.of(new RayHit(hitIntersection, refractiveIndices[0], refractiveIndices[1]));
    }

    static double[] findRefractiveIndices(Collection<Intersection> inters, Intersection hitIntersection) {
        ArrayList<Shape> containers = new ArrayList<>();
        double[] refractiveIndices = new double[2];
        for (Intersection currentIntersection : inters) {
            if (currentIntersection.equals(hitIntersection)) {
                if (containers.isEmpty()) {
                    refractiveIndices[0] = 1.0;
                } else {
                    refractiveIndices[0] = containers.get(containers.size() - 1).getMaterial().refractiveIndex;
                }
            }

            if (containers.contains(currentIntersection.object)) {
                containers.remove(currentIntersection.object);
            } else {
                containers.add(currentIntersection.object);
            }

            if (currentIntersection.equals(hitIntersection)) {
                if (containers.isEmpty()) {
                    refractiveIndices[1] = 1.0;
                } else {
                    refractiveIndices[1] = containers.get(containers.size() - 1).getMaterial().refractiveIndex;
                }
                break;
            }
        }
        return refractiveIndices;
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

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        RayHit themHit = (RayHit) them;
        return (this.ray == themHit.ray && (int)this.rayParameter*1000 == (int)themHit.rayParameter*1000);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new int[] { this.ray.hashCode(), (int)this.rayParameter*1000 });
    }
}
