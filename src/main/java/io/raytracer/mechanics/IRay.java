package io.raytracer.mechanics;

import io.raytracer.geometry.ILine;
import io.raytracer.algebra.ITransform;

public interface IRay extends ILine {
    IRay getTransformed(ITransform t);
}
