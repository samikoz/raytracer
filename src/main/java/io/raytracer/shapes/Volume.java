package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.RayHit;
import lombok.NonNull;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

import static java.lang.Math.log;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class Volume extends Shape {
    private final double densityParameter;
    private final Shape boundary; //assumed convex

    private static final Random randGen = new Random();

    public Volume(double density, Shape boundary) {
        this.densityParameter = -1/density;
        this.boundary = boundary;
    }

    public Volume(double density, Shape boundary, @NonNull Material material) {
        super(material);
        this.densityParameter = -1/density;
        this.boundary = boundary;
    }

    @Override
    public void setTransform(ITransform transform) {
        super.setTransform(transform);
        this.boundary.setTransform(transform);
    }

    @Override
    protected double[] getLocalIntersectionPositions(IRay ray, double tmin, double tmax) {
        Intersection[] boundaryIntersections = this.boundary.intersect(ray, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        Optional<RayHit> leftHit = RayHit.fromIntersections(Arrays.asList(boundaryIntersections));
        if (!leftHit.isPresent()) {
            return new double[]{};
        }
        double leftParameter = leftHit.get().rayParameter;
        Intersection[] subsequentIntersections = this.boundary.intersect(ray, leftParameter + 0.0001, Double.POSITIVE_INFINITY);
        Optional<RayHit> rightHit = RayHit.fromIntersections(Arrays.asList(subsequentIntersections));
        if (!rightHit.isPresent()) {
            return new double[]{};
        }
        double rightParameter = rightHit.get().rayParameter;
        leftParameter = max(tmin, leftParameter);
        rightParameter = min(tmax, rightParameter);
        if (leftParameter >= rightParameter) {
            return new double[]{};
        }
        leftParameter = max(leftParameter, 0);

        double rayLength = ray.getDirection().norm();
        double distInside = (rightParameter - leftParameter)*rayLength;
        double hitDist = this.densityParameter*log(Volume.randGen.nextDouble());
        if (hitDist > distInside) {
            return new double[]{};
        }
        return new double[] { leftParameter + (hitDist / rayLength)};

    }

    @Override
    protected IVector normalLocally(IPoint point) {
        return new Vector(1, 0, 0);
    }
}
