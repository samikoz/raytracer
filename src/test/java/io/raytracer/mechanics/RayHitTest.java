package io.raytracer.mechanics;

import io.raytracer.geometry.Point;
import io.raytracer.geometry.ThreeTransform;
import io.raytracer.geometry.Vector;
import io.raytracer.shapes.Shape;
import io.raytracer.shapes.Sphere;
import io.raytracer.materials.Glass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RayHitTest {
    static IRay testRay;

    @BeforeAll
    static void setupMaterialAndPosition() {
        testRay = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
    }

    @Test
    void hitWithPositiveIntersections() {
        Intersection i1 = new Intersection(testRay,2.0, new Sphere());
        Intersection i2 = new Intersection(testRay,0.1, new Sphere());
        Optional<RayHit> expectedHit = RayHit.fromIntersections(new Intersection[] { i1, i2 });

        assertTrue(expectedHit.isPresent());
        assertEquals(0.1, expectedHit.get().rayParameter, 1e-3,
                "Hit should be with lowest positive parameter value");
    }

    @Test
    void hitWithSomeNegativeIntersections() {
        Intersection i1 = new Intersection(testRay,2.0, new Sphere());
        Intersection i2 = new Intersection(testRay,-0.1, new Sphere());
        Optional<RayHit> expectedHit = RayHit.fromIntersections(new Intersection[] { i1, i2 });

        assertTrue(expectedHit.isPresent());
        assertEquals(2.0, expectedHit.get().rayParameter, 1e-3,
                "Hit should be with the lowest positive parameter value.");
    }

    @Test
    void hitWithAllNegativeIntersections() {
        Intersection i1 = new Intersection(testRay,-2.0, new Sphere());
        Intersection i2 = new Intersection(testRay,-0.1, new Sphere());
        Optional<RayHit> expectedHit = RayHit.fromIntersections(new Intersection[] { i1, i2 });

        assertFalse(expectedHit.isPresent(), "All negative intersections should produce no hit.");
    }

    @Test
    void hitpointCreation() {
        Ray ray = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));
        Sphere sphere = new Sphere();
        Intersection intersection = new Intersection(ray,4, sphere);
        Optional<RayHit> hit = RayHit.fromIntersections(new Intersection[] { intersection });

        assertTrue(hit.isPresent());
        RayHit hitpoint = hit.get();

        assertEquals(sphere, hitpoint.object);
        assertEquals(new Point(0, 0, -1), hitpoint.point);
        assertEquals(new Vector(0, 0, -1), hitpoint.eyeVector);
        assertEquals(new Vector(0, 0, -1), hitpoint.normalVector);
    }

    @Test
    void hitpointCreationFromInside() {
        Ray ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        Sphere sphere = new Sphere();
        Intersection intersection = new Intersection(ray,1, sphere);
        Optional<RayHit> hit = RayHit.fromIntersections(new Intersection[] { intersection });

        assertTrue(hit.isPresent());
        RayHit hitpoint = hit.get();

        assertEquals(new Point(0, 0, 1), hitpoint.point);
        assertEquals(new Vector(0, 0, -1), hitpoint.eyeVector);
        assertEquals(new Vector(0, 0, -1), hitpoint.normalVector,
                "Normal vector should be inverted");
    }

    private static Stream<Arguments> provideIntersectionIndicesAndRefractionIndices() {
        return Stream.of(
                Arguments.of(new Point(0, 0, -2.5), 1.0, 1.5),
                Arguments.of(new Point(0, 0, -1.5), 1.5, 2.0),
                Arguments.of(new Point(0, 0, -1), 2.0, 2.5),
                Arguments.of(new Point(0, 0, 0), 2.5, 2.5),
                Arguments.of(new Point(0, 0, 1), 2.5, 1.5),
                Arguments.of(new Point(0, 0, 1.5), 1.5, 1.0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideIntersectionIndicesAndRefractionIndices")
    void refractiveIndices(Point rayStartPoint, double refractiveFrom, double refractiveTo) {
        Shape outerSphere = new Sphere(Glass.glassBuilder().refractiveIndex(1.5).build());
        outerSphere.setTransform(ThreeTransform.scaling(2, 2, 2));
        Shape leftSphere = new Sphere(Glass.glassBuilder().refractiveIndex(2.0).build());
        leftSphere.setTransform(ThreeTransform.translation(0, 0, -0.25));
        Shape rightSphere = new Sphere(Glass.glassBuilder().refractiveIndex(2.5).build());
        rightSphere.setTransform(ThreeTransform.translation(0, 0, 0.25));
        World world = (World)new World().put(outerSphere).put(leftSphere).put(rightSphere);

        IRay ray = new Ray(rayStartPoint, new Vector(0, 0, 1));
        Intersection[] intersections = world.intersect(ray);
        Optional<RayHit> hit = RayHit.fromIntersections(intersections);
        assertTrue(hit.isPresent());
        RayHit hitpoint = hit.get();

        assertEquals(refractiveFrom, hitpoint.refractiveIndexFrom);
        assertEquals(refractiveTo, hitpoint.refractiveIndexTo);
    }

    @Test
    void reflectanceOfAPerpendicularRay() {
        Shape sphere = new Sphere(Glass.glassBuilder().build());
        IRay ray = new Ray(new Point(0, 0, 0), new Vector(0, 1, 0));
        RayHit hitpoint = RayHit.fromIntersections(new Intersection[] { new Intersection(ray, 1, sphere) }).get();

        assertEquals(0.04, hitpoint.reflectance, 1e-3, "Reflectance is small for a perpendicular ray");
    }

    @Test
    void reflectanceForASmallAngleAndLargerSecondRefractiveIndex() {
        Shape sphere = new Sphere(Glass.glassBuilder().build());
        IRay ray = new Ray(new Point(0, 0.99, -2), new Vector(0, 0, 1));
        RayHit hitpoint = RayHit.fromIntersections(new Intersection[] { new Intersection(ray, 1.8589, sphere) }).get();

        assertEquals(0.48873, hitpoint.reflectance, 1e-3, "Reflectance is small for a perpendicular ray");
    }
}