package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.IRay;

public class Rectangle extends Shape {
    public Rectangle() {
        super();
    }

    public Rectangle(Material material) {
        super(material);
    }

    @Override
    protected double[] getLocalIntersectionPositions(IRay ray) {
        if (ray.getDirection().get(2) == 0) {
            return new double[] {};
        }
        double positionAtZZero = -ray.getOrigin().get(2)/ray.getDirection().get(2);
        double xCoordAtZZero = ray.getOrigin().get(0) + positionAtZZero*ray.getDirection().get(0);
        double yCoordAtZZero = ray.getOrigin().get(1) + positionAtZZero*ray.getDirection().get(1);
        if ((int)xCoordAtZZero == 0 && (int)yCoordAtZZero == 0) {
            return new double[] { positionAtZZero };
        }
        return new double[] {};
    }

    @Override
    protected IVector normalLocally(IPoint point) {
        return new Vector(0, 0, -1);
    }
}
