package io.raytracer.light;

import io.raytracer.drawing.Drawable;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Transformation;
import io.raytracer.geometry.Vector;

public interface Ray {
    Vector getDirection();
    Point getOrigin();

    Point position(double time);

    IntersectionList intersect(Drawable object);

    Ray transform(Transformation t);
}
