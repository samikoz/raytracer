package io.raytracer.mechanics;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Vector;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.function.Function;


public class Recasters {
    public static Function<RayHit, Collection<IRay>> diffuse = hit -> {
        IPoint outerNormalCentre = hit.point.add(hit.normalVector.normalise());
        IVector recastDirection = outerNormalCentre.add(Recasters.getRandomInUnitSphere()).subtract(hit.point);
        return Collections.singletonList(hit.ray.recast(hit.offsetAbove, recastDirection));
    };

    public static Function<RayHit, Collection<IRay>> reflective = hit -> {
        IVector reflectionDirection = hit.ray.getDirection().reflect(hit.normalVector);
        return Collections.singletonList(hit.ray.recast(hit.offsetAbove, reflectionDirection));
    };

    public static Function<RayHit, Collection<IRay>> refractive = hit -> {
        double refractedRatio = hit.refractiveIndexFrom / hit.refractiveIndexTo;
        double cosIncident = hit.eyeVector.dot(hit.normalVector);
        double sinRefractedSquared = Math.pow(refractedRatio, 2)*(1 - Math.pow(cosIncident, 2));
        assert sinRefractedSquared <= 1;
        double cosRefracted = Math.sqrt(1 - sinRefractedSquared);
        IVector refractedDirection = hit.normalVector.multiply(refractedRatio*cosIncident - cosRefracted)
                .subtract(hit.eyeVector.multiply(refractedRatio));
        return Collections.singletonList(hit.ray.recast(hit.point, refractedDirection));
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
}
