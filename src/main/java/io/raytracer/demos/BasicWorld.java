package io.raytracer.demos;

import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.LambertianWorld;
import io.raytracer.shapes.Plane;
import io.raytracer.shapes.Rectangle;
import io.raytracer.shapes.Shape;
import io.raytracer.textures.BlendTexture;
import io.raytracer.textures.StripedTexture;
import io.raytracer.textures.Texture;
import io.raytracer.tools.Camera;
import io.raytracer.tools.GammaColour;
import io.raytracer.tools.LinearColour;
import io.raytracer.tools.MultipleRayCamera;
import io.raytracer.tools.IColour;
import io.raytracer.tools.IPicture;
import io.raytracer.materials.Material;
import io.raytracer.shapes.Sphere;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Vector;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Function;

public class BasicWorld {
    public static void main(String[] args) throws IOException {
        IColour white = new LinearColour(1, 1, 1);
        IColour halfRed = new LinearColour(0.5, 0, 0);
        Texture firstStripes = new StripedTexture(white, halfRed);
        Texture secondStripes = new StripedTexture(white, halfRed);
        secondStripes.setTransform(ThreeTransform.rotation_y(-Math.PI / 2));
        Texture blendTexture = new BlendTexture(firstStripes, secondStripes);
        Material floorMaterial = Material.builder()
                .texture(blendTexture)
                .build();
        Shape floor = new Plane(floorMaterial);
        floor.setTransform(ThreeTransform.scaling(100, 100, 100).translate(0, -100.5, -4));

        Shape rect = new Rectangle(floorMaterial);
        rect.setTransform(ThreeTransform.rotation_y(Math.PI/3).translate(0, 0, -4));

        Function<IRay, IColour> background = ray -> {
            double t = 0.5*(ray.getDirection().normalise().get(1) + 1);
            return (new GammaColour(1, 1, 1)).interpolate(new GammaColour(0.6, 0.6, 0.6), t);
        };
        LambertianWorld world = new LambertianWorld(background);
        world.put(floor).put(rect);

        IPoint eyePosition = new Point(0, 0.5, 0);
        IVector lookDirection = new Point(0, 0, -4).subtract(eyePosition);
        IVector upDirection = new Vector(0, 1, 0);
        Camera camera = new MultipleRayCamera(2, 600, 300, Math.PI / 3, eyePosition, lookDirection, upDirection);

        String filename = "basic_world.ppm";
        PrintWriter writer = new PrintWriter(new FileWriter(filename));
        IPicture picture = world.render(camera);
        picture.export(writer);
    }
}