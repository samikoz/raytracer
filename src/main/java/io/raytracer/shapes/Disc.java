package io.raytracer.shapes;

import io.raytracer.materials.Material;

public class Disc extends FiniteFlatShape {
    public Disc() {
        super();
    }

    public Disc(Material material) {
        super(material);
    }

    @Override
    boolean hitCondition(ZeroZData data) {
        return Math.pow(data.x,2) + Math.pow(data.y,2) < 1;
    }
}
