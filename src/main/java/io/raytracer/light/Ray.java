package io.raytracer.light;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.Transformation;
import io.raytracer.geometry.Vector;

public interface Ray {
    Vector getDirection();
    Point getOrigin();

    Point position(double time);

    IlluminatedPoint getIlluminatedPoint(Intersection intersection);

    Ray transform(Transformation t);
}
