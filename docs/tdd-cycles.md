# Recorded TDD cycles — Exercise 1

These summaries come from the actual Maven test runs during development on
2026-10-29. Each Red run happened before the corresponding production change;
each Green run reran the whole suite with `mvn -B test`.

| Behavior added | Red (expected build failure) | Green (successful build) |
| --- | --- | --- |
| 1. Scalene classification | 6 cases, 0 failures, 6 errors | 6 cases, 0 failures, 0 errors |
| 2. Equilateral classification | 8 cases, 2 failures, 0 errors | 8 cases, 0 failures, 0 errors |
| 3. Isosceles permutations | 11 cases, 3 failures, 0 errors | 11 cases, 0 failures, 0 errors |
| 4. Zero and negative sides | 18 cases, 7 failures, 0 errors | 18 cases, 0 failures, 0 errors |
| 5. Triangle inequality permutations | 30 cases, 12 failures, 0 errors | 30 cases, 0 failures, 0 errors |
| 6. Integer overflow boundaries | 38 cases, 0 failures, 5 errors | 38 cases, 0 failures, 0 errors |

Cycle 1 starts with `UnsupportedOperationException` in the class skeleton.
Cycle 6 fails with exceptions because overflowing `int` sums reject valid
triangles; promoting the sums to `long` resolves those failures.

After the six cycles, duplicated rejection assertions were extracted into
`assertInvalidTriangle`. The final `mvn -B clean verify` run passed all 38 cases
and both the line and branch coverage checks. Its evidence is in
[coverage/exercise-1](coverage/exercise-1/).

The Git history records the initial skeleton, each Green implementation, and
the final refactoring/documentation step.
