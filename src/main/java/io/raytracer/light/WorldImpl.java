package io.raytracer.light;

import io.raytracer.drawing.Camera;
import io.raytracer.drawing.Picture;
import io.raytracer.drawing.Colour;
import io.raytracer.drawing.ColourImpl;
import io.raytracer.drawing.Drawable;
import io.raytracer.drawing.PPMPicture;
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
    public Colour illuminate(@NonNull Ray ray) {
        Optional<Intersection> hit = intersect(ray).hit();
        if (hit.isPresent()) {
            return lightSource.illuminate(ray.getIlluminatedPoint(hit.get()));
        } else {
            return new ColourImpl(0, 0, 0);
        }
    }

    private IntersectionList intersect(@NonNull Ray ray) {
        Stream<IntersectionListImpl> s = contents.stream().map(object -> (IntersectionListImpl) object.intersect(ray));
        return s.reduce(IntersectionListImpl::combine).orElse(new IntersectionListImpl());
    }

    @Override
    public Picture render(Camera camera) {
        Picture picture = new PPMPicture(camera.getPictureWidth(), camera.getPictureHeight());

        for (int y = 0; y < camera.getPictureHeight() - 1; y++) {
            for (int x = 0; x < camera.getPictureWidth() -1; x++) {
                Ray ray = camera.getRayThroughPixel(x, y);
                Colour colour = this.illuminate(ray);
                picture.write(x, y, colour);
            }
        }

        return picture;
    }
}