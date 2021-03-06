package io.raytracer.drawing;

import io.raytracer.geometry.ThreeTransformation;

public class CameraImpl implements Camera {
    private final int hsize;
    private final int vsize;
    private final double fieldOfView;
    private final ThreeTransformation transformation;
    private double halfWidth;
    private double halfHeight;
    private double pixelSize;

    public CameraImpl(int hsize, int vsize, double fieldOfView) {
        this.hsize = hsize;
        this.vsize = vsize;
        this.fieldOfView = fieldOfView;
        this.transformation = new ThreeTransformation();
    }

    @Override
    public ThreeTransformation getTransformation() {
        return transformation;
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
