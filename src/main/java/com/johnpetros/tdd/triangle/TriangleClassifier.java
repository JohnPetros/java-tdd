package com.johnpetros.tdd.triangle;

public class TriangleClassifier {
    /**
     * Classifies three integer side lengths, independent of their order.
     *
     * @throws IllegalArgumentException if a side is nonpositive or the sides
     *         do not satisfy the strict triangle inequality
     */
    public TriangleType classify(int a, int b, int c) {
        if (a <= 0 || b <= 0 || c <= 0) {
            throw new IllegalArgumentException("Triangle sides must be positive.");
        }
        if ((long) a + b <= c || (long) a + c <= b || (long) b + c <= a) {
            throw new IllegalArgumentException("The sum of any two sides must exceed the third side.");
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
