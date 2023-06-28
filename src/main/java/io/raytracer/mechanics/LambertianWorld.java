package io.raytracer.mechanics;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Vector;
import io.raytracer.tools.GammaColour;
import io.raytracer.tools.IColour;

import java.util.Collections;
import java.util.Random;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

public class LambertianWorld extends World {
    private static final int lambertianDepth = 50;

    public LambertianWorld() {
        super();
    }

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
            IRay reflectionRay = ray.recast(hitpoint.offsetAbove, this.getLambertianDirection(hitpoint));
            return hitpoint.object.getIntrinsicColour(hitpoint.point).mix(this.illuminate(Collections.singletonList(reflectionRay)));
        }
        else {
            return this.getBackgroundAt(ray);
        }
    }

    private IVector getLambertianDirection(RayHit hitpoint) {
        IPoint outerNormalCentre = hitpoint.point.add(hitpoint.normalVector.normalise());
        return outerNormalCentre.add(this.getRandomInUnitSphere()).subtract(hitpoint.point);
    }

    private IVector getRandomInUnitSphere() {
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
