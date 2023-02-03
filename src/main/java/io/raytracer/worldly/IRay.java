package io.raytracer.worldly;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.IVector;

public interface IRay {
    IVector getDirection();
    IPoint getOrigin();

    IPoint position(double parameter);

    IRay transform(ITransform t);
}
