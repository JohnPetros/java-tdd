package com.johnpetros.tdd.salary;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SalaryCalculatorTest {
    private final SalaryCalculator calculator = new SalaryCalculator();

    @Test
    void calculatesDeveloperSalaryBelowThreshold() {
        Employee employee = new Employee("Ana Silva", "ana@example.com", new BigDecimal("1000.00"), Role.DEVELOPER);

        assertEquals(new BigDecimal("900.00"), calculator.calculate(employee));
    }
}
