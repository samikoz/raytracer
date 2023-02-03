package io.raytracer.drawing;

import io.raytracer.worldly.IRay;

public interface ICamera {
    int getPictureWidthPixels();
    int getPictureHeightPixels();
    IRay getRayThroughPixel(int x, int y);
}
