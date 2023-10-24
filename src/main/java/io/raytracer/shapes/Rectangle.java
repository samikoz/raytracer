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

public class Rectangle extends Shape {
    public Rectangle() {
        super();
    }

    public Rectangle(Material material) {
        super(material);
    }

    @Override
    protected Intersection[] getLocalIntersections(IRay ray, Interval rayDomain) {
        if (ray.getDirection().get(2) == 0) {
            return new Intersection[] {};
        }
        double rayAtZZero = -ray.getOrigin().get(2)/ray.getDirection().get(2);
        double xCoordAtZZero = ray.getOrigin().get(0) + rayAtZZero*ray.getDirection().get(0);
        double yCoordAtZZero = ray.getOrigin().get(1) + rayAtZZero*ray.getDirection().get(1);
        if ((int)xCoordAtZZero == 0 && (int)yCoordAtZZero == 0 && rayAtZZero >= rayDomain.min && rayAtZZero <= rayDomain.max) {
            return new Intersection[] { new Intersection(this, ray, rayAtZZero, new TextureParameters()) };
        }
        return new Intersection[] {};
    }

    @Override
    protected IVector localNormalAt(IPoint point, double u, double v) {
        return new Vector(0, 0, -1);
    }

    @Override
    protected BBox getLocalBoundingBox() {
        return new BBox(new Point(0, 0, -BBox.paddingMargin), new Point(1, 1, BBox.paddingMargin));
    }
}
