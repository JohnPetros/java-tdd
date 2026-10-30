package com.johnpetros.tdd.triangle;

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
}
