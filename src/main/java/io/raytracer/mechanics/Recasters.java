package io.raytracer.mechanics;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Vector;

import java.util.Optional;
import java.util.Random;
import java.util.function.Function;


public class Recasters {
    private static final Random randGen = new Random();

    public static Function<RayHit, Optional<IRay>> isotropic = hit -> Optional.of(new Ray(hit.point, Recasters.getRandomInUnitSphere()));

    public static Function<RayHit, Optional<IRay>> diffuse = hit -> {
        IPoint outerNormalCentre = hit.point.add(hit.normalVector.normalise());
        IVector recastDirection = outerNormalCentre.add(Recasters.getRandomInUnitSphere()).subtract(hit.point);
        return Optional.of(new Ray(hit.offsetAbove, recastDirection));
    };

    //0 for no fuzziness
    public static Function<Double, Function<RayHit, Optional<IRay>>> fuzzilyReflective = fuzziness -> hit -> {
        IVector reflectionDirection = hit.ray.getDirection().reflect(hit.normalVector);
        IVector fuzzyDirection = reflectionDirection.add(Recasters.getRandomInUnitSphere().multiply(fuzziness));
        if (fuzzyDirection.dot(hit.normalVector) > 0) {
            return Optional.of(new Ray(hit.offsetAbove, fuzzyDirection));
        }
        else {
            return Optional.empty();
        }
    };

    public static Function<RayHit, Optional<IRay>> refractive = hit -> {
        double cosIncident = hit.eyeVector.dot(hit.normalVector);
        double sinRefractedSquared = Math.pow(hit.refractiveRatio, 2)*(1 - Math.pow(cosIncident, 2));
        if (sinRefractedSquared <= 1 && Recasters.getReflectance(cosIncident, hit.refractiveRatio) < Recasters.randGen.nextDouble()) {
            double cosRefracted = Math.sqrt(1 - sinRefractedSquared);
            IVector refractedDirection = hit.normalVector.multiply(hit.refractiveRatio * cosIncident - cosRefracted)
                    .subtract(hit.eyeVector.multiply(hit.refractiveRatio));
            return Optional.of(new Ray(hit.offsetBelow, refractedDirection));
        }
        else {
            return Recasters.fuzzilyReflective.apply(0.0).apply(hit);
        }
    };

    private static IVector getRandomInUnitSphere() {
        while (true) {
            IVector rand = new Vector(2*Recasters.randGen.nextDouble()-1, 2*Recasters.randGen.nextDouble()-1, 2*Recasters.randGen.nextDouble()-1);
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
