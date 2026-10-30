package com.johnpetros.tdd.salary;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SalaryCalculatorTest {
    private final SalaryCalculator calculator = new SalaryCalculator();

    @Test
    void calculatesDeveloperSalaryBelowThreshold() {
        Employee employee = new Employee("Ana Silva", "ana@example.com", new BigDecimal("1000.00"), Role.DEVELOPER);

        assertEquals(new BigDecimal("900.00"), calculator.calculate(employee));
    }

    @ParameterizedTest(name = "{0}: base={1}, net={2}")
    @CsvSource({
        "DEVELOPER, 0.00, 0.00",
        "DEVELOPER, 0.01, 0.01",
        "DEVELOPER, 2999.99, 2699.99",
        "DEVELOPER, 3000, 2400.00",
        "DEVELOPER, 3000.00, 2400.00",
        "DEVELOPER, 3000.01, 2400.01",
        "DEVELOPER, 4000.00, 3200.00",
        "DBA, 0.00, 0.00",
        "DBA, 0.01, 0.01",
        "DBA, 1999.99, 1699.99",
        "DBA, 2000, 1500.00",
        "DBA, 2000.00, 1500.00",
        "DBA, 2000.01, 1500.01",
        "DBA, 3000.00, 2250.00",
        "TESTER, 0.00, 0.00",
        "TESTER, 0.01, 0.01",
        "TESTER, 1999.99, 1699.99",
        "TESTER, 2000, 1500.00",
        "TESTER, 2000.00, 1500.00",
        "TESTER, 2000.01, 1500.01",
        "TESTER, 3000.00, 2250.00",
        "MANAGER, 0.00, 0.00",
        "MANAGER, 0.01, 0.01",
        "MANAGER, 4999.99, 3999.99",
        "MANAGER, 5000, 3500.00",
        "MANAGER, 5000.00, 3500.00",
        "MANAGER, 5000.01, 3500.01",
        "MANAGER, 6000.00, 4200.00"
    })
    void calculatesNetSalaryForEachRoleAndThreshold(Role role, BigDecimal baseSalary, BigDecimal expected) {
        Employee employee = new Employee("Ana Silva", "ana@example.com", baseSalary, role);

        assertEquals(expected, calculator.calculate(employee));
    }

    @ParameterizedTest(name = "{0}: round only the final net salary for {1}")
    @CsvSource({
        "DEVELOPER, 0.05, 0.05",
        "DBA, 0.10, 0.09",
        "TESTER, 1.10, 0.94",
        "MANAGER, 0.03, 0.02",
        "DEVELOPER, 0.015, 0.01",
        "DBA, 0.015, 0.01",
        "TESTER, 0.015, 0.01",
        "MANAGER, 0.015, 0.01",
        "DEVELOPER, 2999.999, 2700.00",
        "DBA, 1999.999, 1700.00",
        "TESTER, 1999.999, 1700.00",
        "MANAGER, 4999.999, 4000.00"
    })
    void roundsOnlyAfterSelectingRateAndCalculatingNetSalary(Role role, BigDecimal baseSalary, BigDecimal expected) {
        Employee employee = new Employee("Ana Silva", "ana@example.com", baseSalary, role);

        assertEquals(expected, calculator.calculate(employee));
    }

    @ParameterizedTest(name = "{0}: large salaries retain exact decimal precision")
    @CsvSource({
        "DEVELOPER, 80000000000000000000.00",
        "DBA, 75000000000000000000.00",
        "TESTER, 75000000000000000000.00",
        "MANAGER, 70000000000000000000.00"
    })
    void calculatesLargeSalariesWithoutPrimitiveOverflow(Role role, BigDecimal expected) {
        Employee employee = new Employee("Ana Silva", "ana@example.com",
            new BigDecimal("100000000000000000000.00"), role);

        assertEquals(expected, calculator.calculate(employee));
    }

    @Test
    void rejectsMissingEmployee() {
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(null));
    }

    @Test
    void rejectsMissingRole() {
        Employee employee = new Employee("Ana Silva", "ana@example.com", new BigDecimal("1000.00"), null);

        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(employee));
    }

    @Test
    void rejectsMissingBaseSalary() {
        Employee employee = new Employee("Ana Silva", "ana@example.com", null, Role.DEVELOPER);

        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(employee));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-0.01", "-1.00", "-9999.99"})
    void rejectsNegativeBaseSalary(String baseSalary) {
        Employee employee = new Employee("Ana Silva", "ana@example.com", new BigDecimal(baseSalary), Role.DEVELOPER);

        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(employee));
    }
}
