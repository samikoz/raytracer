package io.raytracer.light;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class Intersection {
    public double time;
    @NonNull public Drawable object;
}
