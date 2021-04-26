package io.raytracer.drawing;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.PointImpl;
import io.raytracer.geometry.SquareMatrix;
import io.raytracer.geometry.SquareMatrixImpl;
import io.raytracer.geometry.ThreeTransformation;
import io.raytracer.geometry.Transformation;
import io.raytracer.geometry.Vector;
import io.raytracer.light.Ray;
import io.raytracer.light.RayImpl;
import lombok.Getter;

public class CameraImpl implements Camera {
    @Getter private final int hsize;
    @Getter private final int vsize;
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

    public CameraImpl(int hsize, int vsize, double fieldOfView, Point eyePosition, Vector lookDirection, Vector upDirection) {
        this.hsize = hsize;
        this.vsize = vsize;
        this.fieldOfView = fieldOfView;
        this.transformation = this.makeViewTransformation(eyePosition, lookDirection, upDirection);

        computePixelSize();
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

    private ThreeTransformation makeViewTransformation(Point eyePosition, Vector lookDirection, Vector upDirection) {
        Vector forwardDirection = lookDirection.normalise();
        Vector upNormalised = upDirection.normalise();
        Vector leftDirection = forwardDirection.cross(upNormalised);
        Vector realUpDirection = leftDirection.cross(forwardDirection);

        SquareMatrix originTransformation = new SquareMatrixImpl(
                leftDirection.get(0), leftDirection.get(1), leftDirection.get(2), 0,
                realUpDirection.get(0), realUpDirection.get(1), realUpDirection.get(2), 0,
                -forwardDirection.get(0), -forwardDirection.get(1), -forwardDirection.get(2), 0,
                0, 0, 0, 1);

        return ThreeTransformation.translation(-eyePosition.get(0), -eyePosition.get(1), -eyePosition.get(2))
                .transform(originTransformation);
    }
}
