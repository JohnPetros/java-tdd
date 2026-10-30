package com.johnpetros.tdd.salary;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SalaryCalculator {
    /**
     * Applies the role's discount to the entire base salary and rounds the final
     * result to two decimal places using HALF_UP.
     *
     * @throws IllegalArgumentException if the employee, role, or base salary is
     *         missing, or the base salary is negative
     */
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
        BigDecimal retainedFraction = BigDecimal.ONE.subtract(employee.role().discountFor(employee.baseSalary()));
        return employee.baseSalary().multiply(retainedFraction).setScale(2, RoundingMode.HALF_UP);
    }
}
