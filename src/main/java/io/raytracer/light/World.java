package io.raytracer.light;

import io.raytracer.drawing.Camera;
import io.raytracer.drawing.Picture;
import io.raytracer.drawing.Colour;
import io.raytracer.drawing.Drawable;

public interface World {
    World put(LightSource source);
    World put(Drawable object);

    Colour illuminate(Ray ray);

    Picture render(Camera camera);
}
