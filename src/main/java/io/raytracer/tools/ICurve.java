package io.raytracer.tools;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;

public interface ICurve {
    IPoint pointAt(double t);

    IVector normalAt(double t);
}
