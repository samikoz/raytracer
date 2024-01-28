package io.raytracer.demos;

import io.raytracer.algebra.ThreeTransform;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.shapes.*;
import io.raytracer.textures.MonocolourTexture;
import io.raytracer.tools.IPicture;
import io.raytracer.tools.LinearColour;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Paths;

@Getter
public class Corridor extends Painter {

    public Corridor(DemoSetup setup) {
        super(setup);
    }

    @Override
    protected void setSetup() {

    }

    @Override
    protected Hittable[] makeObjects() {
        int radius = 10;

        Material material = Material.builder()
                .texture(new MonocolourTexture(new LinearColour(0.65)))
                .build();
        Material lightMaterial = Material.builder().emit(new LinearColour(120)).build();

        Shape torus = new Torus(radius, 2, material);
        Shape light = new Sphere(lightMaterial);
        light.setTransform(ThreeTransform.translation(radius, 0, 0.7).rotate_z(-Math.PI));
        return new Hittable[] {torus, light};
    }

    public static void main(String[] args) throws IOException {
        int size = 1080;
        int rayCount = 10;
        int radius = 10;
        String filename = "./outputs/trash/cortest7.ppm";

        IVector lowEye = new Vector(0, -radius, 0).transform(ThreeTransform.rotation_z(Math.PI / 10));
        DemoSetup setup = DemoSetup.builder()
                .rayCount(rayCount)
                .xSize(size)
                .ySize(size)
                .viewAngle(Math.PI / 4)
                .upDirection(new Vector(0, -1, 0.1))
                .eyePosition(new Point(lowEye.x()-radius, lowEye.y(), lowEye.z()))
                .lookDirection(new Vector(1, -0.1, 0.15))
                .build();
        Painter painter = new Corridor(setup);
        IPicture rendered = painter.render();
        rendered.export(Paths.get(filename));
    }
}
