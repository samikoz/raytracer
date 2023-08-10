package io.raytracer.mechanics;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.IVector;
import io.raytracer.shapes.Shape;

public interface IRay {
    IVector getDirection();
    IPoint getOrigin();

    IPoint getPosition(double parameter);

    Intersection reintersect(Intersection i);
    IRay recast(IPoint from, IVector direction);
    int getRecast();

    IRay getTransformed(ITransform t);
}
