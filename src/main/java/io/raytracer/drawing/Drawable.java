package io.raytracer.drawing;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.light.IntersectionList;
import io.raytracer.light.Ray;

public interface Drawable {
    IntersectionList intersect(Ray ray);
    Vector normal(Point point);
}
