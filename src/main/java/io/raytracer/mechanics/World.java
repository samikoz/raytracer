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
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class World implements IWorld {
    private ILightSource lightSource;
    private final List<Shape> contents;
    private static final int recursionDepth = 4;

    public World() {
        this.contents = new ArrayList<>();
    }

    @Override
    public IWorld put(ILightSource source) {
        this.lightSource = source;
        return this;
    }

    @Override
    public IWorld put(Shape object) {
        this.contents.add(object);
        return this;
    }

    public IColour illuminate(@NonNull IRay ray) {
        Optional<RayHit> hit = RayHit.fromIntersections(this.intersect(ray));
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
                    .add(refractedColour.multiply(1-hitpoint.reflectance));
            } else {
                return surfaceColour.add(reflectedColour).add(refractedColour);
            }
        } else {
            return new Colour(0, 0, 0);
        }
    }

    Intersection[] intersect(@NonNull IRay ray) {
        Stream<Intersection> s = contents.stream().map(object -> object.intersect(ray)).flatMap(Arrays::stream);
        return s.sorted().toArray(Intersection[]::new);
    }

    boolean isShadowed(IPoint point) {
        IVector lightDistance = this.lightSource.getPosition().subtract(point);
        IVector lightDirection = lightDistance.normalise();
        IRay lightRay = new Ray(point, lightDirection);
        Optional<RayHit> shadowingHit = RayHit.fromIntersections(this.intersect(lightRay));
        return (shadowingHit.isPresent() && shadowingHit.get().point.distance(point) < lightDistance.norm());
    }

    IColour getReflectedColour(@NotNull RayHit hitpoint) {
        if (hitpoint.object.getMaterial().reflectivity == 0 || hitpoint.ray.getRecast() > World.recursionDepth) {
            return new Colour(0, 0, 0);
        }
        IRay reflectedRay = Ray.reflectFrom(hitpoint);
        IColour reflectedColour = this.illuminate(reflectedRay);
        return reflectedColour.multiply(hitpoint.object.getMaterial().reflectivity);
    }

    IColour getRefractedColour(@NonNull RayHit hitpoint) {
        if (hitpoint.object.getMaterial().transparency == 0 || hitpoint.reflectance == 1.0 || hitpoint.ray.getRecast() > World.recursionDepth) {
            return new Colour(0, 0, 0);
        }

        IRay refractedRay = Ray.refractFrom(hitpoint);
        IColour refractedColour = this.illuminate(refractedRay);
        return refractedColour.multiply(hitpoint.object.getMaterial().transparency);
    }
}