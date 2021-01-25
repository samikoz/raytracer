package io.raytracer.light;

import io.raytracer.drawing.Drawable;

public interface World {
    LightSource getLightSource();

    void put(Drawable object);
    boolean contains(Drawable object);
}
