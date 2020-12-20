package io.raytracer.light;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;

public class IlluminatedPoint {
    public Point point;
    public Vector normalVector;
    public Material material;

    public IlluminatedPoint(Point point, Vector normalVector, Material material) {
        this.point = point;
        this.normalVector = normalVector;
        this.material = material;
    }
}
