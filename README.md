# Java TDD — Exercise 1: Triangle

Classify three integer side lengths as **scalene** (escaleno), **isosceles**
(isósceles), or **equilateral** (equilátero), using Java 21, JUnit 5 and Maven.
JaCoCo measures the production code exercised by the tests.

## Requirements

- JDK **21** or later, with `JAVA_HOME` pointing to the JDK.
- Apache Maven **3.6.3** or later.
- Internet access for the first build to download Maven dependencies.

Check the installed versions:

```sh
java -version
mvn -version
```

## Build and test execution

Run these commands from the repository root, where `pom.xml` is located.

```sh
# Compile, execute every test, build the JAR, generate coverage, and check coverage.
mvn clean verify

# Execute only the tests (without generating the HTML coverage report).
mvn test

# Execute a specific group of parameterized tests.
mvn '-Dtest=TriangleClassifierTest#classifiesEveryIsoscelesPermutation' test
```

The JAR is generated at `target/java-tdd-1.0.0-SNAPSHOT.jar`. This is a library:
the exercise's input is supplied through `classify(int a, int b, int c)` and its
behavior is demonstrated by the tests. A console application is not required
to execute the test suite.

## Behavior

All sides must be positive and satisfy **all three** strict inequalities:

```text
a + b > c
a + c > b
b + c > a
```

Invalid inputs throw `IllegalArgumentException`; they are never classified as
a triangle. For valid inputs, three equal sides produce `EQUILATERAL`, exactly
two equal sides produce `ISOSCELES`, and three distinct sides produce `SCALENE`.
The order of the inputs does not change the classification.

```java
import com.johnpetros.tdd.triangle.TriangleClassifier;
import com.johnpetros.tdd.triangle.TriangleType;

TriangleClassifier classifier = new TriangleClassifier();
TriangleType result = classifier.classify(3, 4, 5); // SCALENE
classifier.classify(5, 3, 5);                     // ISOSCELES
classifier.classify(3, 3, 3);                     // EQUILATERAL
classifier.classify(2, 3, 5);                     // throws IllegalArgumentException
```

The inequality checks promote the first operand of each sum to `long` **before**
addition. This prevents overflow when two valid `int` lengths add up to more
than `Integer.MAX_VALUE`.

## Required test cases

Each row in a JUnit `@CsvSource` is a separately executed test case. The suite
contains **38 cases**, including the following coverage of every assignment item:

| Assignment item | Test method | Inputs and expected outcome |
| --- | --- | --- |
| 1. Valid scalene | `classifiesValidScaleneTriangles` | All 6 permutations of `(3, 4, 5)` → `SCALENE` |
| 2 and 4. Valid isosceles and at least 3 permutations | `classifiesEveryIsoscelesPermutation` | `(5, 5, 3)`, `(5, 3, 5)`, `(3, 5, 5)` → `ISOSCELES` |
| 3. Valid equilateral | `classifiesValidEquilateralTriangles` | `(3, 3, 3)`, `(1, 1, 1)` → `EQUILATERAL` |
| 5. One zero side | `rejectsZeroInAnyPosition` | `(0, 3, 3)`, `(3, 0, 3)`, `(3, 3, 0)` → exception |
| 6. One negative side | `rejectsNegativeInAnyPosition` | `(-1, 3, 3)`, `(3, -1, 3)`, `(3, 3, -1)` → exception |
| 7 and 8. Sum equals the third side, in every permutation | `rejectsEveryDegeneratePermutation` | All 6 permutations of `(2, 3, 5)` → exception |
| 9 and 10. Sum is less than the third side, in every permutation | `rejectsEveryTriangleInequalityViolationPermutation` | All 6 permutations of `(2, 3, 6)` → exception |
| 11. Three zero sides | `rejectsThreeZeroSides` | `(0, 0, 0)` → exception |
| Additional: overflow prevention | `classifiesLargeValidSidesWithoutOverflow` | 5 valid cases near `Integer.MAX_VALUE`, covering all 3 triangle types |
| Additional: invalid large sides | `rejectsLargeInvalidSides` | All 3 distinct permutations of `(2147483647, 1, 1)` → exception |

## Exercise 1 — code coverage evidence

Measured on **2026-10-29** with `mvn clean verify`, OpenJDK 21, Maven 3.6.3,
JUnit 5.13.4, and JaCoCo 0.8.14:

```text
Tests run: 38, Failures: 0, Errors: 0, Skipped: 0
All coverage checks have been met.
BUILD SUCCESS
```

| Production class | Instructions | Lines | Branches | Methods |
| --- | --- | --- | --- | --- |
| `TriangleClassifier` | 67/67 (100%) | 10/10 (100%) | 22/22 (100%) | 2/2 (100%) |
| `TriangleType` | 21/21 (100%) | 4/4 (100%) | N/A (no branches) | 1/1 (100%) |
| **Total** | **88/88 (100%)** | **14/14 (100%)** | **22/22 (100%)** | **3/3 (100%)** |

Evidence copied directly from that successful build is committed with the project:

- [JaCoCo CSV report](docs/coverage/exercise-1/jacoco.csv)
- [JaCoCo XML report](docs/coverage/exercise-1/jacoco.xml)
- [JUnit execution summary](docs/coverage/exercise-1/test-results.txt)
- [Maven verification summary](docs/coverage/exercise-1/verification.txt)

To inspect the coverage visually, run `mvn clean verify` and open
`target/site/jacoco/index.html` in a browser. Click the package and then
`TriangleClassifier` to see coverage for each method and highlighted source lines.
The CSV and XML reports are regenerated in the same directory. Test execution
reports are available under `target/surefire-reports/`.

The build enforces **100% line and branch coverage** over all production classes,
with no coverage exclusions. These figures describe coverage by the **whole test
suite**, rather than coverage attributed to each individual test. Coverage shows
which code ran; assertions verify the expected classifications and rejections.
The committed evidence is a snapshot and should be refreshed after code changes.

## TDD process

The implementation was developed in small **Red → Green → Refactor** cycles:

1. Start with the class skeleton and failing scalene tests, then implement the minimum to pass.
2. Add equilateral tests, observe failures, and implement the classification.
3. Add the three isosceles permutations, observe failures, and handle equal pairs.
4. Add zero and negative sides, observe failures, and reject nonpositive sides.
5. Add equal-sum and smaller-sum permutations, observe failures, and enforce the triangle inequality.
6. Add integer-limit tests, observe failures, and promote sums to `long`.
7. Refactor repeated invalid-input assertions into a shared test helper, then rerun all tests and coverage checks.

Every Green step reran the complete existing suite. See the
[recorded cycle results](docs/tdd-cycles.md) and Git history for the progression.

## Source layout

```text
src/main/java/com/johnpetros/tdd/triangle/
    TriangleClassifier.java
    TriangleType.java
src/test/java/com/johnpetros/tdd/triangle/
    TriangleClassifierTest.java
docs/
    tdd-cycles.md
    coverage/exercise-1/
pom.xml
```

## Tool documentation

- [JUnit 5 user guide](https://docs.junit.org/5.13.4/user-guide/)
- [JaCoCo Maven plugin](https://www.jacoco.org/jacoco/trunk/doc/maven.html)
- [JaCoCo coverage checks](https://www.jacoco.org/jacoco/trunk/doc/check-mojo.html)
