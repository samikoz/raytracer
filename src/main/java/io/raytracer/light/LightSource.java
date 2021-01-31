package io.raytracer.light;

import io.raytracer.drawing.Colour;

public interface LightSource {
    Colour illuminate(IlluminatedPoint illuminated);
}
