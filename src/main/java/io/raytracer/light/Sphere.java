package io.raytracer.light;

import io.raytracer.geometry.Transformation;

public interface Sphere extends Drawable {
    Transformation getTransform();
    void setTransform(Transformation t);
}
