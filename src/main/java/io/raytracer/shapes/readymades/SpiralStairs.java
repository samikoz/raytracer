package io.raytracer.shapes.readymades;

import io.raytracer.geometry.ThreeTransform;
import io.raytracer.shapes.Cube;
import io.raytracer.shapes.Group;
import io.raytracer.shapes.Hittable;

import java.util.ArrayList;
import java.util.List;

public class SpiralStairs {
    public static Group form(SpiralParameters params) {
        double actualSeparation = params.stairDepth + params.stairSeparation;
        double rotationAngle = 2*Math.asin(actualSeparation/(2*params.radius));

        List<Hittable> stairs = new ArrayList<>();
        ThreeTransform initialTransform = ThreeTransform.scaling(params.stairWidth/2, params.stairHeight/2, params.stairDepth/2)
                .translate(cubeXPosition(params), -params.stairHeight/2, 0)
                .rotate_y(params.angleOffset);
        ThreeTransform transform = initialTransform;

        for (int preStairIndex = 0; preStairIndex < params.countBefore; preStairIndex++) {
            transform = transform.rotate_y(rotationAngle).translate(0, params.stairDrop, 0);

            Cube stair = new Cube(params.stairMaterial);
            stair.setTransform(transform);
            stairs.add(stair);
        }

        transform = initialTransform;
        for (int skipIndex = 0; skipIndex < params.skip; skipIndex++) {
            transform = transform.rotate_y(-rotationAngle).translate(0, -params.stairDrop, 0);
        }

        for (int stairIndex = 0; stairIndex < params.count; stairIndex++) {
            Cube stair = new Cube(params.stairMaterial);
            stair.setTransform(transform);
            stairs.add(stair);

            transform = transform.rotate_y(-rotationAngle).translate(0, -params.stairDrop, 0);
        }
        return new Group(stairs.toArray(new Hittable[] {}));
    }

    private static double cubeXPosition(SpiralParameters params) {
        return params.getOrientation()*(params.centre.x() + params.radius+params.stairWidth/2);
    }
}
