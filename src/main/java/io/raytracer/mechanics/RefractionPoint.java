package io.raytracer.mechanics;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RefractionPoint {
    public final IPoint point;
    public final double refractiveIndexFrom;
    public final double refractiveIndexTo;
    public final IVector eyeVector;
    public final IVector normalVector;
}
