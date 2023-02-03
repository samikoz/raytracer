package io.raytracer.worldly;

import io.raytracer.drawing.ICamera;
import io.raytracer.drawing.IPicture;
import io.raytracer.drawing.IColour;
import io.raytracer.drawing.Colour;
import io.raytracer.drawing.PPMPicture;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class World implements IWorld {
    private ILightSource lightSource;
    private final List<IDrawable> contents;

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
            return lightSource.illuminate(hit.get().getMaterialPoint());
        } else {
            return new Colour(0, 0, 0);
        }
    }

    private IIntersections intersect(@NonNull IRay ray) {
        Stream<Intersections> s = contents.stream().map(object -> (Intersections) object.intersect(ray));
        return s.reduce(Intersections::combine).orElse(new Intersections());
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