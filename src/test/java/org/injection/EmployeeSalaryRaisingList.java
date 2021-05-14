package org.injection;

import java.util.List;

public class EmployeeSalaryRaisingList {

    public List<EmployeeSalaryRaising> salaryList;

    public EmployeeSalaryRaisingList(List<EmployeeSalaryRaising> salaryList) {
        this.salaryList = salaryList;
    }
}

class EmployeeSalaryRaising {
    public int id;
    public String name;
    public int currentSalary;

    public EmployeeSalaryRaising(int id, String name, int currentSalary) {
        this.id = id;
        this.name = name;
        this.currentSalary = currentSalary;
    }
}
