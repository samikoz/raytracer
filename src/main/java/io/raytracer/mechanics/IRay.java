package io.raytracer.mechanics;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.IVector;
import io.raytracer.shapes.Shape;

public interface IRay {
    IVector getDirection();
    IPoint getOrigin();

    IPoint getPosition(double parameter);

    Intersection intersect(Shape shape, double parameter);
    IRay reflectFrom(IPoint point, IVector reflectionVector);
    IRay refractOn(RefractionPoint refpoint);
    int getRecast();

    IRay getTransformed(ITransform t);
}
