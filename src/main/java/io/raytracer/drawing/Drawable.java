package io.raytracer.drawing;

import io.raytracer.light.IntersectionList;
import io.raytracer.light.Ray;

public interface Drawable {
    IntersectionList intersect(Ray ray);
}
