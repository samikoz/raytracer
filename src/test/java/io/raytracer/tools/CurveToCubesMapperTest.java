package io.raytracer.tools;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.shapes.Axis;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CurveToCubesMapperTest {
    private static ICurve testCurve;
    private static CurveToCubesMapper testMapper;

    @BeforeAll
    static void setupCurveAndMapper() {
        testCurve = new Curve(
            Arrays.asList(new Point(100, 50, 0), new Point(150, 100, 0)),
            Arrays.asList(new Vector(0, 1, 0), new Vector(1, 0, 0))
        );
        testMapper = new CurveToCubesMapper(new Point(100, 100, 0), 0.1, testCurve, Axis.X);
    }

    @Test
    void mapCurvePoints() {
        IPoint origin = new Point(0, 0, 0);

        assertEquals(new Point(0, -5, 0), origin.transform(testMapper.map(0).getTransform()));
        assertEquals(new Point(5, 0, 0), origin.transform(testMapper.map(1).getTransform()));
    }

    @Test
    void mapCurveTangents() {
        IPoint cubeEnd = new Point(1, 0, 0);

        assertEquals(new Point(1, -5, 0), cubeEnd.transform(testMapper.map(0).getTransform()));
        assertEquals(new Point(5, 1, 0), cubeEnd.transform(testMapper.map(1).getTransform()));
    }
}