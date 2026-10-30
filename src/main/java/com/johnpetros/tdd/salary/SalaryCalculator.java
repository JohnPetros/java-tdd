package com.johnpetros.tdd.salary;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SalaryCalculator {
    public BigDecimal calculate(Employee employee) {
        BigDecimal retainedFraction = employee.baseSalary().compareTo(new BigDecimal("3000")) >= 0
            ? new BigDecimal("0.80") : new BigDecimal("0.90");
        return employee.baseSalary().multiply(retainedFraction).setScale(2, RoundingMode.HALF_UP);
    }
}
