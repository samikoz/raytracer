package io.raytracer.light;

import io.raytracer.drawing.Drawable;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class WorldImpl implements World {
    @Getter private final LightSource lightSource;
    private final List<Drawable> contents;

    public WorldImpl(LightSource lightsource) {
        this.lightSource = lightsource;
        this.contents = new ArrayList<Drawable>();
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
    public IntersectionList intersect(Ray ray) {
        Stream<IntersectionListImpl> s = contents.stream().map(object -> (IntersectionListImpl) object.intersect(ray));
        return s.reduce(IntersectionListImpl::combine).orElse(new IntersectionListImpl());
    }
}
