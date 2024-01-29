package io.raytracer.demos;

import io.raytracer.algebra.ThreeTransform;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.shapes.Hittable;
import io.raytracer.shapes.Rectangle;
import io.raytracer.shapes.Shape;
import io.raytracer.shapes.Torus;
import io.raytracer.textures.MonocolourTexture;
import io.raytracer.tools.IPicture;
import io.raytracer.tools.LinearColour;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Paths;

@Getter
public class Tori extends Painter {

    public Tori(DemoSetup setup) {
        super(setup);
    }

    @Override
    protected DemoSetup setSetup(DemoSetup setup) {
        return setup.toBuilder()
                .viewAngle(Math.PI / 4)
                .upDirection(new Vector(0, 1, 0))
                .eyePosition(new Point(0, 8, 15))
                .lookDirection(new Vector(0, -8, -15))
                .build();
    }

    @Override
    protected Hittable[] makeObjects() {
        Material blockMaterial = Material.builder()
                .texture(new MonocolourTexture(new LinearColour(0.65)))
                .build();

        Shape torusFlat = new Torus(4, 2, blockMaterial);
        torusFlat.setTransform(ThreeTransform.translation(-2, -3.5, -1.5));
        Shape torusStand = new Torus(3, 1, blockMaterial);
        torusStand.setTransform(ThreeTransform.rotation_x(Math.PI/2 + Math.PI/9).rotate_z(Math.PI/11).translate(3, 1, -1.5));
        Shape torusOver = new Torus(8, 2, blockMaterial);
        torusOver.setTransform(ThreeTransform.rotation_x(Math.PI / 2 - Math.PI / 6).rotate_z(Math.PI / 3).translate(-2,-6, -3));

        int lightBrightness = 25;
        Material emitentMaterial = Material.builder().emit(new LinearColour(lightBrightness)).build();
        Shape light = new Rectangle(emitentMaterial);
        light.setTransform(ThreeTransform.rotation_y(Math.PI / 2).scale(5, 5, 1));

        return new Hittable[] {light, torusFlat, torusOver, torusStand};
    }

    public static void main(String[] args) throws IOException {
        int size = 1080;
        int rayCount = 100;
        String filename = "./outputs/tori/em/em11.ppm";

        DemoSetup setup = DemoSetup.builder()
                .rayCount(rayCount)
                .xSize(size)
                .ySize(size)
                .build();
        Painter painter = new Tori(setup);
        IPicture rendered = painter.render();
        rendered.export(Paths.get(filename));
    }
}
