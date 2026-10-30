package com.johnpetros.tdd.salary;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SalaryCalculator {
    public BigDecimal calculate(Employee employee) {
        return employee.baseSalary().multiply(new BigDecimal("0.90")).setScale(2, RoundingMode.HALF_UP);
    }
}
