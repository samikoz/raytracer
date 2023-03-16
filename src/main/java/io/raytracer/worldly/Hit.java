package io.raytracer.worldly;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.worldly.drawables.Drawable;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;


@ToString
public class Hit extends Intersection {
    private final double refractiveFrom;
    private final double refractiveTo;
    private Hit(Intersection i, double refractiveFrom, double refractiveTo) {
        super(i.ray, i.rayParameter, i.object);
        this.refractiveFrom = refractiveFrom;
        this.refractiveTo = refractiveTo;
    }

    static Optional<Hit> fromIntersections(Intersection[] inters) {
        Optional<Intersection> firstPositive = Arrays.stream(inters).filter(i -> i.rayParameter > 0).min(Comparator.comparingDouble(i -> i.rayParameter));
        if (!firstPositive.isPresent()) { return Optional.empty(); }
        Intersection hitIntersection = firstPositive.get();
        double[] refractiveIndices = Hit.findRefractiveIndices(inters, hitIntersection);
        return Optional.of(new Hit(hitIntersection, refractiveIndices[0], refractiveIndices[1]));
    }

    static double[] findRefractiveIndices(Intersection[] inters, Intersection hitIntersection) {
        ArrayList<Drawable> containers = new ArrayList<>();
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

    MaterialPoint getMaterialPoint() {
        IPoint intersectionPoint = this.ray.getPosition(this.rayParameter);
        IVector surfaceNormal = this.object.normal(intersectionPoint);
        IVector eyeVector = this.ray.getDirection().negate();
        if (surfaceNormal.dot(eyeVector) < 0) {
            surfaceNormal = surfaceNormal.negate();
        }
        return new MaterialPoint(
            this.object,
            intersectionPoint,
            this.ray,
            eyeVector,
            surfaceNormal, this.refractiveFrom,
            this.refractiveTo,
            false
        );
    }

    @Override
    public boolean equals(Object them) {
        if (them == null || this.getClass() != them.getClass()) return false;

        Hit themHit = (Hit) them;
        return (this.ray == themHit.ray && (int)this.rayParameter*1000 == (int)themHit.rayParameter*1000);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new int[] { this.ray.hashCode(), (int)this.rayParameter*1000 });
    }
}
