package io.raytracer.drawing.patterns;

import io.raytracer.drawing.Colour;
import io.raytracer.geometry.IPoint;

public interface IPattern {
    Colour colourAt(IPoint p);

    int getHashCode();
}
