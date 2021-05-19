package org.example;

import java.time.LocalDate;

public class EmployeeSalary {
    private final LocalDate effectiveDate;
    private final int employeeId;
    private final int amount;

    EmployeeSalary(LocalDate effectiveDate, int employeeId, int amount) {
        this.effectiveDate = effectiveDate;
        this.employeeId = employeeId;
        this.amount = amount;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public int getAmount() {
        return amount;
    }
}
