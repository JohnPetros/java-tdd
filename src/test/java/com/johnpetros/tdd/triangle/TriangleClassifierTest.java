package com.johnpetros.tdd.triangle;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TriangleClassifierTest {
    private final TriangleClassifier classifier = new TriangleClassifier();

    @ParameterizedTest(name = "({0}, {1}, {2}) is scalene")
    @CsvSource({"3, 4, 5", "3, 5, 4", "4, 3, 5", "4, 5, 3", "5, 3, 4", "5, 4, 3"})
    void classifiesValidScaleneTriangles(int a, int b, int c) {
        assertEquals(TriangleType.SCALENE, classifier.classify(a, b, c));
    }

    @ParameterizedTest(name = "({0}, {1}, {2}) is equilateral")
    @CsvSource({"3, 3, 3", "1, 1, 1"})
    void classifiesValidEquilateralTriangles(int a, int b, int c) {
        assertEquals(TriangleType.EQUILATERAL, classifier.classify(a, b, c));
    }

    @ParameterizedTest(name = "({0}, {1}, {2}) is isosceles")
    @CsvSource({"5, 5, 3", "5, 3, 5", "3, 5, 5"})
    void classifiesEveryIsoscelesPermutation(int a, int b, int c) {
        assertEquals(TriangleType.ISOSCELES, classifier.classify(a, b, c));
    }

    @ParameterizedTest(name = "({0}, {1}, {2}) has a zero side")
    @CsvSource({"0, 3, 3", "3, 0, 3", "3, 3, 0"})
    void rejectsZeroInAnyPosition(int a, int b, int c) {
        assertThrows(IllegalArgumentException.class, () -> classifier.classify(a, b, c));
    }

    @ParameterizedTest(name = "({0}, {1}, {2}) has a negative side")
    @CsvSource({"-1, 3, 3", "3, -1, 3", "3, 3, -1"})
    void rejectsNegativeInAnyPosition(int a, int b, int c) {
        assertThrows(IllegalArgumentException.class, () -> classifier.classify(a, b, c));
    }

    @Test
    void rejectsThreeZeroSides() {
        assertThrows(IllegalArgumentException.class, () -> classifier.classify(0, 0, 0));
    }

    @ParameterizedTest(name = "({0}, {1}, {2}) has two sides summing to the third")
    @CsvSource({"2, 3, 5", "2, 5, 3", "3, 2, 5", "3, 5, 2", "5, 2, 3", "5, 3, 2"})
    void rejectsEveryDegeneratePermutation(int a, int b, int c) {
        assertThrows(IllegalArgumentException.class, () -> classifier.classify(a, b, c));
    }

    @ParameterizedTest(name = "({0}, {1}, {2}) has two sides summing below the third")
    @CsvSource({"2, 3, 6", "2, 6, 3", "3, 2, 6", "3, 6, 2", "6, 2, 3", "6, 3, 2"})
    void rejectsEveryTriangleInequalityViolationPermutation(int a, int b, int c) {
        assertThrows(IllegalArgumentException.class, () -> classifier.classify(a, b, c));
    }
}
