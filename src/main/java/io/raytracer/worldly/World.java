package io.raytracer.worldly;

import io.raytracer.drawing.ICamera;
import io.raytracer.drawing.IPicture;
import io.raytracer.drawing.IColour;
import io.raytracer.drawing.Colour;
import io.raytracer.drawing.PPMPicture;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.worldly.drawables.Drawable;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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

    @Override
    public IPicture render(ICamera camera) {
        IPicture picture = new PPMPicture(camera.getPictureWidthPixels(), camera.getPictureHeightPixels());

        for (int y = 0; y < camera.getPictureHeightPixels() - 1; y++) {
            for (int x = 0; x < camera.getPictureWidthPixels() - 1; x++) {
                IRay ray = camera.getRayThroughPixel(x, y);
                IColour colour = this.illuminate(ray);
                picture.write(x, y, colour);
            }
        }

        return picture;
    }

    IColour illuminate(@NonNull IRay ray) {
        Optional<Hit> hit = this.intersect(ray).getHit();
        if (hit.isPresent()) {
            MaterialPoint realPoint = hit.get().getMaterialPoint();
            realPoint.shadowed = this.isShadowed(realPoint.offsetPoint);
            IColour surfaceColour = lightSource.illuminate(realPoint);
            IColour reflectedColour = this.getReflectedColour(realPoint);
            return surfaceColour.add(reflectedColour);
        } else {
            return new Colour(0, 0, 0);
        }
    }

    IIntersections intersect(@NonNull IRay ray) {
        Stream<Intersections> s = contents.stream().map(object -> (Intersections) object.intersect(ray));
        return s.reduce(Intersections::combine).orElse(new Intersections());
    }

    boolean isShadowed(IPoint point) {
        IVector lightDistance = this.lightSource.getPosition().subtract(point);
        IVector lightDirection = lightDistance.normalise();
        IRay lightRay = new Ray(point, lightDirection);
        Optional<Hit> shadowingHit = this.intersect(lightRay).getHit();
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
}