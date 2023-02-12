package io.raytracer.worldly;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.IVector;

public interface IRay {
    IVector direction();
    IPoint origin();

    IPoint position(double parameter);

    IRay getTransformed(ITransform t);
}
