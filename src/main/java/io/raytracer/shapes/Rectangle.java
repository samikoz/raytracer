package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.BBox;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;

import java.util.Optional;

public class Rectangle extends Shape {
    public Rectangle() {
        super();
    }

    public Rectangle(Material material) {
        super(material);
    }

    @Override
    protected Intersection[] getLocalIntersections(IRay ray, double tmin, double tmax) {
        if (ray.getDirection().get(2) == 0) {
            return new Intersection[] {};
        }
        double positionAtZZero = -ray.getOrigin().get(2)/ray.getDirection().get(2);
        double xCoordAtZZero = ray.getOrigin().get(0) + positionAtZZero*ray.getDirection().get(0);
        double yCoordAtZZero = ray.getOrigin().get(1) + positionAtZZero*ray.getDirection().get(1);
        if ((int)xCoordAtZZero == 0 && (int)yCoordAtZZero == 0 && positionAtZZero >= tmin && positionAtZZero <= tmax) {
            return new Intersection[] { new Intersection(this, ray, positionAtZZero, 0, 0) };
        }
        return new Intersection[] {};
    }

    @Override
    protected IVector localNormalAt(IPoint point, double u, double v) {
        return new Vector(0, 0, -1);
    }

    @Override
    protected Optional<BBox> getLocalBoundingBox() {
        return Optional.of(new BBox(new Point(0, -1e-3, 0), new Point(1, 1e-3, 1)));
    }
}
