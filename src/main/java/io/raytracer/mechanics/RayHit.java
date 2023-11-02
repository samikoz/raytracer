package io.raytracer.mechanics;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@ToString
public class RayHit extends Intersection {
    public final IPoint point;
    public final IPoint offsetAbove;
    public final IPoint offsetBelow;
    public final IVector eyeVector;
    public final IVector normalVector;
    public final double refractiveRatio;
    public boolean shadowed;

    private RayHit(Intersection i) {
        super(i.object, i.ray, i.rayParameter);

        IPoint intersectionPoint = this.ray.getPosition(this.rayParameter);
        IVector surfaceNormal = this.object.normal(i);
        IVector eyeVector = this.ray.getDirection().negate();
        if (surfaceNormal.dot(eyeVector) < 0) {
            surfaceNormal = surfaceNormal.negate();
            this.refractiveRatio = 1/i.object.getMaterial().refractiveIndex;
        } else {
            this.refractiveRatio = object.getMaterial().refractiveIndex;
        }
        this.point = intersectionPoint;
        this.eyeVector = eyeVector.normalise();
        this.normalVector = surfaceNormal.normalise();
        this.offsetAbove = this.point.add(this.normalVector.multiply(1e-6));
        this.offsetBelow = this.point.subtract(this.normalVector.multiply(1e-6));
        this.shadowed = false;
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

    public static Optional<RayHit> fromIntersections(List<Intersection> inters) {
        if (inters.isEmpty()) {
            return Optional.empty();
        }
        inters.sort(Intersection::compareTo);
        return Optional.of(new RayHit(inters.get(0)));
    }

    /*
    static double[] findRefractiveIndices(Intersection[] inters, Intersection hitIntersection) {
        //respects overlapping objects when finding proper refractive indices
        //if ever gonna use it, make sure no shortcuts taken when computing intersection list e.g. in Group
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
    */
}
