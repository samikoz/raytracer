package io.raytracer.geometry;

public interface IPlane {
    IPoint getPoint();
    IVector getNormal();
    IPoint intersect(ILine line);
    IPoint reflect(IPoint p);
    boolean doesContain(IPoint point);
}
