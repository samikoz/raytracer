package io.raytracer.light;

import io.raytracer.drawing.Camera;
import io.raytracer.drawing.Canvas;
import io.raytracer.drawing.Colour;
import io.raytracer.drawing.ColourImpl;
import io.raytracer.drawing.Drawable;
import io.raytracer.drawing.PPMCanvas;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class WorldImpl implements World {
    private LightSource lightSource;
    private final List<Drawable> contents;

    public WorldImpl() {
        this.contents = new ArrayList<>();
    }

    @Override
    public World put(LightSource source) {
        this.lightSource = source;
        return this;
    }

    @Override
    public World put(Drawable object) {
        this.contents.add(object);
        return this;
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
    public Canvas render(Camera camera) {
        Canvas canvas  = new PPMCanvas(camera.getHsize(), camera.getVsize());

        for (int y = 0; y < camera.getVsize() - 1; y++) {
            for (int x = 0; x < camera.getHsize() -1; x++) {
                Ray ray = camera.rayThrough(x, y);
                Colour colour = this.illuminate(ray);
                canvas.write(x, y, colour);
            }
        }

        return canvas;
    }
}