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
        if (ray.getDirection().get(2) == 0) {
            return new Intersection[] {};
        }
        double positionAtZZero = -ray.getOrigin().get(2)/ray.getDirection().get(2);
        double xCoordAtZZero = ray.getOrigin().get(0) + positionAtZZero*ray.getDirection().get(0);
        double yCoordAtZZero = ray.getOrigin().get(1) + positionAtZZero*ray.getDirection().get(1);
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
