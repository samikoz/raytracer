package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Interval;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.BBox;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.TextureParameters;
import lombok.NonNull;

import static java.lang.Math.abs;

public class Plane extends Shape {
    private static final double parallelTolerance = 1e-3;

    public Plane() {
        super();
    }

    public Plane(@NonNull Material material) {
        super(material);
    }

    @Override
    protected Intersection[] getLocalIntersections(IRay ray, Interval rayDomain) {
        if (abs(ray.getDirection().y()) < parallelTolerance) {
            return new Intersection[]{};
        }
        double intersection = -ray.getOrigin().y() / ray.getDirection().y();
        if (intersection < rayDomain.min || intersection > rayDomain.max) {
            return new Intersection[] {};
        }
        return new Intersection[] { new Intersection(this, ray, -ray.getOrigin().y() / ray.getDirection().y(),new TextureParameters()) };
    }

    @Override
    protected IVector localNormalAt(IPoint point, double u, double v) {
        return new Vector(0, 1, 0);
    }

    @Override
    protected BBox getLocalBoundingBox() {
        return new BBox(
            new Point(Double.NEGATIVE_INFINITY, -BBox.paddingMargin, Double.NEGATIVE_INFINITY),
            new Point(Double.POSITIVE_INFINITY, BBox.paddingMargin, Double.POSITIVE_INFINITY)
        );
    }
}
