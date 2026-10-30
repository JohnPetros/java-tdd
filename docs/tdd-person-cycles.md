# Recorded TDD cycles — Exercise 2

These summaries come from the actual `mvn -B test` runs during development on
2026-10-29. Counts include the 38 existing Exercise 1 cases, which stayed green
throughout. Each Red run happened before its corresponding production change;
every Green run reran both exercises.

| Behavior added | Red (expected build failure) | Green (successful build) |
| --- | --- | --- |
| 1. Valid person and minimal email | 39 cases, 0 failures, 1 error | 39 cases, 0 failures, 0 errors |
| 2. Multipart alphabetic name | 57 cases, 11 failures, 0 errors | 57 cases, 0 failures, 0 errors |
| 3. Inclusive age limits | 67 cases, 5 failures, 0 errors | 67 cases, 0 failures, 0 errors |
| 4. Required email association | 70 cases, 2 failures, 0 errors | 70 cases, 0 failures, 0 errors |
| 5. Every email format and indexed errors | 98 cases, 23 failures, 0 errors | 98 cases, 0 failures, 0 errors |
| 6. Null person and accumulated errors | 102 cases, 0 failures, 1 error | 102 cases, 0 failures, 0 errors |

The skeleton initially throws `UnsupportedOperationException`. The final Red
run exposes a `NullPointerException` for a missing person; a guard returns a
validation error instead. The same cycle confirms that name, age, and every
email error are accumulated and that separate calls do not share errors.

After these cycles, email validation was extracted into a private helper to
reduce nesting and repeated association access. The full `mvn -B clean verify`
run then passed all **102 cases** (64 for Exercise 2), including the **100% line
and branch coverage checks**, without exclusions.

Evidence: [Exercise 2 coverage and test summaries](coverage/exercise-2/).
The JaCoCo CSV and XML snapshots select the person package from the full build's
report. Their coverage counters are copied directly from that package.

Git history records the skeleton, each Green implementation, and the final
refactoring and documentation step.
