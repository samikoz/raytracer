package io.raytracer.demos;

import io.raytracer.algebra.ThreeTransform;
import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.materials.Material;
import io.raytracer.shapes.Shape;
import io.raytracer.shapes.Torus;
import io.raytracer.textures.MonocolourTexture;
import io.raytracer.tools.LinearColour;

import java.io.IOException;
import java.nio.file.Paths;

public class PolyAccumulator {
    public static void main(String[] args) throws IOException {
        int radius = 12;
        DemoSetup base = DemoSetup.builder()
                .rayCount(100)
                .xSize(1080)
                .ySize(1080)
                .build();
        IPoint eyePosition = new Point(new Vector(-radius, 0, 0).transform(ThreeTransform.rotation_z(Math.PI / 10)));
        Material material = Material.builder()
                .texture(new MonocolourTexture(new LinearColour(0.65)))
                .build();

        Shape leftTorus = new Torus(radius, 2, material);
        ThreeTransform torusRotation = ThreeTransform.rotation_y(-Math.PI/12).rotate_x(-Math.PI/5.5);
        leftTorus.setTransform(torusRotation.conjugateTranslating(eyePosition));
        DemoSetup leftSetup = base.toBuilder()
                .brightness(16)
                .injectedObject(leftTorus)
                .build();
        Painter leftNostril = new Nose(leftSetup);

        Shape rightTorus = new Torus(radius, 2, material);
        DemoSetup rightSetup = base.toBuilder()
                .brightness(16)
                .injectedObject(rightTorus)
                .build();
        Painter rightNostril = new Nose(rightSetup);

        Accumulator leftAccumulator = new Accumulator(leftNostril, Paths.get("./buffs/newLeftNostril/"), "./outputs/holes/newLeft%02d.ppm");
        Accumulator rightAccumulator = new Accumulator(rightNostril, Paths.get("./buffs/newRightNostril/"), "./outputs/holes/newRight%02d.ppm");

        while (true) {
            leftAccumulator.accumulate();
            rightAccumulator.accumulate();
        }
    }
}
