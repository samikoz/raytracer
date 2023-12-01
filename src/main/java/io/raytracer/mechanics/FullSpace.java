package io.raytracer.mechanics;

import io.raytracer.algebra.ITransform;
import io.raytracer.geometry.Interval;

public class FullSpace extends BBox {
    public FullSpace() {
        super();
    }

    @Override
    public BBox transform(ITransform t) {
        return this;
    }

    @Override
    public boolean isHit(IRay ray, Interval range) {
        return true;
    }
}
