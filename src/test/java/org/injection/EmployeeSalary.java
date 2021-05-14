package org.injection;

import java.util.Date;

public class EmployeeSalary {
    private final Date effectiveDate;
    private final int employeeId;
    private final int amount;

    EmployeeSalary(Date effectiveDate, int employeeId, int amount) {
        this.effectiveDate = effectiveDate;
        this.employeeId = employeeId;
        this.amount = amount;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public int getAmount() {
        return amount;
    }
}
