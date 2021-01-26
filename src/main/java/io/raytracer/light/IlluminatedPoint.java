package io.raytracer.light;

import io.raytracer.drawing.Drawable;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;

@AllArgsConstructor
public class IlluminatedPoint {
    public double time;
    @NonNull public Drawable object;
    @NonNull public Point point;
    @NonNull public Vector normalVector;
    @NonNull public Vector eyeVector;
    public boolean inside;
}
