package io.raytracer.tools;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.ITransform;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.shapes.Shape;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CurveToCubesMapperTest {
    private static List<CurvepointData> curveData;
    private static CurveToCubesMapper testMapper;

    @BeforeAll
    static void setupCurveAndMapper() {
        curveData = Arrays.asList(
            new CurvepointData(new Point(100, 50, 0), new Vector(1, 0, 0)),
            new CurvepointData(new Point(150, 100, 0), new Vector(0, 1, 0))
        );
        testMapper = new CurveToCubesMapper(new Point(100, 100, 0), 0.1);
    }

    @Test
    void mapCurvePoints() {
        IPoint origin = new Point(0, 0, 0);
        List<ITransform> transforms = testMapper.mapCurve(curveData).stream().map(Shape::getTransform).collect(Collectors.toList());

        assertEquals(new Point(0, -5, 0), origin.transform(transforms.get(0)));
        assertEquals(new Point(5, 0, 0), origin.transform(transforms.get(1)));
    }

    @Test
    void mapCurveTangents() {
        IPoint cubeEnd = new Point(1, 0, 0);
        List<ITransform> transforms = testMapper.mapCurve(curveData).stream().map(Shape::getTransform).collect(Collectors.toList());

        assertEquals(new Point(1, -5, 0), cubeEnd.transform(transforms.get(0)));
        assertEquals(new Point(5, 1, 0), cubeEnd.transform(transforms.get(1)));
    }
}