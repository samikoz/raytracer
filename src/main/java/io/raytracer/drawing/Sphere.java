package io.raytracer.drawing;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.Transformation;
import io.raytracer.geometry.Vector;

public interface Sphere extends Drawable {
    Transformation getTransform();
    void setTransform(Transformation t);

    Vector normal(Point p);
}
