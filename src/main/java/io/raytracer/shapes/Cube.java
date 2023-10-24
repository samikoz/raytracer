package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
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
    protected Intersection[] getLocalIntersections(IRay ray, double tmin, double tmax) {
        double[] xIntersections = this.getAxisIntersections(ray.getOrigin().get(0), ray.getDirection().get(0));
        double[] yIntersections = this.getAxisIntersections(ray.getOrigin().get(1), ray.getDirection().get(1));
        double[] zIntersections = this.getAxisIntersections(ray.getOrigin().get(2), ray.getDirection().get(2));

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
        return Arrays.stream(is).filter(i -> i.rayParameter > tmin && i.rayParameter < tmax).toArray(Intersection[]::new);
    }

    private double[] getAxisIntersections(double originComponent, double directionComponent) {
        double oneIntersection = (-1 - originComponent)/directionComponent;
        double otherIntersection = (1 - originComponent)/directionComponent;
        return new double[] {Math.min(oneIntersection, otherIntersection), Math.max(oneIntersection, otherIntersection)};
    }

    private double[] getMappingParameters(IPoint intersectionPoint) {
        double[] sideIntersectionCoords = Arrays.stream(new double[]{intersectionPoint.get(0), intersectionPoint.get(1), intersectionPoint.get(2)})
            .filter(coord -> Math.abs(coord - 1) > 1e-3 && Math.abs(coord + 1) > 1e-3).toArray();
        if (sideIntersectionCoords.length != 2) {
            return new double[] {1, 1};
        }
        return Arrays.stream(sideIntersectionCoords).map(coord -> (1+coord)/2).toArray();
    }

    @Override
    protected IVector localNormalAt(IPoint point, double u, double v) {
        double maxComponent = Math.max(Math.abs(point.get(0)), Math.max(Math.abs(point.get(1)), Math.abs(point.get(2))));

        if (maxComponent == Math.abs(point.get(0))) {
            return new Vector(point.get(0), 0, 0);
        } else if (maxComponent == Math.abs(point.get(1))) {
            return new Vector(0, point.get(1), 0);
        }
        return new Vector(0, 0, point.get(2));
    }

    @Override
    public BBox getLocalBoundingBox() {
        return new BBox(new Point(-1, -1, -1), new Point(1, 1, 1));
    }
}
