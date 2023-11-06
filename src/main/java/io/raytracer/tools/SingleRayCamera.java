package io.raytracer.tools;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.mechanics.IRay;

import java.util.Collection;
import java.util.Collections;

public class SingleRayCamera extends Camera {
    public SingleRayCamera(int hsize, int vsize, double fieldOfView) {
        super(hsize, vsize, fieldOfView);
    }

    public SingleRayCamera(int hsize, int vsize, double fieldOfView, IPoint eyePosition, IVector lookDirection, IVector upDirection) {
        super(hsize, vsize, fieldOfView, eyePosition, lookDirection, upDirection);
    }

    @Override
    public Collection<IRay> getRaysThroughPixel(Pixel pixel) {
        return Collections.singletonList(this.getRayThroughPixel(pixel.x, pixel.y));
    }
}
