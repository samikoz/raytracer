package io.raytracer.mechanics;

import io.raytracer.tools.GammaColour;
import io.raytracer.tools.IColour;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

public class LambertianWorld extends World {
    private static final int lambertianDepth = 50;

    public LambertianWorld(Function<IRay, IColour> background) {
        super(background);
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
            Collection<IRay> reflectedRays = Recasters.diffuse.apply(hitpoint);
            return hitpoint.object.getIntrinsicColour(hitpoint.point).mix(this.illuminate(reflectedRays));
        }
        else {
            return this.getBackgroundAt(ray);
        }
    }
}
