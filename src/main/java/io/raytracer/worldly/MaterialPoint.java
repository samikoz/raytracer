package io.raytracer.worldly;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;

@AllArgsConstructor
public class MaterialPoint {
    @NonNull public Drawable object;
    @NonNull public IPoint point;
    @NonNull public IVector normalVector;
    @NonNull public IVector eyeVector;
    @NonNull public boolean shadowed;
}
