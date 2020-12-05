package io.raytracer.geometry;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;


class TupleComparator {
    static void assertTuplesEqual(Tuple expected, Tuple actual, String message) {
        assertEquals(expected.dim(), actual.dim(), "Should be of the same dimension");

        assertEquals(
                expected, actual,
                () -> message + " " + messageComparingCoordinates(expected, actual)
        );
    }

    static String messageComparingCoordinates(Tuple expected, Tuple actual) {
        String[] coordinateComparisonMessages = new String[expected.dim()];

        for (int coordinateIndex = 0; coordinateIndex < expected.dim(); coordinateIndex++) {
            coordinateComparisonMessages[coordinateIndex] =
                    expected.get(coordinateIndex) + " should be " + actual.get(coordinateIndex);
        }
        return String.join(", ", Arrays.asList(coordinateComparisonMessages));
    }
}


class MatrixComparator {
    static void compareMatrices(SquareMatrix expected, SquareMatrix actual) {
        assertEquals(expected.dim(), actual.dim(), "Matrix dimensions should be the same");

        for (int x = 0; x < expected.dim(); x++) {
            for (int y = 0; y < expected.dim(); y++) {
                assertEquals(
                        expected.get(x, y),
                        actual.get(x, y),
                        1e-3,
                        "(" + x + "," + y + ")-coordinates should be equal"
                );
            }
        }
    }
}
