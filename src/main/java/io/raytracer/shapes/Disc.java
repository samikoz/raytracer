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

public class Disc extends Shape {
    public Disc() {
        super();
    }

    public Disc(Material material) {
        super(material);
    }

    @Override
    protected Intersection[] getLocalIntersections(IRay ray, Interval rayDomain) {
        if (ray.getDirection().z() == 0) {
            return new Intersection[] {};
        }
        double positionAtZZero = -ray.getOrigin().z()/ray.getDirection().z();
        double xCoordAtZZero = ray.getOrigin().x() + positionAtZZero*ray.getDirection().x();
        double yCoordAtZZero = ray.getOrigin().y() + positionAtZZero*ray.getDirection().y();
        if (Math.pow(xCoordAtZZero,2) + Math.pow(yCoordAtZZero,2) < 1 && positionAtZZero >= rayDomain.min && positionAtZZero <= rayDomain.max) {
            return new Intersection[]{new Intersection(this, ray, positionAtZZero, new TextureParameters())};
        }
        return new Intersection[] {};
    }

    @Override
    protected IVector localNormalAt(IPoint point, double u, double v) {
        return new Vector(0, 0, -1);
    }

    @Override
    protected BBox getLocalBoundingBox() {
        return new BBox(new Point(-1, -1, -BBox.paddingMargin), new Point(1, 1, BBox.paddingMargin));
    }
}
