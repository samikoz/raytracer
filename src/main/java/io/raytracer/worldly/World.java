package io.raytracer.worldly;

import io.raytracer.drawing.IColour;
import io.raytracer.drawing.Colour;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.worldly.drawables.Drawable;
import io.raytracer.worldly.materials.Material;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class World implements IWorld {
    private ILightSource lightSource;
    private final List<Drawable> contents;
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
    public IWorld put(Drawable object) {
        this.contents.add(object);
        return this;
    }

    public IColour illuminate(@NonNull IRay ray) {
        Optional<Hit> hit = Hit.fromIntersections(this.intersect(ray));
        if (hit.isPresent()) {
            MaterialPoint realPoint = hit.get().getMaterialPoint();
            realPoint.shadowed = this.isShadowed(realPoint.offsetAbove);
            IColour surfaceColour = lightSource.illuminate(realPoint);
            IColour reflectedColour = this.getReflectedColour(realPoint);
            return surfaceColour.add(reflectedColour);
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
        Optional<Hit> shadowingHit = Hit.fromIntersections(this.intersect(lightRay));
        return (shadowingHit.isPresent() && shadowingHit.get().getMaterialPoint().point.distance(point) < lightDistance.norm());
    }

    IColour getReflectedColour(@NotNull MaterialPoint point) {
        if (point.object.getMaterial().reflectivity == 0 || point.inRay.getReflectionDepth() > World.recursionDepth) {
            return new Colour(0, 0, 0);
        }
        IRay reflectedRay = Ray.reflectFrom(point);
        IColour reflectedColour = this.illuminate(reflectedRay);
        return reflectedColour.multiply(point.object.getMaterial().reflectivity);
    }

    IColour getRefractedColour(@NonNull MaterialPoint point) {
        if (point.object.getMaterial().transparency == 0) {
            return new Colour(0, 0, 0);
        }

        double refractedRatio = point.refractiveIndexFrom / point.refractiveIndexTo;
        double cosIncident = point.eyeVector.normalise().dot(point.normalVector.normalise());
        double sinRefractedSquared = Math.pow(refractedRatio, 2)*(1 - Math.pow(cosIncident, 2));
        if (sinRefractedSquared > 1) {
            //total internal reflection
            return new Colour(0, 0, 0);
        }
        return new Colour(1, 1, 1);
    }
}