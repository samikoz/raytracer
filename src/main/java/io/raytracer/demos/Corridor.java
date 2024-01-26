package io.raytracer.demos;

import io.raytracer.algebra.ThreeTransform;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.LambertianWorld;
import io.raytracer.mechanics.World;
import io.raytracer.shapes.*;
import io.raytracer.textures.MonocolourTexture;
import io.raytracer.tools.IColour;
import io.raytracer.tools.IPicture;
import io.raytracer.tools.LinearColour;

import java.io.IOException;
import java.nio.file.Paths;

public class Corridor {
    private final DemoSetup setup;

    public Corridor(DemoSetup setup) {
        this.setup = setup;
    }

    public IPicture render() throws IOException {
        int radius = 10;
        IVector eyePositionV = new Vector(0, -radius, 0).transform(ThreeTransform.rotation_z(Math.PI / 10));
        IColour backgroundColour = new LinearColour(0, 0, 0);

        //setups
        DemoSetup centralSetup = setup.toBuilder()
                .viewAngle(Math.PI / 4)
                .upDirection(new Vector(0, -1, 0.1))
                .eyePosition(new Point(eyePositionV.x()-radius, eyePositionV.y(), eyePositionV.z()))
                .lookDirection(new Vector(1, -0.1, 0.15))
                .build();

        Material material = Material.builder()
                .texture(new MonocolourTexture(new LinearColour(0.65)))
                .build();

        Shape torus = new Torus(10, 2, material);

        //light
        Material emitentMaterial = Material.builder().emit(new LinearColour(120)).build();
        Shape light = new Sphere(emitentMaterial);
        light.setTransform(ThreeTransform.translation(radius, 0, 0.7).rotate_z(-Math.PI));

        //worlds
        World centralWorld = new LambertianWorld(backgroundColour);
        centralWorld.put(light);
        centralWorld.put(new Group(new Hittable[] {torus, light}));

        //render
        IPicture picture = centralSetup.makePicture();
        centralWorld.render(picture, centralSetup.makeCamera());
        return picture;
    }

    public static void main(String[] args) throws IOException {
        int size = 1080;
        int rayCount = 100;
        String filename = "./outputs/trash/cortest5.ppm";

        DemoSetup setup = DemoSetup.builder()
                .rayCount(rayCount)
                .xSize(size)
                .ySize(size)
                .build();
        Corridor renderer = new Corridor(setup);
        IPicture rendered = renderer.render();
        rendered.export(Paths.get(filename));
    }
}
