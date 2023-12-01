package io.raytracer.tools;

import io.raytracer.algebra.FourMatrix;
import io.raytracer.geometry.IPoint;
import io.raytracer.algebra.ISquareMatrix;
import io.raytracer.algebra.ITransform;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import io.raytracer.algebra.ThreeTransform;
import io.raytracer.geometry.Vector;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Ray;
import io.raytracer.shapes.Plane;
import lombok.Getter;

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
    @Getter private final IPoint eyePosition;
    @Getter private final IVector lookDirection;

    public Camera(int hsize, int vsize, double fieldOfView, IPoint eyePosition, IVector lookDirection, IVector upDirection) {
        this.pictureWidthPixels = hsize;
        this.pictureHeightPixels = vsize;
        this.fieldOfView = fieldOfView;
        this.eyePosition = eyePosition;
        this.lookDirection = lookDirection;
        this.inverseWorld = this.makeViewTransformation(eyePosition, lookDirection, upDirection);
        this.worldTransformation = this.inverseWorld.inverse();

        computePixelSize();
    }
    public abstract Collection<IRay> getRaysThrough(Pixel p);

    public IRay getRayThrough(Pixel p) {
        double canvasXOffset = (p.x + 0.5) * this.pixelSize;
        double canvasYOffset = (p.y + 0.5) * this.pixelSize;

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

    public Optional<Pixel> projectOnSensorPlane(IPoint p) {
        IPoint pCameraCoords = this.inverseWorld.act(p);
        Optional<IPoint> projected = pCameraCoords.project(new Plane(new Vector(0, 0, 1), new Point(0, 0, -1)), new Point(0, 0, 0));
        if (projected.isPresent()) {
            IPoint projectedPoint = projected.get();
            IPoint reflectedImage = new Point(this.pictureHalfWidth - projectedPoint.x(), this.pictureHalfHeight - projectedPoint.y(), projectedPoint.z());
            Pixel pixel = new Pixel(Math.round((reflectedImage.x() / this.pixelSize)), Math.round((reflectedImage.y() / this.pixelSize)));
            return Optional.of(pixel);
        }
        return Optional.empty();
    }
}
