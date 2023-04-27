package io.raytracer.mechanics;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.IVector;

public interface IRay {
    IVector getDirection();
    IPoint getOrigin();

    IPoint getPosition(double parameter);

    IRay reflectFrom(IPoint point, IVector reflectionVector);
    IRay refractOn(RefractionPoint refpoint);
    int getRecast();

    IRay getTransformed(ITransform t);
}
