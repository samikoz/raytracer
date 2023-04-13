package io.raytracer.shapes;

import io.raytracer.geometry.IPoint;
import io.raytracer.geometry.IVector;
import io.raytracer.geometry.Point;
import io.raytracer.geometry.Vector;
import io.raytracer.mechanics.IRay;
import io.raytracer.mechanics.Ray;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CylinderTest {

    private static Stream<Arguments> provideMissingRays() {
        return Stream.of(
            Arguments.of(new Ray(new Point(1, 0, 0), new Vector(0, 1, 0))),
            Arguments.of(new Ray(new Point(0, 0, 0), new Vector(0, 1, 0))),
            Arguments.of(new Ray(new Point(0, 0, -5), new Vector(1, 1, 1)))
        );
    }

    @ParameterizedTest
    @MethodSource("provideMissingRays")
    void getLocalIntersectionsForMissingRay(IRay ray) {
        Shape cylinder = new Cylinder();
        double[] intersections = cylinder.getLocalIntersectionPositions(ray);

        assertEquals(0, intersections.length);
    }

    private static Stream<Arguments> provideHittingRaysAndIntersections() {
        return Stream.of(
            Arguments.of(new Ray(new Point(1, 0, -5), new Vector(0,0,1)), 5.0, 5.0),
            Arguments.of(new Ray(new Point(0, 0, -5), new Vector(0,0,1)), 4.0, 6.0),
            Arguments.of(new Ray(new Point(0.5, 0, -5), new Vector(0.070534,0.705345,0.705345)), 6.80798, 7.08872)
        );
    }

    @ParameterizedTest
    @MethodSource("provideHittingRaysAndIntersections")
    void positiveLocalIntersectionPositions(Ray ray, double firstIntersection, double secondIntersection) {
        Shape cylinder = new Cylinder();
        double[] intersections = cylinder.getLocalIntersectionPositions(ray);
        assertEquals(2, intersections.length);
        assertEquals(firstIntersection, intersections[0], 1e-3);
        assertEquals(secondIntersection, intersections[1], 1e-3);
    }

    private static Stream<Arguments> provideNormalPositionsAndNormals() {
        return Stream.of(
                Arguments.of(new Point(1, 0, 0), new Vector(1,0,0)),
                Arguments.of(new Point(0, 5, -1), new Vector(0,0,-1)),
                Arguments.of(new Point(0, -2, 1), new Vector(0,0,1)),
                Arguments.of(new Point(-1, 1, 0), new Vector(-1, 0, 0))
        );
    }

    @ParameterizedTest
    @MethodSource("provideNormalPositionsAndNormals")
    void normalToCylinders(IPoint normalPosition, IVector expectedNormal) {
        Shape cylinder = new Cylinder();
        IVector normal = cylinder.normalLocally(normalPosition);

        assertEquals(expectedNormal, normal);
    }
}