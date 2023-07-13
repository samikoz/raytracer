package io.raytracer.mechanics;

import io.raytracer.tools.IColour;
import io.raytracer.tools.LinearColour;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.materials.Material;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.function.Function;

public class PhongWorld extends World {
    private ILightSource lightSource;
    private static final int recursionDepth = 4;

    public PhongWorld() {
        super();
    }

    public PhongWorld(Function<IRay, IColour> background) {
        super(background);
    }

    public PhongWorld put(ILightSource source) {
        this.lightSource = source;
        return this;
    }

    public IColour illuminate(IRay ray) {
        if (ray.getRecast() >= PhongWorld.recursionDepth) {
            return new LinearColour(0, 0, 0);
        }
        Collection<Intersection> intersections = this.intersect(ray);
        Optional<RayHit> hit = RayHit.fromIntersections(intersections);
        if (hit.isPresent()) {
            RayHit hitpoint = hit.get();
            hitpoint.shadowed = this.isShadowed(hitpoint.offsetAbove);
            IColour surfaceColour = lightSource.illuminate(hitpoint);
            IColour reflectedColour = this.getReflectedColour(hitpoint);
            IColour refractedColour = this.getRefractedColour(hitpoint);
            Material pointMaterial = hitpoint.object.getMaterial();
            if (pointMaterial.reflectivity > 0 && pointMaterial.transparency > 0) {
                //Schlick approximation
                return surfaceColour
                        .add(reflectedColour.multiply(hitpoint.reflectance))
                        .add(refractedColour.multiply(1 - hitpoint.reflectance));
            } else {
                return surfaceColour.add(reflectedColour).add(refractedColour);
            }
        } else {
            return this.getBackgroundAt(ray);
        }
    }

    boolean isShadowed(IPoint point) {
        IVector lightDistance = this.lightSource.getPosition().subtract(point);
        IVector lightDirection = lightDistance.normalise();
        IRay lightRay = new Ray(point, lightDirection);
        Collection<Intersection> shadowingIntersections = this.intersect(lightRay).stream().filter(i -> i.object.isCastingShadows()).collect(Collectors.toList());
        Optional<RayHit> shadowingHit = RayHit.fromIntersections(shadowingIntersections);
        return (shadowingHit.isPresent() && shadowingHit.get().point.distance(point) < lightDistance.norm());
    }

    IColour getReflectedColour(@NotNull RayHit hitpoint) {
        if (hitpoint.object.getMaterial().reflectivity == 0) {
            return new LinearColour(0, 0, 0);
        }
        IColour reflectedColour =  this.illuminate(Recasters.reflective.apply(hitpoint));
        return reflectedColour.multiply(hitpoint.object.getMaterial().reflectivity);
    }

    IColour getRefractedColour(@NonNull RayHit hitpoint) {
        if (hitpoint.object.getMaterial().transparency == 0 || hitpoint.reflectance == 1.0) {
            return new LinearColour(0, 0, 0);
        }
        IColour refractedColour = this.illuminate(Recasters.refractive.apply(hitpoint));
        return refractedColour.multiply(hitpoint.object.getMaterial().transparency);
    }
}