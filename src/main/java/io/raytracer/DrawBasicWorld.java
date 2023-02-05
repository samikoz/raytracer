package io.raytracer;

import io.raytracer.drawing.ICamera;
import io.raytracer.drawing.Camera;
import io.raytracer.drawing.IPicture;
import io.raytracer.drawing.Colour;
import io.raytracer.worldly.Material;
import io.raytracer.worldly.drawables.Sphere;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Vector;
import io.raytracer.worldly.LightSource;
import io.raytracer.worldly.IWorld;
import io.raytracer.worldly.World;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class DrawBasicWorld {
    public static void main(String[] args) throws IOException {
        Material wallMaterial = Material.builder()
                .colour(new Colour(1, 0.9, 0.9))
                .specular(0)
                .ambient(0.1)
                .diffuse(0.9)
                .shininess(200)
                .build();
        Sphere floor = new Sphere(wallMaterial);
        floor.setTransform(ThreeTransform.scaling(10, 0.01, 10));

        Sphere leftWall = new Sphere(wallMaterial);
        leftWall.setTransform(ThreeTransform
                .scaling(10, 0.01, 10)
                .rotate_x(Math.PI / 2)
                .rotate_y(-Math.PI / 4)
                .translate(0, 0, 5));

        Sphere rightWall = new Sphere(wallMaterial);
        rightWall.setTransform(ThreeTransform
                .scaling(10, 0.01, 10)
                .rotate_x(Math.PI / 2)
                .rotate_y(Math.PI / 4)
                .translate(0, 0, 5)
        );

        Material sphereMaterial = Material.builder().diffuse(0.7).specular(0.3).ambient(0.1).shininess(200).build();

        Material middleMaterial = sphereMaterial.toBuilder().colour(new Colour(0.1, 1, 0.5)).build();
        Sphere middleSphere = new Sphere(middleMaterial);
        middleSphere.setTransform(ThreeTransform.translation(-0.5, 1, 0.5));

        Material rightMaterial = sphereMaterial.toBuilder().colour(new Colour(0.2, 0.1, 1)).build();
        Sphere rightSphere = new Sphere(rightMaterial);
        rightSphere.setTransform(ThreeTransform.scaling(0.5, 0.5, 0.5).translate(2, 0.5, -0.5));

        Material leftMaterial = sphereMaterial.toBuilder().colour(new Colour(1, 0.8, 1)).build();
        Sphere leftSphere = new Sphere(leftMaterial);
        leftSphere.setTransform(ThreeTransform
                .scaling(0.33, 0.33, 0.33).translate(-1.5, 0.33, -0.75));

        IWorld world = new World();
        world.put(new LightSource(new Colour(1, 1, 1), new Point(-10, 10, -10)));
        world.put(floor).put(leftWall).put(rightWall).put(middleSphere).put(leftSphere).put(rightSphere);

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
