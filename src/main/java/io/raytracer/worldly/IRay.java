package io.raytracer.worldly;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.IVector;

public interface IRay {
    IVector getDirection();
    IPoint getOrigin();

    IPoint getPosition(double parameter);

    IRay getTransformed(ITransform t);
}
