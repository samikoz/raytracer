package io.raytracer;

import io.raytracer.drawing.Camera;
import io.raytracer.drawing.CameraImpl;
import io.raytracer.drawing.Canvas;
import io.raytracer.drawing.ColourImpl;
import io.raytracer.drawing.Material;
import io.raytracer.drawing.Sphere;
import io.raytracer.drawing.SphereImpl;
import io.raytracer.geometry.PointImpl;
import io.raytracer.geometry.ThreeTransformation;
import io.raytracer.geometry.Transformation;
import io.raytracer.geometry.VectorImpl;
import io.raytracer.light.LightSourceImpl;
import io.raytracer.light.World;
import io.raytracer.light.WorldImpl;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class DrawBasicWorld {
    public static void main(String[] args) throws IOException {
        Material wallMaterial = new Material();
        wallMaterial.colour = new ColourImpl(1, 0.9, 0.9);
        wallMaterial.specular = 0;
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

        Material middleMaterial = new Material();
        middleMaterial.colour = new ColourImpl(0.1, 1, 0.5);
        middleMaterial.diffuse = 0.7;
        middleMaterial.specular = 0.3;
        Sphere middleSphere = new SphereImpl(middleMaterial);
        middleSphere.setTransform(ThreeTransformation.translation(-0.5, 1, 0.5));

        Material rightMaterial = new Material();
        rightMaterial.colour = new ColourImpl(0.2, 0.1, 1);
        rightMaterial.diffuse = 0.7;
        rightMaterial.specular = 0.3;
        Sphere rightSphere = new SphereImpl(rightMaterial);
        rightSphere.setTransform(ThreeTransformation.scaling(0.5, 0.5, 0.5).translate(2, 0.5, -0.5));

        Material leftMaterial = new Material();
        leftMaterial.colour = new ColourImpl(1, 0.8, 1);
        leftMaterial.diffuse = 0.7;
        leftMaterial.specular = 0.3;
        Sphere leftSphere = new SphereImpl(leftMaterial);
        leftSphere.setTransform(ThreeTransformation.scaling(0.33, 0.33, 0.33).translate(-1.5, 0.33, -0.75));

        World world = new WorldImpl(
                new LightSourceImpl(new ColourImpl(1, 1, 1), new PointImpl(-10, 10, -10)));
        world.put(floor).put(leftWall).put(rightWall).put(middleSphere).put(leftSphere).put(rightSphere);

        Transformation cameraTransform = world.getViewTransformation(
                new PointImpl(0, 1.5, -5),
                new PointImpl(0, 1, 0),
                new VectorImpl(0, 1, 0));
        Camera camera = new CameraImpl(600, 300, Math.PI / 3, cameraTransform);

        String filename = "basic_world.ppm";
        PrintWriter writer = new PrintWriter(new FileWriter(filename));
        Canvas canvas = world.render(camera);
        canvas.export(writer);
    }
}
