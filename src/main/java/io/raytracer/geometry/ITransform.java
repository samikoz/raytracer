package io.raytracer.geometry;

public interface ITransform {
    ITransform inverse();
    ITransform transpose();
    ISquareMatrix getMatrix();
    ITransform transform(ITransform t);

    IPoint act(IPoint p);
    IVector act(IVector v);
}
