package com.johnpetros.tdd.salary;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SalaryCalculator {
    /**
     * Aplica o desconto do cargo sobre todo o salário-base e arredonda o resultado
     * final para duas casas decimais usando HALF_UP.
     *
     * @throws IllegalArgumentException se o funcionário, o cargo ou o salário-base
     *         não for informado, ou se o salário-base for negativo
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
