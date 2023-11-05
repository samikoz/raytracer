package io.raytracer.shapes.readymades;

import io.raytracer.geometry.ThreeTransform;
import io.raytracer.shapes.Cube;

import java.util.ArrayList;
import java.util.List;

public class SpiralStairs {
    public static List<Cube> form(SpiralParameters params) {
        double actualSeparation = params.stairDepth + params.stairSeparation;
        double rotationAngle = params.getOrientation()*2*Math.asin(actualSeparation/(2*params.radius));

        List<Cube> stairs = new ArrayList<>();
        ThreeTransform transform = ThreeTransform.scaling(params.stairWidth/2, params.stairHeight/2, params.stairDepth/2)
                .translate(cubeXPosition(params), params.initialHeight-params.stairHeight/2, 0)
                .rotate_y(params.angleOffset);

        for (int stairIndex = 0; stairIndex < params.count; stairIndex++) {
            Cube stair = new Cube(params.stairMaterial);
            stair.setTransform(transform);
            stairs.add(stair);

            transform = transform.rotate_y(rotationAngle).translate(0, params.elevationDiff, 0);
        }
        return stairs;
    }

    private static double cubeXPosition(SpiralParameters params) {
        return -params.radius+params.stairWidth/2;
    }
}
