# Java TDD — JUnit exercises

Exercises implemented using Java 21, JUnit 5 and Maven, following TDD.
JaCoCo measures the production code exercised by the tests.

- **Exercise 1:** classify triangle sides as scalene, isosceles, or equilateral.
- **Exercise 2:** validate a `Person` and its associated `Email` objects through
  `PersonDAO.isValidToInclude`, returning a list of errors.
- **Exercise 3:** calculate an employee's net salary using the discount for their
  role and base salary.

The complete suite has **153 passing cases**: 38 for Exercise 1, 64 for Exercise 2,
and 51 for Exercise 3. All three exercise packages have **100% line and branch
coverage**.

**Repository:** [github.com/JohnPetros/java-tdd](https://github.com/JohnPetros/java-tdd)

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

Get the project:

```sh
git clone https://github.com/JohnPetros/java-tdd.git
cd java-tdd
```

Run these commands from the repository root, where `pom.xml` is located.

```sh
# Compile, execute every test, build the JAR, generate coverage, and check coverage.
mvn clean verify

# Execute only the tests (without generating the HTML coverage report).
mvn test

# Execute a specific group of parameterized tests.
mvn '-Dtest=TriangleClassifierTest#classifiesEveryIsoscelesPermutation' test

# Execute only Exercise 2 tests.
mvn -Dtest=PersonDAOTest test

# Execute only Exercise 3 tests.
mvn -Dtest=SalaryCalculatorTest test
```

The JAR is generated at `target/java-tdd-1.0.0-SNAPSHOT.jar`. This is a library:
inputs are supplied through `TriangleClassifier.classify(int a, int b, int c)`,
`PersonDAO.isValidToInclude(Person person)`, and `SalaryCalculator.calculate(Employee employee)`.
Their behavior is demonstrated by the tests. A console application is not
required to execute the test suite.

## Exercise 1 — behavior

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

## Exercise 1 — required test cases

Each row in a JUnit `@CsvSource` is a separately executed test case. The suite
for Exercise 1 contains **38 cases**, covering every assignment item:

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

## Exercise 2 — person validation

The model follows the supplied diagram:

- `Person`: `int id`, `String name`, `int age`, and `List<Email> emails`.
- `Email`: `int id` and `String name` (the address).
- `PersonDAO`: `List<String> isValidToInclude(Person person)`.

`Person` and `Email` are Java records, which are data classes with generated
constructors and accessors such as `person.name()` and `email.name()`. Invalid
values can be constructed so the DAO can report their errors together. The
diagram's persistence operation `save` is outside this exercise's requested
validation method; no database is needed.

The validator returns an **empty list** when all four requirements are satisfied.
Otherwise it collects **all errors**, in name, age, then email order:

| Rule | Accepted input | Rejected input |
| --- | --- | --- |
| Name | At least two whitespace-separated parts, containing only Unicode letters; e.g. `Ana Silva` or `João Gonçalves` | Null, blank, a single name, digits, punctuation, hyphens, or apostrophes |
| Age | Integers from **1 through 200**, inclusive | Zero, negative ages, or ages over 200 |
| Email association | At least one associated email | Null or empty list |
| Each email's name | `local@domain.suffix`, with at least one character in each part | Null email objects or names, blank names, missing parts, whitespace, extra `@`, or empty domain segments |

Leading/trailing whitespace around a person's name is ignored; internal spaces,
tabs, and Unicode whitespace can separate name parts. Email addresses are checked
as supplied, without trimming. Dots and `+` in the local part and multiple domain
suffixes are allowed. This implements the exercise's format check, without
checking email delivery or adding a full RFC address parser.

Each invalid email adds its own error with a **1-based position**. A null `Person`
returns `Person must be provided.` instead of throwing. Validation does not save
or modify the supplied objects, and each call returns a fresh error list.

```java
import java.util.List;
import com.johnpetros.tdd.person.Email;
import com.johnpetros.tdd.person.Person;
import com.johnpetros.tdd.person.PersonDAO;

PersonDAO dao = new PersonDAO();
Person valid = new Person(1, "Ana Silva", 30, List.of(new Email(1, "ana@example.com")));
List<String> validErrors = dao.isValidToInclude(valid); // []

Person invalid = new Person(2, "Ana", 0, List.of(new Email(2, "invalid")));
List<String> errors = dao.isValidToInclude(invalid);
// ["Name must contain at least two parts made only of letters.",
//  "Age must be between 1 and 200 inclusive.",
//  "Email at position 1 must match local@domain.suffix."]
```

### Exercise 2 test cases

`PersonDAOTest` contains **64 cases**:

| Area | Cases | What is verified |
| --- | --- | --- |
| Valid baseline | 1 | A valid person with minimal email parts, `a@b.c` |
| Name | 18 | Multipart names, accents, whitespace, missing names, single names, digits, and punctuation |
| Age | 10 | Both inclusive boundaries, nearby valid values, values outside the range, and integer limits |
| Association | 3 | Missing/empty lists and multiple associated emails |
| Email format and iteration | 28 | Valid formats, all missing parts, null values, whitespace, extra separators, and every email in mixed lists |
| Null input and accumulated errors | 4 | Null person, several simultaneous failures, and independent validation calls |

### Exercise 2 code coverage evidence

The committed Exercise 2 snapshot records a successful `mvn clean verify` run
with **102 cases**: 38 for Exercise 1 and 64 for Exercise 2, before Exercise 3 was
added. The Exercise 2 classes have **100% line and branch coverage**.
See the measured class counters below and the committed build evidence:

| Production class | Instructions | Lines | Branches | Methods |
| --- | --- | --- | --- | --- |
| `PersonDAO` | 99/99 (100%) | 21/21 (100%) | 22/22 (100%) | 4/4 (100%) |
| `Person` | 15/15 (100%) | 1/1 (100%) | N/A (no branches) | 1/1 (100%) |
| `Email` | 9/9 (100%) | 1/1 (100%) | N/A (no branches) | 1/1 (100%) |
| **Total** | **123/123 (100%)** | **23/23 (100%)** | **22/22 (100%)** | **6/6 (100%)** |

- [JaCoCo CSV snapshot](docs/coverage/exercise-2/jacoco.csv)
- [JaCoCo XML snapshot](docs/coverage/exercise-2/jacoco.xml)
- [Exercise 2 JUnit execution summary](docs/coverage/exercise-2/test-results.txt)
- [Full build verification summary](docs/coverage/exercise-2/verification.txt)

The JaCoCo snapshots contain the `com.johnpetros.tdd.person` package extracted
from the full build's report. Run `mvn clean verify` and open
`target/site/jacoco/index.html`, then select that package to inspect Exercise 2.
As with Exercise 1, the measurements cover production code exercised by the
complete suite, and the build enforces 100% line and branch coverage.

## Exercise 3 — salary calculator

`Employee` is a Java record containing `String name`, `String email`,
`BigDecimal baseSalary`, and `Role role`. `SalaryCalculator.calculate(Employee)`
returns the employee's net salary. Names and email addresses are stored as
employee information; salary calculations depend on the base salary and role.

The discount applies to the **entire base salary**, according to the assignment:

| Role | Assignment name | Threshold | Discount below threshold | Discount at or above threshold |
| --- | --- | --- | --- | --- |
| `DEVELOPER` | DESENVOLVEDOR | 3000.00 | 10% | 20% |
| `DBA` | DBA | 2000.00 | 15% | 25% |
| `TESTER` | TESTADOR | 2000.00 | 15% | 25% |
| `MANAGER` | GERENTE | 5000.00 | 20% | 30% |

The calculation is `net = baseSalary × (1 − discount)`. Equality belongs to the
higher-discount bracket: a developer with a base salary of `3000.00` receives
`2400.00`; one with `2999.99` receives `2699.99`.

All amounts and rates use `BigDecimal` constructed from decimal strings. The
threshold comparison and multiplication use the original, unrounded amount.
Because the assignment does not specify rounding, this implementation rounds
only the final net salary to **two decimal places with `RoundingMode.HALF_UP`**.
Base salaries with additional decimal places are accepted. For example, a DBA
with `0.10` receives `0.09`, and a developer with `2999.999` remains below the
threshold and receives `2700.00` after final rounding.

Zero base salary is valid. A missing employee, role, or salary, or a negative
salary, throws `IllegalArgumentException`.

```java
import java.math.BigDecimal;
import com.johnpetros.tdd.salary.Employee;
import com.johnpetros.tdd.salary.Role;
import com.johnpetros.tdd.salary.SalaryCalculator;

Employee employee = new Employee(
    "Ana Silva", "ana@example.com", new BigDecimal("3000.00"), Role.DEVELOPER
);
BigDecimal netSalary = new SalaryCalculator().calculate(employee); // 2400.00
```

### Exercise 3 test cases

`SalaryCalculatorTest` contains **51 cases**. Expected net salaries are supplied
explicitly through JUnit `@CsvSource`, and assertions check both the amount and
the required two-decimal scale.

| Area | Cases | What is verified |
| --- | --- | --- |
| Developer | 8 | Initial example, zero, small salary, and values below, at, and above 3000 |
| DBA | 7 | Zero, small salary, and values below, at, and above 2000 |
| Tester | 7 | The same required brackets as DBA, independently exercised |
| Manager | 7 | Zero, small salary, and values below, at, and above 5000 |
| Rounding and precision | 12 | Half-cent rounding, extra input precision, and comparison before rounding |
| Large amounts | 4 | One large exact decimal calculation for each role |
| Invalid input | 6 | Missing employee, role, or salary, and three negative amounts |

Every role's threshold is tested with different decimal scales, such as
`3000` and `3000.00`, which represent the same amount.

### Exercise 3 code coverage evidence

Measured on **2026-10-29** using `mvn clean verify`, JUnit 5.13.4 and JaCoCo 0.8.14:

```text
SalaryCalculatorTest: 51 cases, 0 failures, 0 errors
Full suite: 153 cases, 0 failures, 0 errors
All coverage checks have been met.
BUILD SUCCESS
```

| Production class | Instructions | Lines | Branches | Methods |
| --- | --- | --- | --- | --- |
| `SalaryCalculator` | 46/46 (100%) | 9/9 (100%) | 8/8 (100%) | 2/2 (100%) |
| `Role` | 73/73 (100%) | 11/11 (100%) | 2/2 (100%) | 3/3 (100%) |
| `Employee` | 15/15 (100%) | 1/1 (100%) | N/A (no branches) | 1/1 (100%) |
| **Total** | **134/134 (100%)** | **21/21 (100%)** | **10/10 (100%)** | **6/6 (100%)** |

- [JaCoCo CSV snapshot](docs/coverage/exercise-3/jacoco.csv)
- [JaCoCo XML snapshot](docs/coverage/exercise-3/jacoco.xml)
- [Exercise 3 JUnit execution summary](docs/coverage/exercise-3/test-results.txt)
- [Full build verification summary](docs/coverage/exercise-3/verification.txt)

The snapshots select the `com.johnpetros.tdd.salary` package from the full
build's report. To inspect it visually, run `mvn clean verify`, open
`target/site/jacoco/index.html`, and select that package. Coverage is measured
over the complete test suite, with no production-code exclusions.

## TDD process

### Exercise 1

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

### Exercise 2

Six measured Red → Green cycles introduced the class skeleton, name validation,
inclusive age limits, the required email association, every email's format, and
null-person handling with accumulated errors. Email validation was then extracted
into a private helper, and the complete test suite and coverage checks reran
successfully. See the [Exercise 2 cycle results](docs/tdd-person-cycles.md).

### Exercise 3

Six measured Red → Green cycles introduced the salary calculator skeleton,
developer threshold, DBA discounts, tester discounts, manager discounts, and
input validation. Precision and large-amount regression cases accompanied the
last cycle. The final refactoring moved the role-specific thresholds and
discounts into the `Role` enum, replacing repeated calculation branches with
one shared decimal formula. All 153 cases and coverage checks then passed.
See the [Exercise 3 cycle results](docs/tdd-salary-cycles.md).

## Source layout

```text
src/main/java/com/johnpetros/tdd/triangle/
    TriangleClassifier.java
    TriangleType.java
src/test/java/com/johnpetros/tdd/triangle/
    TriangleClassifierTest.java
src/main/java/com/johnpetros/tdd/person/
    Person.java
    Email.java
    PersonDAO.java
src/test/java/com/johnpetros/tdd/person/
    PersonDAOTest.java
src/main/java/com/johnpetros/tdd/salary/
    Employee.java
    Role.java
    SalaryCalculator.java
src/test/java/com/johnpetros/tdd/salary/
    SalaryCalculatorTest.java
docs/
    tdd-cycles.md
    tdd-person-cycles.md
    tdd-salary-cycles.md
    coverage/exercise-1/
    coverage/exercise-2/
    coverage/exercise-3/
pom.xml
```

## Tool documentation

- [JUnit 5 user guide](https://docs.junit.org/5.13.4/user-guide/)
- [JaCoCo Maven plugin](https://www.jacoco.org/jacoco/trunk/doc/maven.html)
- [JaCoCo coverage checks](https://www.jacoco.org/jacoco/trunk/doc/check-mojo.html)
