package io.raytracer.light;

import io.raytracer.drawing.Colour;
import io.raytracer.drawing.ColourImpl;
import io.raytracer.drawing.Drawable;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Transformation;
import io.raytracer.geometry.Vector;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class WorldImpl implements World {
    @Getter private final LightSourceImpl lightSource;
    private final List<Drawable> contents;

    public WorldImpl(LightSourceImpl lightsource) {
        this.lightSource = lightsource;
        this.contents = new ArrayList<>();
    }

    @Override
    public void put(Drawable object) {
        this.contents.add(object);
    }

    @Override
    public boolean contains(Drawable object) {
        return this.contents.contains(object);
    }

    @Override
    public IntersectionList intersect(@NonNull Ray ray) {
        Stream<IntersectionListImpl> s = contents.stream().map(object -> (IntersectionListImpl) object.intersect(ray));
        return s.reduce(IntersectionListImpl::combine).orElse(new IntersectionListImpl());
    }

    @Override
    public Colour illuminate(@NonNull Ray ray) {
        Optional<Intersection> hit = intersect(ray).hit();
        if (hit.isPresent()) {
            return lightSource.illuminate(ray.getIlluminatedPoint(hit.get()));
        } else {
            return new ColourImpl(0, 0, 0);
        }
    }

    @Override
    public Transformation getViewTransformation(Point eyePosition, Point lookPosition, Vector upDirection) {
        return null;
    }
}
