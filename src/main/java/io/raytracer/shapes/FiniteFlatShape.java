package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Interval;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.TextureParameters;
import lombok.AllArgsConstructor;

public abstract class FiniteFlatShape extends Shape{
    abstract boolean hitCondition(ZeroZData data);
    private final double rayZDirectionTolerance = 1e-7;

    public FiniteFlatShape() {
        super();
    }

    public FiniteFlatShape(Material material) {
        super(material);
    }

    @Override
    protected Intersection[] getLocalIntersections(IRay ray, Interval rayDomain) {
        if (Math.abs(ray.getDirection().z()) < rayZDirectionTolerance) {
            return new Intersection[] {};
        }
        double positionAtZZero = -ray.getOrigin().z()/ray.getDirection().z();
        double xCoordAtZZero = ray.getOrigin().x() + positionAtZZero*ray.getDirection().x();
        double yCoordAtZZero = ray.getOrigin().y() + positionAtZZero*ray.getDirection().y();
        ZeroZData data = new ZeroZData(positionAtZZero, xCoordAtZZero, yCoordAtZZero);
        if (this.hitCondition(data)) {
            return new Intersection[]{new Intersection(this, ray, positionAtZZero, new TextureParameters())};
        }
        return new Intersection[] {};
    }

    @Override
    protected IVector localNormalAt(IPoint point, TextureParameters p) {
        return new Vector(0, 0, -1);
    }
}

@AllArgsConstructor
class ZeroZData {
    public double rayParameter;
    public double x;
    public double y;
}