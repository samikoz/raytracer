package io.raytracer.tools;

import io.raytracer.mechanics.IRay;

public interface ICamera {
    int getPictureWidthPixels();
    int getPictureHeightPixels();
    IRay getRayThroughPixel(int x, int y);
}
