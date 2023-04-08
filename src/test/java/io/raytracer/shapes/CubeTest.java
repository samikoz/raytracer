package io.raytracer.shapes;

import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.mechanics.Ray;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CubeTest {
    private static Stream<Arguments> providePositiveRaysAndIntersections() {
        return Stream.of(
                Arguments.of(new Ray(new Point(5, 0.5, 0), new Vector(-1,0,0)), 4.0, 6.0),
                Arguments.of(new Ray(new Point(-5, 0.5, 0), new Vector(1,0,0)), 4.0, 6.0),
                Arguments.of(new Ray(new Point(0.5, 5, 0), new Vector(0,-1,0)), 4.0, 6.0),
                Arguments.of(new Ray(new Point(0.5, -5, 0), new Vector(0,1,0)), 4.0, 6.0),
                Arguments.of(new Ray(new Point(0.5, 0, 5), new Vector(0,0,-1)), 4.0, 6.0),
                Arguments.of(new Ray(new Point(0.5, 0, -5), new Vector(0,0,1)), 4.0, 6.0),
                Arguments.of(new Ray(new Point(0, 0.5, 0), new Vector(0,0,1)), -1, 1)
        );
    }

    @ParameterizedTest
    @MethodSource("providePositiveRaysAndIntersections")
    void positiveLocalIntersectionPositions(Ray ray, double firstIntersection, double secondIntersection) {
        Shape cube = new Cube();
        double[] intersections = cube.getLocalIntersectionPositions(ray);
        assertEquals(2, intersections.length);
        assertEquals(firstIntersection, intersections[0], 1e-3);
        assertEquals(secondIntersection, intersections[1], 1e-3);
    }

    private static Stream<Arguments> provideMissingRaysAndIntersections() {
        return Stream.of(
                Arguments.of(new Ray(new Point(2, 0, 0), new Vector(0.2673, 0.5345, 0.8018))),
                Arguments.of(new Ray(new Point(0, -2, 0), new Vector(0.8018, 0.2673, 0.5345))),
                Arguments.of(new Ray(new Point(0, 0, -2), new Vector(0.5345, 0.8018, 0.2673))),
                Arguments.of(new Ray(new Point(2, 0, 2), new Vector(0, 0, -1))),
                Arguments.of(new Ray(new Point(0, 2, 2), new Vector(0, -1, 0))),
                Arguments.of(new Ray(new Point(2, 2, 0), new Vector(1, 0, 0)))
        );
    }

    @ParameterizedTest
    @MethodSource("provideMissingRaysAndIntersections")
    void missingLocalIntersectionPositions(Ray ray) {
        Shape cube = new Cube();
        double[] intersections = cube.getLocalIntersectionPositions(ray);
        assertEquals(0, intersections.length);
    }

    private static Stream<Arguments> providePointsAndNormals() {
        return Stream.of(
                Arguments.of(new Point(1, 0.5, -0.8), new Vector(1, 0, 0)),
                Arguments.of(new Point(-1, -0.2, 0.9), new Vector(-1, 0, 0)),
                Arguments.of(new Point(-0.4, 1, -0.1), new Vector(0, 1, 0)),
                Arguments.of(new Point(0.3, -1, -0.7), new Vector(0, -1, 0)),
                Arguments.of(new Point(0.6, 0.3, 1), new Vector(0, 0, 1)),
                Arguments.of(new Point(0.4, 0.4, -1), new Vector(0, 0, -1)),
                Arguments.of(new Point(1, 1, 1), new Vector(1, 0, 0)),
                Arguments.of(new Point(-1, -1, -1), new Vector(-1, 0, 0))
        );
    }

    @ParameterizedTest
    @MethodSource("providePointsAndNormals")
    void normalLocally(Point point, Vector normal) {
        Shape cube = new Cube();
        IVector actualNormal = cube.normalLocally(point);
        assertEquals(normal, actualNormal);
    }
}