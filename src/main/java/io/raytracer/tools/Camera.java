package io.raytracer.tools;

import io.raytracer.geometry.FourMatrix;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.ISquareMatrix;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.Vector;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Ray;
import io.raytracer.shapes.Plane;

import java.util.Collection;
import java.util.Optional;

public abstract class Camera {
    private final int pictureWidthPixels;
    private final int pictureHeightPixels;
    private final double fieldOfView;
    private ITransform worldTransformation;
    private ITransform inverseWorld;
    private double pictureHalfWidth;
    private double pictureHalfHeight;
    private double pixelSize;

    public Camera(int hsize, int vsize, double fieldOfView) {
        this.pictureWidthPixels = hsize;
        this.pictureHeightPixels = vsize;
        this.fieldOfView = fieldOfView;
        this.worldTransformation = new ThreeTransform();
        this.inverseWorld = new ThreeTransform();

        computePixelSize();
    }

    public Camera(int hsize, int vsize, double fieldOfView, IPoint eyePosition, IVector lookDirection, IVector upDirection) {
        this.pictureWidthPixels = hsize;
        this.pictureHeightPixels = vsize;
        this.fieldOfView = fieldOfView;
        this.worldTransformation = this.makeViewTransformation(eyePosition, lookDirection, upDirection).inverse();
        this.inverseWorld = this.worldTransformation.inverse();

        computePixelSize();
    }
    public abstract Collection<IRay> getRaysThroughPixel(Pixel p);

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
        // the picture == canvas is always one unit behind the camera
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
        this.worldTransformation = this.worldTransformation.transform(t.inverse());
        this.inverseWorld = this.worldTransformation.inverse();
    }

    public Optional<Pixel> projectOntoSensor(IPoint p) {
        IPoint pCameraCoords = this.inverseWorld.act(p);
        IPoint projectedPoint = pCameraCoords.project(new Plane(new Vector(0, 0, 1), new Point(0, 0, -1)), new Point(0, 0, 0));
        IPoint reflectedImage = new Point(this.pictureHalfWidth - projectedPoint.x(), this.pictureHalfHeight - projectedPoint.y(), projectedPoint.z());
        Pixel pixel = new Pixel((int)(reflectedImage.x()/this.pixelSize), (int)(reflectedImage.y()/this.pixelSize));
        if (pixel.x >= 0 && pixel.x < this.pictureWidthPixels && pixel.y > 0 && pixel.y < this.pictureHeightPixels) {
            return Optional.of(pixel);
        }
        return Optional.empty();
    }
}
