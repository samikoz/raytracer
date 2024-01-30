package io.raytracer.demos;

import io.raytracer.algebra.ThreeTransform;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.mechanics.LambertianWorld;
import io.raytracer.mechanics.Recasters;
import io.raytracer.mechanics.World;
import io.raytracer.shapes.*;
import io.raytracer.textures.MonocolourTexture;
import io.raytracer.tools.IPicture;
import io.raytracer.tools.LinearColour;

import java.io.IOException;
import java.nio.file.Paths;

public class Nose extends Painter{
    private int radius = 12;
    public Nose(DemoSetup setup) {
        super(setup);
    }

    @Override
    protected DemoSetup setSetup(DemoSetup setup) {
        IVector lowEye = new Vector(-radius, 0, 0).transform(ThreeTransform.rotation_z(Math.PI / 10));
        return setup.toBuilder()
                .viewAngle(Math.PI / 2.7)
                .upDirection(new Vector(1, 0, 0.1))
                .eyePosition(new Point(lowEye.x(), lowEye.y(), lowEye.z()))
                .lookDirection(new Vector(0.5, -1, 0.4))
                .build();
    }

    @Override
    protected Hittable[] makeObjects() {
        Material ringMaterial = Material.builder()
                .texture(new MonocolourTexture(new LinearColour(0.65)))
                .recast(Recasters.fuzzilyReflective.apply(0.2), 0.5)
                .recast(Recasters.diffuse, 0.5)
                .build();
        Material lightMaterial = Material.builder().emit(new LinearColour(this.setup.brightness)).build();

        Shape light = new Sphere(lightMaterial);
        Shape noseRing = new Torus(2, 0.5, ringMaterial);
        IPoint ringPosition = this.setup.eyePosition.add(this.setup.lookDirection.multiply(4.5).add(new Vector(1.5, 0, 0.3)));
        noseRing.setTransform(ThreeTransform.rotation_x(Math.PI / 2).rotate_z(-Math.PI / 8).rotate_y(Math.PI/4).translate(ringPosition.x(), ringPosition.y(), ringPosition.z()));
        light.setTransform(ThreeTransform.translation(-radius, 0, 0));
        return new Hittable[] {this.setup.injectedObject, light, noseRing};
    }

    @Override
    protected World makeWorld() {
        Hittable[] objects = this.makeObjects();
        Hittable[] groupable = new Hittable[] {objects[1], objects[2]};
        Group group = new Group(groupable);
        World world = new LambertianWorld(this.backgroundColour());
        world.put(objects[0]);
        world.put(group);
        return world;
    }

    public static void main(String[] args) throws IOException {
        int size = 1080;
        int rayCount = 3;
        String filename = "./outputs/trash/modNose06_.ppm";

        DemoSetup setup = DemoSetup.builder()
                .rayCount(rayCount)
                .xSize(size)
                .ySize(size)
                .build();
        Painter painter = new Nose(setup);
        IPicture rendered = painter.render();
        rendered.export(Paths.get(filename));
    }
}
