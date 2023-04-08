package io.raytracer.demos;

import io.raytracer.tools.ICamera;
import io.raytracer.tools.Camera;
import io.raytracer.tools.IPicture;
import io.raytracer.tools.Colour;
import io.raytracer.textures.GradientTexture;
import io.raytracer.textures.MonocolourTexture;
import io.raytracer.textures.Texture;
import io.raytracer.textures.StripedTexture;
import io.raytracer.materials.Material;
import io.raytracer.shapes.Plane;
import io.raytracer.shapes.Sphere;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Vector;
import io.raytracer.mechanics.LightSource;
import io.raytracer.mechanics.IWorld;
import io.raytracer.mechanics.World;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class BasicWorld {
    public static void main(String[] args) throws IOException {
        Material wallMaterial = Material.builder()
                .texture(new MonocolourTexture(new Colour(1, 0.9, 0.9)))
                .specular(0.0)
                .ambient(0.1)
                .diffuse(0.9)
                .shininess(200.0)
                .reflectivity(0.5)
                .build();
        Plane floor = new Plane(wallMaterial);

        Material sphereMaterial = Material.builder().diffuse(0.7).specular(0.3).ambient(0.1).shininess(200.0).build();

        Material leftMaterial = sphereMaterial.toBuilder()
                .texture(new StripedTexture(new Colour(1, 1, 1), new Colour(0, 1, 0)))
                .transparency(0.5).refractiveIndex(1.5)
                .build();
        Sphere leftSphere = new Sphere(leftMaterial);
        leftSphere.setTransform(ThreeTransform.rotation_z(Math.PI / 4).translate(-1, 1, -1));

        Texture rightTexture = new GradientTexture(new Colour(1, 0, 0), new Colour(0, 0, 1));
        rightTexture.setTransform(ThreeTransform.translation(-0.5,0 ,0 ).rotate_z(Math.PI / 2).scale(2, 2, 2));
        Material rightMaterial = sphereMaterial.toBuilder().texture(rightTexture).build();
        Sphere rightSphere = new Sphere(rightMaterial);
        rightSphere.setTransform(ThreeTransform.scaling(0.5, 0.5, 0.5).translate(2, 0.5, 1));

        IWorld world = new World();
        world.put(new LightSource(new Colour(1, 1, 1), new Point(-10, 10, -10)));
        world.put(floor).put(leftSphere).put(rightSphere);

        IPoint eyePosition = new Point(0, 1.5, -5);
        IVector lookDirection = new Point(0, 1, 0).subtract(eyePosition);
        IVector upDirection = new Vector(0, 1, 0);
        ICamera camera = new Camera(600, 300, Math.PI / 3, eyePosition, lookDirection, upDirection);

        String filename = "basic_world.ppm";
        PrintWriter writer = new PrintWriter(new FileWriter(filename));
        IPicture picture = world.render(camera);
        picture.export(writer);
    }
}