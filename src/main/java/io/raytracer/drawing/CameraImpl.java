package io.raytracer.drawing;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.PointImpl;
import io.raytracer.geometry.ThreeTransformation;
import io.raytracer.geometry.Transformation;
import io.raytracer.geometry.Vector;
import io.raytracer.geometry.VectorImpl;
import io.raytracer.light.Ray;
import io.raytracer.light.RayImpl;

public class CameraImpl implements Camera {
    private final int hsize;
    private final int vsize;
    private final double fieldOfView;
    private final Transformation transformation;
    private double halfWidth;
    private double halfHeight;
    private double pixelSize;

    public CameraImpl(int hsize, int vsize, double fieldOfView) {
        this.hsize = hsize;
        this.vsize = vsize;
        this.fieldOfView = fieldOfView;
        this.transformation = new ThreeTransformation();

        computePixelSize();
    }

    public CameraImpl(int hsize, int vsize, double fieldOfView, Transformation transformation) {
        this.hsize = hsize;
        this.vsize = vsize;
        this.fieldOfView = fieldOfView;
        this.transformation = transformation;

        computePixelSize();
    }

    @Override
    public Transformation getTransformation() {
        return transformation;
    }

    @Override
    public Ray rayThrough(int x, int y) {
        double canvasXOffset = (x + 0.5) * this.pixelSize;
        double canvasYOffset = (y + 0.5) * this.pixelSize;

        double worldXCoordinate = this.halfWidth - canvasXOffset;
        double worldYCoordinate = this.halfHeight - canvasYOffset;

        Transformation worldTransformation = this.transformation.inverse();

        Point pixel = worldTransformation.act(new PointImpl(worldXCoordinate, worldYCoordinate, -1));
        Point origin = worldTransformation.act(new PointImpl(0, 0, 0));
        Vector direction = (pixel.subtract(origin)).normalise();

        return new RayImpl(origin, direction);
    }

    private void computePixelSize() {
        double halfView = Math.tan(fieldOfView / 2);
        double ratio = (double) hsize / vsize;

        if (ratio >= 1) {
            halfWidth = halfView;
            halfHeight = halfView / ratio;
        } else {
            halfWidth = halfView * ratio;
            halfHeight = halfView;
        }

        pixelSize = halfWidth * 2 / hsize;
    }
}
