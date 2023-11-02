package io.raytracer.shapes;

import io.raytracer.geometry.Point;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.BBox;

public class Rectangle extends FiniteFlatShape {
    public Rectangle() {
        super();
    }

    public Rectangle(Material material) {
        super(material);
    }

    @Override
    boolean hitCondition(ZeroZData data) {
        return (int)data.x == 0 && (int)data.y == 0;
    }

    @Override
    protected BBox getLocalBoundingBox() {
        return new BBox(new Point(0, 0, -BBox.paddingMargin), new Point(1, 1, BBox.paddingMargin));
    }
}
