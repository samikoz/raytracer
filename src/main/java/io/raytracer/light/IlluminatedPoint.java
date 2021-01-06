package io.raytracer.light;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;

@RequiredArgsConstructor
public class IlluminatedPoint {
    @NonNull public Point point;
    @NonNull public Vector normalVector;
    @NonNull public Material material;
}
