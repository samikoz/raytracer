package io.raytracer.worldly;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.worldly.drawables.Drawable;
import io.raytracer.worldly.drawables.Sphere;
import io.raytracer.worldly.materials.Glass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaterialPointTest {
    @Test
    void reflectanceOfAPerpendicularRay() {
        Drawable sphere = new Sphere(Glass.glassBuilder().build());
        IRay ray = new Ray(new Point(0, 0, 0), new Vector(0, 1, 0));
        MaterialPoint point = new MaterialPoint(
            sphere, new Point(0, 1, 0), ray, ray.getDirection().negate(), new Vector(0, -1, 0),
            1, 1.5, false
        );

        assertEquals(0.04, point.reflectance, 1e-3, "Reflectance is small for a perpendicular ray");
    }

    @Test
    void reflectanceForASmallAngleAndLargerSecondRefractiveIndex() {
        Drawable sphere = new Sphere(Glass.glassBuilder().build());
        IRay ray = new Ray(new Point(0, 0.99, -2), new Vector(0, 0, 1));
        MaterialPoint point = new MaterialPoint(
            sphere, new Point(0, 0.99, -0.1411), ray, ray.getDirection().negate(),
            new Vector(0, 0.99, -0.1411), 1, 1.5, false
        );

        assertEquals(0.48873, point.reflectance, 1e-3, "Reflectance is small for a perpendicular ray");
    }
}