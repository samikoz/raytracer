package io.raytracer.geometry;

public interface IPlane {
    IPoint getPoint();
    IVector getNormal();
    IPoint intersect(ILine line);
    boolean doesContain(IPoint point);
}
