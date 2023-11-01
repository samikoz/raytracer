package io.raytracer.shapes;

import io.raytracer.materials.Material;

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
}
