package io.raytracer.demos;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.LambertianWorld;
import io.raytracer.shapes.Plane;
import io.raytracer.shapes.Rectangle;
import io.raytracer.shapes.Shape;
import io.raytracer.shapes.Sphere;
import io.raytracer.textures.BlendTexture;
import io.raytracer.textures.CheckerTexture;
import io.raytracer.textures.MonocolourTexture;
import io.raytracer.textures.StripedTexture;
import io.raytracer.textures.Texture;
import io.raytracer.tools.Camera;
import io.raytracer.tools.GammaColour;
import io.raytracer.tools.IColour;
import io.raytracer.tools.IPicture;
import io.raytracer.tools.LinearColour;
import io.raytracer.tools.MultipleRayCamera;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Function;

public class Prison {
    public static void main(String[] args) throws IOException {
        Material floorMaterial = Material.builder()
            .texture(new CheckerTexture(new GammaColour(0, 0, 0), new GammaColour(1, 1, 1)))
            .build();
        Shape floor = new Plane(floorMaterial);

        Shape sphere = new Sphere(floorMaterial);
        sphere.setTransform(ThreeTransform.scaling(2, 2, 2).translate(3, 2, 1));

        Material emitent = Material.builder().emit(new GammaColour(4, 4, 4)).build();
        Shape rect = new Rectangle(emitent);
        rect.setTransform(ThreeTransform.scaling(2, 2, 1).translate(3, 1, -2));

        Function<IRay, IColour> background = ray -> new GammaColour(0, 0, 0);
        LambertianWorld world = new LambertianWorld(background);
        world.put(floor).put(rect).put(sphere);

        IPoint eyePosition = new Point(26, 3, 6);
        IVector lookDirection = new Point(0, 2, 0).subtract(eyePosition);
        IVector upDirection = new Vector(0, 1, 0);
        Camera camera = new MultipleRayCamera(200, 600, 300, Math.PI / 3, eyePosition, lookDirection, upDirection);

        String filename = "prison.ppm";
        PrintWriter writer = new PrintWriter(new FileWriter(filename));
        IPicture picture = world.render(camera);
        picture.export(writer);
    }
}