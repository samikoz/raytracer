package io.raytracer.tools;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ISquareMatrix;
import io.raytracer.geometry.SquareMatrix;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.IVector;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Ray;
import lombok.Getter;

import java.util.Collection;
import java.util.Collections;

public class Camera implements ICamera {
    @Getter private final int pictureWidthPixels;
    @Getter private final int pictureHeightPixels;
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

    @Override
    public Collection<IRay> getRaysThroughPixel(int x, int y) {
        double canvasXOffset = (x + 0.5) * this.pixelSize;
        double canvasYOffset = (y + 0.5) * this.pixelSize;

        double worldXCoordinate = this.pictureHalfWidth - canvasXOffset;
        double worldYCoordinate = this.pictureHalfHeight - canvasYOffset;

        IPoint pixel = worldTransformation.act(new Point(worldXCoordinate, worldYCoordinate, -1));
        IPoint origin = worldTransformation.act(new Point(0, 0, 0));
        IVector direction = (pixel.subtract(origin)).normalise();

        return Collections.singletonList(new Ray(origin, direction));
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

        ISquareMatrix originTransformation = new SquareMatrix(
                leftDirection.get(0), leftDirection.get(1), leftDirection.get(2), 0,
                realUpDirection.get(0), realUpDirection.get(1), realUpDirection.get(2), 0,
                -forwardDirection.get(0), -forwardDirection.get(1), -forwardDirection.get(2), 0,
                0, 0, 0, 1);

        return ThreeTransform.translation(-eyePosition.get(0), -eyePosition.get(1), -eyePosition.get(2))
                .transform(originTransformation);
    }

    @Override
    public void transform(ITransform t) {
        this.worldTransformation = ThreeTransform.transformation(t.inverse().getMatrix().multiply(this.worldTransformation.getMatrix()));
    }
}
