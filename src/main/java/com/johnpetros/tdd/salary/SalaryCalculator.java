package com.johnpetros.tdd.salary;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SalaryCalculator {
    public BigDecimal calculate(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee must be provided.");
        }
        if (employee.role() == null) {
            throw new IllegalArgumentException("Employee role must be provided.");
        }
        if (employee.baseSalary() == null || employee.baseSalary().signum() < 0) {
            throw new IllegalArgumentException("Base salary must be provided and nonnegative.");
        }
        BigDecimal retainedFraction = employee.baseSalary().compareTo(new BigDecimal("3000")) >= 0
            ? new BigDecimal("0.80") : new BigDecimal("0.90");
        if (employee.role() == Role.DBA || employee.role() == Role.TESTER) {
            retainedFraction = employee.baseSalary().compareTo(new BigDecimal("2000")) >= 0
                ? new BigDecimal("0.75") : new BigDecimal("0.85");
        }
        if (employee.role() == Role.MANAGER) {
            retainedFraction = employee.baseSalary().compareTo(new BigDecimal("5000")) >= 0
                ? new BigDecimal("0.70") : new BigDecimal("0.80");
        }
        return employee.baseSalary().multiply(retainedFraction).setScale(2, RoundingMode.HALF_UP);
    }
}
