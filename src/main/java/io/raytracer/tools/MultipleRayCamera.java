package io.raytracer.tools;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.mechanics.IRay;

import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MultipleRayCamera extends Camera {
    private final int rayCount;
    private final Random randGen;

    public MultipleRayCamera(int rayCount, int hsize, int vsize, double fieldOfView) {
        super(hsize, vsize, fieldOfView);
        this.randGen = new Random();
        this.rayCount = rayCount;
    }

    public MultipleRayCamera(int rayCount, int hsize, int vsize, double fieldOfView, IPoint eyePosition, IVector lookDirection, IVector upDirection) {
        super(hsize, vsize, fieldOfView, eyePosition, lookDirection, upDirection);
        this.randGen = new Random();
        this.rayCount = rayCount;
    }

    @Override
    public Collection<IRay> getRaysThroughPixel(int x, int y) {
        return IntStream.range(0, rayCount).mapToObj(i -> this.getRayThroughPixel(x + this.randGen.nextDouble() - 0.5, y + this.randGen.nextDouble() - 0.5))
                .collect(Collectors.toList());
    }
}
