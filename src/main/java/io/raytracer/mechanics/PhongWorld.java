package io.raytracer.mechanics;

import io.raytracer.tools.IColour;
import io.raytracer.tools.Colour;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.shapes.Shape;
import io.raytracer.materials.Material;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
        if (hitpoint.object.getMaterial().reflectivity == 0 || hitpoint.ray.getRecast() > PhongWorld.recursionDepth) {
            return new Colour(0, 0, 0);
        }
        Collection<IRay> reflectedRays = Collections.singletonList(hitpoint.ray.reflectFrom(hitpoint.offsetAbove, hitpoint.normalVector));
        IColour reflectedColour = this.illuminate(reflectedRays);
        return reflectedColour.multiply(hitpoint.object.getMaterial().reflectivity);
    }

    IColour getRefractedColour(@NonNull RayHit hitpoint) {
        if (hitpoint.object.getMaterial().transparency == 0 || hitpoint.reflectance == 1.0 || hitpoint.ray.getRecast() > PhongWorld.recursionDepth) {
            return new Colour(0, 0, 0);
        }

        Collection<IRay> refractedRays = Collections.singletonList(hitpoint.ray.refractOn(hitpoint.getRefractionPoint()));
        IColour refractedColour = this.illuminate(refractedRays);
        return refractedColour.multiply(hitpoint.object.getMaterial().transparency);
    }
}