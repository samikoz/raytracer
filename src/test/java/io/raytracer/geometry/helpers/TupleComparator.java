package io.raytracer.geometry.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import io.raytracer.geometry.Tuple;

public class TupleComparator {
    public static void assertTuplesEqual(Tuple expected, Tuple actual, String message) {
        assertEquals(expected.dim(), actual.dim(), "Should be of the same dimension");

        assertEquals(expected, actual, () -> message + " " + messageComparingCoordinates(expected, actual)
        );
    }

    public static String messageComparingCoordinates(Tuple expected, Tuple actual) {
        String[] coordinateComparisonMessages = new String[expected.dim()];

        for (int coordinateIndex = 0; coordinateIndex < expected.dim(); coordinateIndex++) {
            coordinateComparisonMessages[coordinateIndex] =
                    expected.get(coordinateIndex) + " should be " + actual.get(coordinateIndex);
        }
        return String.join(", ", Arrays.asList(coordinateComparisonMessages));
    }
}