package io.raytracer.demos;

import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.LambertianWorld;
import io.raytracer.shapes.Cylinder;
import io.raytracer.shapes.Shape;
import io.raytracer.tools.Camera;
import io.raytracer.tools.MultipleRayCamera;
import io.raytracer.tools.SingleRayCamera;
import io.raytracer.tools.IColour;
import io.raytracer.tools.IPicture;
import io.raytracer.tools.Colour;
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
import io.raytracer.mechanics.PhongWorld;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Function;

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

        Texture rightTexture = new MonocolourTexture(new Colour(1, 0.965, 0));
        Material rightMaterial = sphereMaterial.toBuilder().texture(rightTexture).reflectivity(0.5).build();
        Shape rightShape = new Cylinder(rightMaterial);
        rightShape.setTransform(ThreeTransform.scaling(0.5, 0.5, 0.5).translate(2, 0.5, 1));

        Function<IRay, IColour> background = ray -> {
            double t = 0.5*ray.getDirection().normalise().get(1) + 1;
            return (new Colour(1, 1, 1)).multiply(1-t).add(new Colour(0.5, 0.7, 1.0)).multiply(t);
        };
        LambertianWorld world = new LambertianWorld(background);
        //world.put(new LightSource(new Colour(1, 1, 1), new Point(-10, 10, -10)));
        world.put(floor).put(leftSphere).put(rightShape);

        IPoint eyePosition = new Point(0, 1.5, -5);
        IVector lookDirection = new Point(0, 1, 0).subtract(eyePosition);
        IVector upDirection = new Vector(0, 1, 0);
        Camera camera = new MultipleRayCamera(50, 600, 300, Math.PI / 3, eyePosition, lookDirection, upDirection);

        String filename = "basic_world.ppm";
        PrintWriter writer = new PrintWriter(new FileWriter(filename));
        IPicture picture = world.render(camera);
        picture.export(writer);
    }
}