package io.raytracer.mechanics;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Vector;

import java.util.Optional;
import java.util.Random;
import java.util.function.Function;


public class Recasters {
    private static final Random randGen = new Random();

    public static Function<RayHit, Optional<IRay>> diffuse = hit -> {
        IPoint outerNormalCentre = hit.point.add(hit.normalVector.normalise());
        IVector recastDirection = outerNormalCentre.add(Recasters.getRandomInUnitSphere()).subtract(hit.point);
        return Optional.of(hit.ray.recast(hit.offsetAbove, recastDirection));
    };

    public static Function<Double, Function<RayHit, Optional<IRay>>> fuzzilyReflective = fuzziness -> hit -> {
        IVector reflectionDirection = hit.ray.getDirection().reflect(hit.normalVector);
        IVector fuzzyDirection = reflectionDirection.add(Recasters.getRandomInUnitSphere().multiply(fuzziness));
        if (fuzzyDirection.dot(hit.normalVector) > 0) {
            return Optional.of(hit.ray.recast(hit.offsetAbove, fuzzyDirection));
        }
        else {
            return Optional.empty();
        }
    };

    public static Function<RayHit, Optional<IRay>> refractive = hit -> {
        double refractedRatio = hit.refractiveIndexFrom / hit.refractiveIndexTo;
        double cosIncident = hit.eyeVector.dot(hit.normalVector);
        double sinRefractedSquared = Math.pow(refractedRatio, 2)*(1 - Math.pow(cosIncident, 2));
        if (sinRefractedSquared <= 1 && Recasters.getReflectance(cosIncident, refractedRatio) < Recasters.randGen.nextDouble()) {
            double cosRefracted = Math.sqrt(1 - sinRefractedSquared);
            IVector refractedDirection = hit.normalVector.multiply(refractedRatio * cosIncident - cosRefracted)
                    .subtract(hit.eyeVector.multiply(refractedRatio));
            return Optional.of(hit.ray.recast(hit.offsetBelow, refractedDirection));
        }
        else {
            return Recasters.fuzzilyReflective.apply(0.0).apply(hit);
        }
    };

    private static IVector getRandomInUnitSphere() {
        while (true) {
            Random randGen = new Random();
            IVector rand = new Vector(2*randGen.nextDouble()-1, 2*randGen.nextDouble()-1, 2*randGen.nextDouble()-1);
            if (rand.norm() >= 1) {
                continue;
            }
            return rand;
        }
    }

    private static double getReflectance(double cosI, double refRatio) {
        double r = Math.pow(((1 - refRatio) / (1 + refRatio)), 2);
        return r + (1-r)*Math.pow(1-cosI, 5);
    }
}
