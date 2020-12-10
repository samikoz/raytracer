package io.raytracer.light;

import io.raytracer.geometry.Point;

public interface Ray {
    Point position(double time);
}
