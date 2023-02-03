package io.raytracer.worldly;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;

public interface IDrawable {
    Material getMaterial();
    IIntersections intersect(IRay ray);
    IVector normal(IPoint point);
}
