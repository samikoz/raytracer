package io.raytracer.light;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import io.raytracer.drawing.Colour;
import io.raytracer.geometry.Point;

@RequiredArgsConstructor
public class LightSource {
    @NonNull public final Colour colour;
    @NonNull public final Point position;
}
