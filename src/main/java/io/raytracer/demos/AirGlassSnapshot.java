package io.raytracer.demos;

import io.raytracer.drawing.ICamera;
import io.raytracer.drawing.Camera;
import io.raytracer.drawing.IPicture;
import io.raytracer.drawing.Colour;
import io.raytracer.drawing.patterns.CheckerPattern;
import io.raytracer.drawing.patterns.Monopattern;
import io.raytracer.worldly.drawables.Drawable;
import io.raytracer.worldly.materials.Glass;
import io.raytracer.worldly.materials.Material;
import io.raytracer.worldly.drawables.Plane;
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

public class AirGlassSnapshot {
    public static void render(double angle, String filename) throws IOException {
        int cubeHalfSide = 7;
        Material wallMaterial = Material.builder()
            .pattern(new CheckerPattern(new Colour(0.6, 0.6, 0.6), new Colour(0, 0, 0)))
            .ambient(1.0)
            .build();
        Plane backWall = new Plane(wallMaterial);
        backWall.setTransform(ThreeTransform.rotation_x(Math.PI/2).translate(0, 0, cubeHalfSide));
        Plane frontWall = new Plane(wallMaterial);
        frontWall.setTransform(ThreeTransform.rotation_x(Math.PI/2).translate(0, 0, -cubeHalfSide));
        Plane leftWall = new Plane(wallMaterial);
        leftWall.setTransform(ThreeTransform.rotation_z(Math.PI/2).translate(-cubeHalfSide, 0, 0));
        Plane rightWall = new Plane(wallMaterial);
        rightWall.setTransform(ThreeTransform.rotation_z(Math.PI/2).translate(cubeHalfSide, 0, 0));
        Plane topWall = new Plane(wallMaterial);
        topWall.setTransform(ThreeTransform.translation(0, cubeHalfSide, 0));
        Plane bottomWall = new Plane(wallMaterial);
        bottomWall.setTransform(ThreeTransform.translation(0, -cubeHalfSide, 0));

        Material glassMaterial = Glass.glassBuilder()
                .pattern(new Monopattern(new Colour(0, 0, 0)))
                .specular(0.9)
                .ambient(0.1)
                .shininess(250.0)
                .build();
        Drawable glassSphere = new Sphere(glassMaterial);

        Material airMaterial = Material.builder()
            .transparency(1.0)
            .reflectivity(0.9)
            .pattern(new Monopattern(new Colour(0, 0, 0)))
            .specular(0.1)
            .ambient(0.1)
            .shininess(20.0)
            .build();
        Drawable airSphere = new Sphere(airMaterial);
        airSphere.setTransform(ThreeTransform.scaling(0.5, 0.5, 0.5));

        IWorld world = new World();
        world.put(new LightSource(new Colour(1, 1, 1), new Point(-5, 5, -5)));
        world.put(backWall).put(frontWall).put(leftWall).put(rightWall).put(topWall).put(bottomWall).put(airSphere).put(glassSphere);

        IPoint eyePosition = new Point(0-3*Math.sin(angle), 0, -3*Math.cos(angle));
        IVector lookDirection = new Point(0, 0, 0).subtract(eyePosition);
        IVector upDirection = new Vector(0, 1, 0);
        ICamera camera = new Camera(1080, 1080, 3*Math.PI / 8, eyePosition, lookDirection, upDirection);

        PrintWriter writer = new PrintWriter(new FileWriter(filename));
        IPicture picture = world.render(camera);
        picture.export(writer);
    }
}