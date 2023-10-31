package io.raytracer.mechanics;

import io.raytracer.materials.RecasterContribution;
import io.raytracer.mechanics.reporters.RenderData;
import io.raytracer.mechanics.reporters.Reporter;
import io.raytracer.tools.GammaColour;
import io.raytracer.tools.IColour;
import io.raytracer.tools.LinearColour;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public class LambertianWorld extends World {
    private static final int lambertianDepth = 50;
    private static final double imperceptibleColourThreshold = 0.02;
    private final Supplier<Float> randomVariable;

    public LambertianWorld() {
        super();
        this.randomVariable = (new Random())::nextFloat;
    }
    public LambertianWorld(Function<IRay, IColour> background) {
        super(background);
        this.randomVariable = (new Random())::nextFloat;
    }

    public LambertianWorld(Function<IRay, IColour> background, Function<RenderData, Reporter> reporterFactory) {
        super(background, reporterFactory);
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
        return this.illuminate(ray, new LinearColour(1, 1, 1));
    }

    private IColour illuminate(IRay ray, IColour colourSoFar) {
        if (ray.getRecast() >= LambertianWorld.lambertianDepth) {
            return new LinearColour(0, 0, 0);
        }
        Optional<RayHit> hit = RayHit.fromIntersections(this.intersect(ray));
        if (hit.isPresent()) {
            RayHit hitpoint = hit.get();
            IColour emit = hitpoint.object.getMaterial().emit;
            if (!emit.equals(new LinearColour(0, 0 ,0))) {
                return emit;
            }
            IColour ownColour = hitpoint.object.getIntrinsicColour(hitpoint);
            IColour contributedColour = colourSoFar.mix(ownColour);
            if (contributedColour.getBrightness() < LambertianWorld.imperceptibleColourThreshold) {
                return new LinearColour(0, 0, 0);
            }

            float randVar = this.randomVariable.get();
            Optional<IRay> recastRay = this.getRecastRay(hitpoint, randVar);
            IColour recastColour = recastRay.map(r -> this.illuminate(r, contributedColour)).orElse(new GammaColour(0, 0, 0));
            return ownColour.mix(recastColour);
        }
        else {
            return this.getBackgroundAt(ray);
        }
    }

    private Optional<IRay> getRecastRay(RayHit hitpoint, float randomChance) {
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
