package io.raytracer;

import io.raytracer.drawing.Camera;
import io.raytracer.drawing.CameraImpl;
import io.raytracer.drawing.Canvas;
import io.raytracer.drawing.ColourImpl;
import io.raytracer.drawing.Material;
import io.raytracer.drawing.Sphere;
import io.raytracer.drawing.SphereImpl;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.PointImpl;
import io.raytracer.geometry.ThreeTransformation;
import io.raytracer.geometry.Transformation;
import io.raytracer.geometry.Vector;
import io.raytracer.geometry.VectorImpl;
import io.raytracer.light.LightSourceImpl;
import io.raytracer.light.World;
import io.raytracer.light.WorldImpl;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class DrawBasicWorld {
    public static void main(String[] args) throws IOException {
        Material wallMaterial = Material.builder()
                .colour(new ColourImpl(1, 0.9, 0.9))
                .specular(0)
                .ambient(0.1)
                .diffuse(0.9)
                .shininess(200)
                .build();
        Sphere floor = new SphereImpl(wallMaterial);
        floor.setTransform(ThreeTransformation.scaling(10, 0.01, 10));

        Sphere leftWall = new SphereImpl(wallMaterial);
        leftWall.setTransform(ThreeTransformation
                .scaling(10, 0.01, 10)
                .rotate_x(Math.PI / 2)
                .rotate_y(-Math.PI / 4)
                .translate(0, 0, 5));

        Sphere rightWall = new SphereImpl(wallMaterial);
        rightWall.setTransform(ThreeTransformation
                .scaling(10, 0.01, 10)
                .rotate_x(Math.PI / 2)
                .rotate_y(Math.PI / 4)
                .translate(0, 0, 5)
        );

        Material sphereMaterial = Material.builder().diffuse(0.7).specular(0.3).ambient(0.1).shininess(200).build();

        Material middleMaterial = sphereMaterial.toBuilder().colour(new ColourImpl(0.1, 1, 0.5)).build();
        Sphere middleSphere = new SphereImpl(middleMaterial);
        middleSphere.setTransform(ThreeTransformation.translation(-0.5, 1, 0.5));

        Material rightMaterial = sphereMaterial.toBuilder().colour(new ColourImpl(0.2, 0.1, 1)).build();
        Sphere rightSphere = new SphereImpl(rightMaterial);
        rightSphere.setTransform(ThreeTransformation.scaling(0.5, 0.5, 0.5).translate(2, 0.5, -0.5));

        Material leftMaterial = sphereMaterial.toBuilder().colour(new ColourImpl(1, 0.8, 1)).build();
        Sphere leftSphere = new SphereImpl(leftMaterial);
        leftSphere.setTransform(ThreeTransformation
                .scaling(0.33, 0.33, 0.33).translate(-1.5, 0.33, -0.75));

        World world = new WorldImpl();
        world.put(new LightSourceImpl(new ColourImpl(1, 1, 1), new PointImpl(-10, 10, -10)));
        world.put(floor).put(leftWall).put(rightWall).put(middleSphere).put(leftSphere).put(rightSphere);

        Point eyePosition = new PointImpl(0, 1.5, -5);
        Vector lookDirection = new PointImpl(0, 1, 0).subtract(eyePosition);
        Vector upDirection = new VectorImpl(0, 1, 0);
        Camera camera = new CameraImpl(600, 300, Math.PI / 3, eyePosition, lookDirection, upDirection);

        String filename = "basic_world.ppm";
        PrintWriter writer = new PrintWriter(new FileWriter(filename));
        Canvas canvas = world.render(camera);
        canvas.export(writer);
    }
}
