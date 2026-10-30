# Recorded TDD cycles — Exercise 3

These results come from the actual `mvn -B test` runs during development on
2026-10-29. Counts include the 102 existing cases from Exercises 1 and 2, which
stayed green throughout. Each Red run happened before its corresponding
production change; every Green run reran all exercises.

| Behavior added | Red (expected build failure) | Green (successful build) |
| --- | --- | --- |
| 1. Initial developer example | 103 cases, 0 failures, 1 error | 103 cases, 0 failures, 0 errors |
| 2. Developer threshold and decimal scales | 110 cases, 4 failures, 0 errors | 110 cases, 0 failures, 0 errors |
| 3. DBA discounts | 117 cases, 5 failures, 0 errors | 117 cases, 0 failures, 0 errors |
| 4. Tester discounts | 124 cases, 5 failures, 0 errors | 124 cases, 0 failures, 0 errors |
| 5. Manager discounts | 131 cases, 4 failures, 0 errors | 131 cases, 0 failures, 0 errors |
| 6. Input validation, precision, and large amounts | 153 cases, 6 failures, 0 errors | 153 cases, 0 failures, 0 errors |

The skeleton initially throws `UnsupportedOperationException`. Subsequent Red
runs demonstrate missing salary brackets and input guards. The last cycle adds
six failing invalid-input cases together with precision and large-amount
regressions that already pass.

After the six cycles, repeated role-specific conditions were replaced with
thresholds and discounts stored in `Role`. The calculator now validates input,
selects the role's discount, and applies one shared `BigDecimal` formula. This
refactoring preserves the tested behavior, including comparison before rounding
and final two-decimal `HALF_UP` rounding.

The final `mvn -B clean verify` run passes **153 cases**, including **51 for
Exercise 3**, and the **100% line and branch coverage checks**, without exclusions.

Evidence: [Exercise 3 coverage and execution summaries](coverage/exercise-3/).
The JaCoCo CSV and XML snapshots select the salary package from the full build's
report. Their coverage counters are copied directly from that package.

Git history records the skeleton, each Green implementation, and the final
refactoring and documentation step.
