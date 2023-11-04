package io.raytracer.geometry;

public interface IPlane {
    IPoint getPoint();
    IVector getNormal();
    IPoint intersect(ILine line);
    boolean doesContain(IPoint point);

    IPlane translate(IVector translation);
    IPlane rotate_x(double angle);
    IPlane rotate_y(double angle);
    IPlane rotate_z(double angle);
}
