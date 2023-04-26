package io.raytracer.tools;

import io.raytracer.geometry.ITransform;
import io.raytracer.mechanics.IRay;

public interface ICamera {
    int getPictureWidthPixels();
    int getPictureHeightPixels();
    IRay getRayThroughPixel(int x, int y);
    void transform(ITransform t);
}
