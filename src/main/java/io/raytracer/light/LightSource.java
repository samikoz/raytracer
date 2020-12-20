package io.raytracer.light;

import io.raytracer.drawing.Colour;
import io.raytracer.geometry.Point;

public class LightSource {
    public final Colour colour;
    public final Point position;

    public LightSource(Colour colour, Point position) {
        this.colour = colour;
        this.position = position;
    }
}
