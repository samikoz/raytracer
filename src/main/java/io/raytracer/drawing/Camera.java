package io.raytracer.drawing;

import io.raytracer.geometry.Transformation;
import io.raytracer.light.Ray;

public interface Camera {
    int getHsize();
    int getVsize();

    Transformation getTransformation();

    Ray rayThrough(int x, int y);
}
