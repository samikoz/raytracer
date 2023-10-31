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

import java.util.Arrays;

public class Cube extends Shape {
    public Cube() {
        super();
    }

    public Cube(@NonNull Material material) {
        super(material);
    }

    @Override
    protected Intersection[] getLocalIntersections(IRay ray, Interval rayDomain) {
        double[] xIntersections = this.getAxisIntersections(ray.getOrigin().x(), ray.getDirection().x());
        double[] yIntersections = this.getAxisIntersections(ray.getOrigin().y(), ray.getDirection().y());
        double[] zIntersections = this.getAxisIntersections(ray.getOrigin().z(), ray.getDirection().z());

        double maxMinIntersection = Math.max(Math.max(xIntersections[0], yIntersections[0]), zIntersections[0]);
        double minMaxIntersection = Math.min(Math.min(xIntersections[1], yIntersections[1]), zIntersections[1]);

        if (maxMinIntersection > minMaxIntersection) {
            return new Intersection[] {};
        }
        //maps all sides to the same, linearly from (1,1,1)/(-1,-1,-1)
        double[] maxMinMappingParameters = this.getMappingParameters(ray.getPosition(maxMinIntersection));
        double[] minMaxMappingParameters = this.getMappingParameters(ray.getPosition(minMaxIntersection));
        Intersection[] is = new Intersection[] {
            new Intersection(this, ray, maxMinIntersection, new TextureParameters(maxMinMappingParameters[0], maxMinMappingParameters[1])),
            new Intersection(this, ray, minMaxIntersection, new TextureParameters(minMaxMappingParameters[0], minMaxMappingParameters[1]))
        };
        return Arrays.stream(is).filter(i -> i.rayParameter > rayDomain.min && i.rayParameter < rayDomain.max).toArray(Intersection[]::new);
    }

    private double[] getAxisIntersections(double originComponent, double directionComponent) {
        double oneIntersection = (-1 - originComponent)/directionComponent;
        double otherIntersection = (1 - originComponent)/directionComponent;
        return new double[] {Math.min(oneIntersection, otherIntersection), Math.max(oneIntersection, otherIntersection)};
    }

    private double[] getMappingParameters(IPoint intersectionPoint) {
        double[] sideIntersectionCoords = Arrays.stream(new double[]{intersectionPoint.x(), intersectionPoint.y(), intersectionPoint.z()})
            .filter(coord -> Math.abs(coord - 1) > 1e-3 && Math.abs(coord + 1) > 1e-3).toArray();
        if (sideIntersectionCoords.length != 2) {
            return new double[] {1, 1};
        }
        return Arrays.stream(sideIntersectionCoords).map(coord -> (1+coord)/2).toArray();
    }

    @Override
    protected IVector localNormalAt(IPoint point, double u, double v) {
        double maxComponent = Math.max(Math.abs(point.x()), Math.max(Math.abs(point.y()), Math.abs(point.z())));

        if (maxComponent == Math.abs(point.x())) {
            return new Vector(point.x(), 0, 0);
        } else if (maxComponent == Math.abs(point.y())) {
            return new Vector(0, point.y(), 0);
        }
        return new Vector(0, 0, point.z());
    }

    @Override
    public BBox getLocalBoundingBox() {
        return new BBox(new Point(-1, -1, -1), new Point(1, 1, 1));
    }
}
