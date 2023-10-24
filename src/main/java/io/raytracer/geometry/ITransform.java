package io.raytracer.geometry;

public interface ITransform {
    boolean isId();
    ITransform inverse();
    ITransform transpose();
    ISquareMatrix getMatrix();

    IPoint act(IPoint p);
    IVector act(IVector v);
}
