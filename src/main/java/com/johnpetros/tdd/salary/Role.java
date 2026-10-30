package com.johnpetros.tdd.salary;

import java.math.BigDecimal;

public enum Role {
    DEVELOPER("3000.00", "0.10", "0.20"),
    DBA("2000.00", "0.15", "0.25"),
    TESTER("2000.00", "0.15", "0.25"),
    MANAGER("5000.00", "0.20", "0.30");

    private final BigDecimal threshold;
    private final BigDecimal belowThresholdDiscount;
    private final BigDecimal atOrAboveThresholdDiscount;

    Role(String threshold, String belowThresholdDiscount, String atOrAboveThresholdDiscount) {
        this.threshold = new BigDecimal(threshold);
        this.belowThresholdDiscount = new BigDecimal(belowThresholdDiscount);
        this.atOrAboveThresholdDiscount = new BigDecimal(atOrAboveThresholdDiscount);
    }

    BigDecimal discountFor(BigDecimal baseSalary) {
        return baseSalary.compareTo(threshold) >= 0 ? atOrAboveThresholdDiscount : belowThresholdDiscount;
    }
}
