package io.raytracer.tools;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.ISquareMatrix;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.FourMatrix;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Ray;

import java.util.Collection;

public abstract class Camera {
    private final int pictureWidthPixels;
    private final int pictureHeightPixels;
    private final double fieldOfView;
    private ITransform worldTransformation;
    private double pictureHalfWidth;
    private double pictureHalfHeight;
    private double pixelSize;

    public Camera(int hsize, int vsize, double fieldOfView) {
        this.pictureWidthPixels = hsize;
        this.pictureHeightPixels = vsize;
        this.fieldOfView = fieldOfView;
        this.worldTransformation = new ThreeTransform();

        computePixelSize();
    }

    public Camera(int hsize, int vsize, double fieldOfView, IPoint eyePosition, IVector lookDirection, IVector upDirection) {
        this.pictureWidthPixels = hsize;
        this.pictureHeightPixels = vsize;
        this.fieldOfView = fieldOfView;
        this.worldTransformation = this.makeViewTransformation(eyePosition, lookDirection, upDirection).inverse();

        computePixelSize();
    }
    public abstract Collection<IRay> getRaysThroughPixel(int x, int y);

    protected IRay getRayThroughPixel(double x, double y) {
        double canvasXOffset = (x + 0.5) * this.pixelSize;
        double canvasYOffset = (y + 0.5) * this.pixelSize;

        double worldXCoordinate = this.pictureHalfWidth - canvasXOffset;
        double worldYCoordinate = this.pictureHalfHeight - canvasYOffset;

        IPoint pixel = worldTransformation.act(new Point(worldXCoordinate, worldYCoordinate, -1));
        IPoint origin = worldTransformation.act(new Point(0, 0, 0));
        IVector direction = (pixel.subtract(origin)).normalise();

        return new Ray(origin, direction);
    }
    private void computePixelSize() {
        // the picture == canvas is always one unit away from the camera
        double halfView = Math.tan(fieldOfView / 2);
        double ratio = (double) pictureWidthPixels / pictureHeightPixels;

        if (ratio >= 1) {
            pictureHalfWidth = halfView;
            pictureHalfHeight = halfView / ratio;
        } else {
            pictureHalfHeight = halfView;
            pictureHalfWidth = halfView * ratio;
        }

        pixelSize = pictureHalfWidth * 2 / pictureWidthPixels;
    }

    private ThreeTransform makeViewTransformation(IPoint eyePosition, IVector lookDirection, IVector upDirection) {
        IVector forwardDirection = lookDirection.normalise();
        IVector upNormalised = upDirection.normalise();
        IVector leftDirection = forwardDirection.cross(upNormalised);
        IVector realUpDirection = leftDirection.cross(forwardDirection);

        ISquareMatrix originTransformation = new FourMatrix(
                leftDirection.x(), leftDirection.y(), leftDirection.z(), 0,
                realUpDirection.x(), realUpDirection.y(), realUpDirection.z(), 0,
                -forwardDirection.x(), -forwardDirection.y(), -forwardDirection.z(), 0,
                0, 0, 0, 1);

        return ThreeTransform.translation(-eyePosition.x(), -eyePosition.y(), -eyePosition.z())
                .transform(originTransformation);
    }

    public void transform(ITransform t) {
        this.worldTransformation = ThreeTransform.transformation(t.inverse().getMatrix().multiply(this.worldTransformation.getMatrix()));
    }
}
