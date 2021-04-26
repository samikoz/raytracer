package io.raytracer.drawing;

import io.raytracer.light.Ray;

public interface Camera {
    int getHsize();
    int getVsize();

    Ray rayThrough(int x, int y);
}
