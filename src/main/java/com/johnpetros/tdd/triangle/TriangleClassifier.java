package com.johnpetros.tdd.triangle;

public class TriangleClassifier {
    public TriangleType classify(int a, int b, int c) {
        if (a <= 0 || b <= 0 || c <= 0) {
            throw new IllegalArgumentException("Triangle sides must be positive.");
        }
        if (a == b && b == c) {
            return TriangleType.EQUILATERAL;
        }
        if (a == b || a == c || b == c) {
            return TriangleType.ISOSCELES;
        }
        return TriangleType.SCALENE;
    }
}
