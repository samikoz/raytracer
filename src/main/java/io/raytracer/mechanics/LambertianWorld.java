package io.raytracer.mechanics;

import io.raytracer.materials.RecasterContribution;
import io.raytracer.tools.GammaColour;
import io.raytracer.tools.IColour;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public class LambertianWorld extends World {
    private static final int lambertianDepth = 50;
    private final Supplier<Float> randomVariable;

    public LambertianWorld() {
        super();
        this.randomVariable = (new Random())::nextFloat;
    }
    public LambertianWorld(Function<IRay, IColour> background) {
        super(background);
        this.randomVariable = (new Random())::nextFloat;
    }

    public LambertianWorld(IColour backgroundColour) {
        super(ray -> backgroundColour);
        this.randomVariable = (new Random()::nextFloat);
    }

    LambertianWorld(Supplier<Float> randomVariable) {
        this.randomVariable = randomVariable;
    }

    @Override
    IColour illuminate(IRay ray) {
        if (ray.getRecast() >= LambertianWorld.lambertianDepth) {
            return new GammaColour(0, 0, 0);
        }
        Collection<Intersection> intersections = this.intersect(ray);
        Optional<RayHit> hit = RayHit.fromIntersections(intersections);
        if (hit.isPresent()) {
            RayHit hitpoint = hit.get();
            IColour emit = hitpoint.object.getMaterial().emit;
            if (!emit.equals(new GammaColour(0, 0 ,0))) {
                return emit;
            }

            float randVar = this.randomVariable.get();
            Optional<IRay> recastRay = this.getRecastRays(hitpoint, randVar);
            IColour recastColour = recastRay.map(this::illuminate).orElse(new GammaColour(0, 0, 0));
            return hitpoint.object.getIntrinsicColour(hitpoint).mix(recastColour);
        }
        else {
            return this.getBackgroundAt(ray);
        }
    }

    private Optional<IRay> getRecastRays(RayHit hitpoint, float randomChance) {
        List<RecasterContribution> recasters = hitpoint.object.getMaterial().getRecasterContributions();
        double probabilityThreshold = 1;
        for (int i = recasters.size() - 1; i > -1; i--) {
            RecasterContribution presentContribution = recasters.get(i);
            probabilityThreshold -= presentContribution.contribution;
            if (randomChance > probabilityThreshold) {
                return presentContribution.recaster.apply(hitpoint);
            }
        }
        return recasters.get(0).recaster.apply(hitpoint);
    }
}
