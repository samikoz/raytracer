package io.raytracer.geometry;

import java.util.Optional;

public interface IPlane {
    IPoint getPoint();
    IVector getNormal();
    Optional<IPoint> intersect(ILine line);
    IPoint reflect(IPoint p);
    boolean doesContain(IPoint point);
}
