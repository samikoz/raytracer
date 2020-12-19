package io.raytracer.light;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.Transformation;
import io.raytracer.geometry.Vector;

public interface Sphere extends Drawable {
    Transformation getTransform();
    void setTransform(Transformation t);

    Material getMaterial();

    Vector normal(Point p);
}
