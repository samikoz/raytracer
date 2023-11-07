package io.raytracer.tools;

import org.javatuples.Pair;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClosedPixelCurveTest {

    @Test
    void getBoundsForY() {
        List<Pixel> curvePoints = Arrays.asList(
                new Pixel(10, 100), new Pixel(10, 10), new Pixel(70, 70), new Pixel(100,30), new Pixel(100, 100), new Pixel(10, 100)
        );
        ClosedPixelCurve curve = new ClosedPixelCurve(curvePoints);
        Set<Pair<Integer, Integer>> expectedPairs = new HashSet<>(Arrays.asList(new Pair<>(10, 50), new Pair<>(85, 100)));

        Set<Pair<Integer, Integer>> pixelPairs = new HashSet<>(curve.getBoundsForY(50));

        assertEquals(expectedPairs, pixelPairs);
    }
}