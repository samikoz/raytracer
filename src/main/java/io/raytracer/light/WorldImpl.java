package io.raytracer.light;

import io.raytracer.drawing.Camera;
import io.raytracer.drawing.Canvas;
import io.raytracer.drawing.Colour;
import io.raytracer.drawing.ColourImpl;
import io.raytracer.drawing.Drawable;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.SquareMatrix;
import io.raytracer.geometry.SquareMatrixImpl;
import io.raytracer.geometry.ThreeTransformation;
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
        Vector forwardDirection = lookPosition.subtract(eyePosition).normalise();
        Vector upNormalised = upDirection.normalise();
        Vector leftDirection = forwardDirection.cross(upNormalised);
        Vector realUpDirection = leftDirection.cross(forwardDirection);

        SquareMatrix originTransformation = new SquareMatrixImpl(
                leftDirection.get(0), leftDirection.get(1), leftDirection.get(2), 0,
                realUpDirection.get(0), realUpDirection.get(1), realUpDirection.get(2), 0,
                -forwardDirection.get(0), -forwardDirection.get(1), -forwardDirection.get(2), 0,
                0, 0, 0, 1);

        return ThreeTransformation.translation(-eyePosition.get(0), -eyePosition.get(1), -eyePosition.get(2))
                .transform(originTransformation);
    }

    @Override
    public Canvas render(Camera camera) {
        return null;
    }
}