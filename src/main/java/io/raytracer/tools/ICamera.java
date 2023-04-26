package io.raytracer.tools;

import io.raytracer.geometry.ITransform;
import io.raytracer.mechanics.IRay;

import java.util.Collection;

public interface ICamera {
    int getPictureWidthPixels();
    int getPictureHeightPixels();
    Collection<IRay> getRaysThroughPixel(int x, int y);
    void transform(ITransform t);
}
