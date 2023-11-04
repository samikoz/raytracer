package io.raytracer.mechanics;

import io.raytracer.geometry.ILine;
import io.raytracer.geometry.ITransform;

public interface IRay extends ILine {
    IRay getTransformed(ITransform t);
}
