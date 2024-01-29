package io.raytracer.demos;

import io.raytracer.algebra.ThreeTransform;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;

import java.io.IOException;
import java.nio.file.Paths;

public class PolyAccumulator {
    public static void main(String[] args) throws IOException {
        int radius = 10;
        DemoSetup base = DemoSetup.builder()
                .rayCount(250)
                .xSize(1080)
                .ySize(1080)
                .build();

        IVector highEye = new Vector(0, -radius, 0).transform(ThreeTransform.rotation_z(Math.PI / 10)).subtract(new Vector(radius, 0, 0)).multiply(0.8);
        DemoSetup highHoleSetup = base.toBuilder()
                .viewAngle(Math.PI / 2.7)
                .upDirection(new Vector(0, 1, 0.6))
                .eyePosition(new Point(highEye.x(), highEye.y(), highEye.z()))
                .lookDirection(new Vector(1, -0.6, -0.35))
                .build();
        Painter highHole = new Hole(highHoleSetup);

        IVector lowEye = new Vector(0, -radius, 0).transform(ThreeTransform.rotation_z(Math.PI / 10));

        DemoSetup lowSetup = base.toBuilder()
                .viewAngle(Math.PI / 4)
                .upDirection(new Vector(0, -1, 0.1))
                .eyePosition(new Point(lowEye.x()-radius, lowEye.y(), lowEye.z()))
                .lookDirection(new Vector(1, -0.1, 0.15))
                .build();
        Painter lowHole = new Hole(lowSetup);

        Accumulator lowHoleAccumulator = new Accumulator(lowHole, Paths.get("./buffs/lowHole/"), "./outputs/holes/low%02d.ppm");
        Accumulator highHoleAccumulator = new Accumulator(highHole, Paths.get("./buffs/highHole/"), "./outputs/holes/high%02d.ppm");

        while (true) {
            lowHoleAccumulator.accumulate();
            highHoleAccumulator.accumulate();
        }
    }
}
