package io.raytracer.drawing;

import io.raytracer.geometry.Transformation;
import io.raytracer.light.Ray;

public interface Camera {
    Transformation getTransformation();

    Ray rayThrough(int x, int y);
}
