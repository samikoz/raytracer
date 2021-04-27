package io.raytracer.drawing;

import io.raytracer.light.Ray;

public interface Camera {
    int getPictureWidth();
    int getPictureHeight();

    Ray rayThroughPixel(int x, int y);
}
