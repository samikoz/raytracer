package io.raytracer.geometry;

public interface ThreeVector extends Vector {
    ThreeVector cross(ThreeVector them);
}
