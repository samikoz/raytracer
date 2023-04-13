package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.IRay;
import lombok.NonNull;

public class Cylinder extends Shape {
    private static final double tolerance = 1e-3;

    Cylinder() {
        super();
    }

    Cylinder(@NonNull Material material) {
        super(material);
    }

    @Override
    protected double[] getLocalIntersectionPositions(IRay ray) {
        IVector rayDirection = ray.getDirection();
        IPoint rayOrigin = ray.getOrigin();
        double a = Math.pow(rayDirection.get(0), 2) + Math.pow(rayDirection.get(2), 2);
        if (Math.abs(a) < tolerance) {
            return new double[] {};
        }

        double b = 2*rayOrigin.get(0)*rayDirection.get(0) + 2*rayOrigin.get(2)*rayDirection.get(2);
        double c = Math.pow(rayOrigin.get(0), 2) + Math.pow(rayOrigin.get(2), 2) - 1;
        double delta = Math.pow(b, 2) - 4*a*c;
        if (delta < 0) {
            return new double[] {};
        }

        return new double[] {
            (-b - Math.sqrt(delta))/(2*a),
            (-b + Math.sqrt(delta))/(2*a)
        };
    }

    @Override
    protected IVector normalLocally(IPoint point) {
        return new Vector(point.get(0), 0, point.get(2));
    }
}
