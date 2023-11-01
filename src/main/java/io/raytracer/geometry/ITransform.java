package io.raytracer.geometry;

public interface ITransform {
    ITransform inverse();
    ITransform transpose();
    ISquareMatrix getMatrix();

    IPoint act(IPoint p);
    IVector act(IVector v);
}
