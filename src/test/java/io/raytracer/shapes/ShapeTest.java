package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.textures.StripedTexture;
import io.raytracer.textures.Texture;
import io.raytracer.tools.LinearColour;
import io.raytracer.tools.IColour;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShapeTest {
    @Test
    void getObjectColourForTransformedObject() {
        IColour white = new LinearColour(1, 1, 1);
        IColour black = new LinearColour(0, 0, 0);
        Material textureedMaterial = Material.builder()
                .texture(new StripedTexture(white, black))
                .ambient(1.0).diffuse(0.0).specular(0.0).shininess(200.0)
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
                .ambient(1.0).diffuse(0.0).specular(0.0).shininess(200.0)
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
                .ambient(1.0).diffuse(0.0).specular(0.0).shininess(200.0)
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
        Group group = new Group();
        group.setTransform(ThreeTransform.scaling(2, 2, 2));
        Shape sphere = new Sphere(Material.builder().texture(new StripedTexture(white, black)).build());
        group.add(sphere);

        IColour objectColour = sphere.getIntrinsicColour(new Point(2.5, 0, 0));

        assertEquals(black, objectColour);
    }

    @Test
    void transformToOwnSpace() {
        Group outerGroup = new Group();
        outerGroup.setTransform(ThreeTransform.rotation_y(Math.PI / 2));
        Group innerGroup = new Group();
        innerGroup.setTransform(ThreeTransform.scaling(2, 2, 2));
        outerGroup.add(innerGroup);
        Shape sphere = new Sphere();
        sphere.setTransform(ThreeTransform.translation(5, 0, 0));
        innerGroup.add(sphere);

        IPoint transformedPoint = sphere.transformToOwnSpace(new Point(-2, 0, -10));
        IPoint expectedPoint = new Point(0, 0, -1);

        assertEquals(expectedPoint, transformedPoint);
    }

    @Test
    void normalOnObjectInTransformedGroup() {
        Group outerGroup = new Group();
        outerGroup.setTransform(ThreeTransform.rotation_y(Math.PI / 2));
        Group innerGroup = new Group();
        innerGroup.setTransform(ThreeTransform.scaling(1, 2, 3));
        outerGroup.add(innerGroup);
        Shape sphere = new Sphere();
        sphere.setTransform(ThreeTransform.translation(5, 0, 0));
        innerGroup.add(sphere);

        IVector normal = sphere.normal(new Point(1.7321, 1.1547, -5.5774));
        IVector expectedNormal = new Vector(0.2857, 0.4286, -0.8571);

        assertEquals(expectedNormal, normal);
    }
}