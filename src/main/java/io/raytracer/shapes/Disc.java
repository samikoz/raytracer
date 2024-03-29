package io.raytracer.shapes;

import io.raytracer.geometry.Point;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.BBox;

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

    @Override
    protected BBox getLocalBoundingBox() {
        return new BBox(new Point(-1, -1, -BBox.paddingMargin), new Point(1, 1, BBox.paddingMargin));
    }
}
