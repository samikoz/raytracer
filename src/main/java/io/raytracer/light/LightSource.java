package io.raytracer.light;

import io.raytracer.drawing.Colour;
import io.raytracer.geometry.Point;

public class LightSource {
    private final Colour colour;
    private final Point position;

    public LightSource(Colour colour, Point position) {
        this.colour = colour;
        this.position = position;
    }
}
