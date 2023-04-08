package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.IRay;
import lombok.NonNull;

public class Cube extends Shape {
    Cube() {
        super();
    }

    Cube(@NonNull Material material) {
        super(material);
    }

    @Override
    protected double[] getLocalIntersectionPositions(IRay ray) {
        double[] xIntersections = this.getAxisIntersections(ray.getOrigin().get(0), ray.getDirection().get(0));
        double[] yIntersections = this.getAxisIntersections(ray.getOrigin().get(1), ray.getDirection().get(1));
        double[] zIntersections = this.getAxisIntersections(ray.getOrigin().get(2), ray.getDirection().get(2));

        double maxMinIntersection = Math.max(Math.max(xIntersections[0], yIntersections[0]), zIntersections[0]);
        double minMaxIntersection = Math.min(Math.min(xIntersections[1], yIntersections[1]), zIntersections[1]);

        if (maxMinIntersection > minMaxIntersection) {
            return new double[] {};
        }

        return new double[] { maxMinIntersection, minMaxIntersection };
    }

    private double[] getAxisIntersections(double originComponent, double directionComponent) {
        double oneIntersection = (-1 - originComponent)/directionComponent;
        double otherIntersection = (1 - originComponent)/directionComponent;
        return new double[] {Math.min(oneIntersection, otherIntersection), Math.max(oneIntersection, otherIntersection)};
    }

    @Override
    protected IVector normalLocally(IPoint point) {
        double maxComponent = Math.max(Math.abs(point.get(0)), Math.max(Math.abs(point.get(1)), Math.abs(point.get(2))));

        if (maxComponent == Math.abs(point.get(0))) {
            return new Vector(point.get(0), 0, 0);
        } else if (maxComponent == Math.abs(point.get(1))) {
            return new Vector(0, point.get(1), 0);
        }
        return new Vector(0, 0, point.get(2));
    }
}
