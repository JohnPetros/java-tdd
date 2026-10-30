package com.johnpetros.tdd.salary;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        "DBA, 3000.00, 2250.00"
    })
    void calculatesNetSalaryForEachRoleAndThreshold(Role role, BigDecimal baseSalary, BigDecimal expected) {
        Employee employee = new Employee("Ana Silva", "ana@example.com", baseSalary, role);

        assertEquals(expected, calculator.calculate(employee));
    }
}
