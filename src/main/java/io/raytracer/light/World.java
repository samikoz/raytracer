package io.raytracer.light;

import io.raytracer.drawing.Drawable;

public interface World {
    LightSourceImpl getLightSource();

    void put(Drawable object);
    boolean contains(Drawable object);

    IntersectionList intersect(Ray ray);
}
