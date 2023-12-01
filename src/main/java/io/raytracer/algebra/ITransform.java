package io.raytracer.algebra;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;

public interface ITransform {
    ITransform inverse();
    ITransform transpose();
    ISquareMatrix getMatrix();
    ITransform transform(ITransform t);

    IPoint act(IPoint p);
    IVector act(IVector v);
}
