package io.raytracer.mechanics;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.IVector;

public interface IRay {
    IVector getDirection();
    IPoint getOrigin();

    IPoint pointAt(double parameter);


    IRay getTransformed(ITransform t);
}
