package io.raytracer.drawing;

import io.raytracer.geometry.ThreeTransformation;
import io.raytracer.geometry.Transformation;
import io.raytracer.light.Ray;

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
    }

    public CameraImpl(int hsize, int vsize, double fieldOfView, Transformation transformation) {
        this.hsize = hsize;
        this.vsize = vsize;
        this.fieldOfView = fieldOfView;
        this.transformation = transformation;
    }

    @Override
    public Transformation getTransformation() {
        return transformation;
    }

    @Override
    public Ray rayThrough(int x, int y) {
        return null;
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
