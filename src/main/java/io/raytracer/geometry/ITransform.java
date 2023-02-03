package io.raytracer.geometry;

public interface ITransform {
    ITransform inverse();
    ITransform transpose();

    IPoint act(IPoint p);
    IVector act(IVector v);
}
