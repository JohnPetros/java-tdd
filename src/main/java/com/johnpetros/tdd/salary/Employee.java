package com.johnpetros.tdd.salary;

import java.math.BigDecimal;

public record Employee(String name, String email, BigDecimal baseSalary, Role role) {
}
