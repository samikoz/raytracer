package io.raytracer.demos;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.LambertianWorld;
import io.raytracer.mechanics.Recasters;
import io.raytracer.shapes.Corner;
import io.raytracer.shapes.CubeCorner;
import io.raytracer.shapes.Shape;
import io.raytracer.textures.MonocolourTexture;
import io.raytracer.tools.Camera;
import io.raytracer.tools.IColour;
import io.raytracer.tools.IPicture;
import io.raytracer.tools.LinearColour;
import io.raytracer.tools.MultipleRayCamera;
import io.raytracer.tools.SingleRayCamera;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Function;

public class DepthIllusion {
    public static void main(String[] args) throws IOException {
        Material cubeMaterial = Material.builder().texture(new MonocolourTexture(new LinearColour(0.75, 0.2, 0.4))).build();
        cubeMaterial.addRecaster(Recasters.diffuse, 1);
        Shape corner = Corner.builder().material(cubeMaterial).build();
        corner.setTransform(ThreeTransform.rotation_y(-Math.PI/5).translate(1.5, 0, 0));

        Function<IRay, IColour> background = ray -> new LinearColour(0.5, 0.5, 0.5);
        LambertianWorld firstWorld = new LambertianWorld(background);
        firstWorld.put(corner);

        IPoint eyePosition = new Point(4, 2, 4);
        IVector lookDirection = new Point(0, 0, 0).subtract(eyePosition);
        IVector upDirection = new Vector(0, 1, 0);
        Camera camera = new MultipleRayCamera(3, 540, 540, Math.PI / 3, eyePosition, lookDirection, upDirection);

        String filename = "corner.ppm";
        PrintWriter writer = new PrintWriter(new FileWriter(filename));
        IPicture picture = firstWorld.render(camera);
        picture.export(writer);
    }
}