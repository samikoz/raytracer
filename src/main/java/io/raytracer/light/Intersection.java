package io.raytracer.light;

import io.raytracer.drawing.Drawable;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class Intersection {
    public double time;
    @NonNull public Drawable object;
}
