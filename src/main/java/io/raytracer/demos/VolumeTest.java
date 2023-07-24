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
import io.raytracer.shapes.Plane;
import io.raytracer.shapes.Rectangle;
import io.raytracer.shapes.Shape;
import io.raytracer.shapes.Sphere;
import io.raytracer.shapes.Volume;
import io.raytracer.textures.BlendTexture;
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

public class VolumeTest {
    public static void main(String[] args) throws IOException {
        Material floorMaterial = Material.builder()
                .texture(new MonocolourTexture(new LinearColour(0.2, 0.2, 0.2)))
                .build();
        floorMaterial.addRecaster(Recasters.diffuse, 1);
        Shape floor = new Plane(floorMaterial);

        Material emitent = Material.builder().emit(new LinearColour(5, 5, 5)).build();
        Shape rect = new Rectangle(emitent);
        rect.setTransform(ThreeTransform.rotation_y(Math.PI/2).translate(-1.5, 0, 0));

        Material volumeMaterial = Material.builder().texture(new MonocolourTexture(new LinearColour(1, 0, 0))).build();
        volumeMaterial.addRecaster(Recasters.isotropic, 1);
        Shape sphereBoundary = new Sphere();
        Shape volume = new Volume(0.1, sphereBoundary, volumeMaterial);

        Function<IRay, IColour> background = ray -> new LinearColour(0, 0, 0);
        LambertianWorld world = new LambertianWorld(background);
        world.put(floor).put(rect).put(volume);

        IPoint eyePosition = new Point(0, 1, -4);
        IVector lookDirection = new Point(0, 0, 0).subtract(eyePosition);
        IVector upDirection = new Vector(0, 1, 0);
        Camera camera = new MultipleRayCamera(20, 600, 300, Math.PI / 3, eyePosition, lookDirection, upDirection);

        String filename = "volume_test.ppm";
        PrintWriter writer = new PrintWriter(new FileWriter(filename));
        IPicture picture = world.render(camera);
        picture.export(writer);
    }
}