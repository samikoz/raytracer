package io.raytracer.worldly;

import io.raytracer.drawing.ICamera;
import io.raytracer.drawing.IPicture;
import io.raytracer.drawing.IColour;
import io.raytracer.drawing.Colour;
import io.raytracer.drawing.PPMPicture;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class World implements IWorld {
    private ILightSource lightSource;
    private final List<IDrawable> contents;
    private final double acneTolerance = 1e-6;

    public World() {
        this.contents = new ArrayList<>();
    }

    @Override
    public IWorld put(ILightSource source) {
        this.lightSource = source;
        return this;
    }

    @Override
    public IWorld put(IDrawable object) {
        this.contents.add(object);
        return this;
    }

    IColour illuminate(@NonNull IRay ray) {
        Optional<Intersection> hit = this.intersect(ray).getHit();
        if (hit.isPresent()) {
            MaterialPoint realPoint = hit.get().getMaterialPoint();
            IPoint normalOffset = realPoint.point.add(realPoint.object.normal(realPoint.point).multiply(1e-6));
            realPoint.shadowed = this.isShadowed(normalOffset);
            return lightSource.illuminate(realPoint);
        } else {
            return new Colour(0, 0, 0);
        }
    }

    private IIntersections intersect(@NonNull IRay ray) {
        Stream<Intersections> s = contents.stream().map(object -> (Intersections) object.intersect(ray));
        return s.reduce(Intersections::combine).orElse(new Intersections());
    }

    boolean isShadowed(IPoint point) {
        IVector lightDistance = this.lightSource.getPosition().subtract(point);
        IVector lightDirection = lightDistance.normalise();
        IRay lightRay = new Ray(point, lightDirection);
        Optional<Intersection> shadowingHit = this.intersect(lightRay).getHit();
        return (shadowingHit.isPresent() && shadowingHit.get().getMaterialPoint().point.distance(point) < lightDistance.norm());
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
}