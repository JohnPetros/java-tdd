package com.johnpetros.tdd.triangle;

public class TriangleClassifier {
    public TriangleType classify(int a, int b, int c) {
        if (a == b && b == c) {
            return TriangleType.EQUILATERAL;
        }
        return TriangleType.SCALENE;
    }
}
