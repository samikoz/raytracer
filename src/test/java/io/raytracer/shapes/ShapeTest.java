package io.raytracer.shapes;

import io.raytracer.algebra.ThreeTransform;
import io.raytracer.geometry.*;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.Intersection;
import io.raytracer.mechanics.Ray;
import io.raytracer.mechanics.TextureParameters;
import io.raytracer.textures.StripedTexture;
import io.raytracer.textures.Texture;
import io.raytracer.tools.IColour;
import io.raytracer.tools.LinearColour;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShapeTest {
    @Test
    void getObjectColourForTransformedObject() {
        IColour white = new LinearColour(1, 1, 1);
        IColour black = new LinearColour(0, 0, 0);
        Material textureedMaterial = Material.builder()
                .texture(new StripedTexture(white, black))
                .build();
        Shape sphere = new Sphere(textureedMaterial);
        sphere.setTransform(ThreeTransform.scaling(2, 2, 2));

        IColour colour = sphere.getIntrinsicColour(new Point(1.5, 0, 0));

        assertEquals(white, colour);
    }

    @Test
    void getObjectColourForTransformedTexture() {
        IColour white = new LinearColour(1, 1, 1);
        IColour black = new LinearColour(0, 0, 0);
        Texture stripedTexture = new StripedTexture(white, black);
        stripedTexture.setTransform(ThreeTransform.scaling(2, 2, 2));
        Material textureedMaterial = Material.builder()
                .texture(stripedTexture)
                .build();
        Shape sphere = new Sphere(textureedMaterial);

        IColour colour = sphere.getIntrinsicColour(new Point(1.5, 0, 0));

        assertEquals(white, colour);
    }

    @Test
    void getObjectColourForBothTransformedObjectAndTexture() {
        IColour white = new LinearColour(1, 1, 1);
        IColour black = new LinearColour(0, 0, 0);
        Texture stripedTexture = new StripedTexture(white, black);
        stripedTexture.setTransform(ThreeTransform.translation(0.5, 0, 0));
        Material textureedMaterial = Material.builder()
                .texture(stripedTexture)
                .build();
        Shape sphere = new Sphere(textureedMaterial);
        sphere.setTransform(ThreeTransform.scaling(2, 2, 2));

        IColour firstColour = sphere.getIntrinsicColour(new Point(1.6, 0, 0));
        IColour secondColour = sphere.getIntrinsicColour(new Point(3.1, 0, 0));

        assertEquals(white, firstColour);
        assertEquals(black, secondColour);
    }

    @Test
    void patternOnObjectInTransformedGroup() {
        IColour white = new LinearColour(1, 1, 1);
        IColour black = new LinearColour(0, 0, 0);
        Shape sphere = new Sphere(Material.builder().texture(new StripedTexture(white, black)).build());
        Group group = new Group(new Hittable[]{sphere});
        group.setTransform(ThreeTransform.scaling(2, 2, 2));

        IColour objectColour = sphere.getIntrinsicColour(new Point(2.5, 0, 0));

        assertEquals(black, objectColour);
    }

    @Test
    void transformToOwnSpace() {
        Shape sphere = new Sphere();
        sphere.setTransform(ThreeTransform.translation(5, 0, 0));
        Group innerGroup = new Group(new Hittable[] {sphere});
        innerGroup.setTransform(ThreeTransform.scaling(2, 2, 2));
        Group outerGroup = new Group(new Hittable[] {innerGroup});
        outerGroup.setTransform(ThreeTransform.rotation_y(Math.PI / 2));

        IPoint transformedPoint = sphere.transformToOwnSpace(new Point(-2, 0, -10));
        IPoint expectedPoint = new Point(0, 0, -1);

        assertEquals(expectedPoint, transformedPoint);
    }

    @Test
    void normalOnObjectInTransformedGroup() {
        Shape sphere = new Sphere();
        sphere.setTransform(ThreeTransform.translation(5, 0, 0));
        Group innerGroup = new Group(new Hittable[] {sphere});
        innerGroup.setTransform(ThreeTransform.scaling(1, 2, 3));
        Group outerGroup = new Group(new Hittable[] {innerGroup});
        outerGroup.setTransform(ThreeTransform.rotation_y(Math.PI / 2));

        Intersection testIntersection = new Intersection(
            sphere,
            new Ray(new Point(1.7321, 1.1547, -5.5774), new Vector(0, 0, 1)),
            0,
                new TextureParameters()
        );
        IVector normal = sphere.normal(testIntersection);
        IVector expectedNormal = new Vector(0.2857, 0.4286, -0.8571);

        assertEquals(expectedNormal, normal);
    }
}