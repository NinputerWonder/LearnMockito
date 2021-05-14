package org.injection;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ManageEmployeeSalary {
    private EmployeeService employeeService;
    private EmployeeSalaryService employeeSalaryService;

    public ManageEmployeeSalary(EmployeeService employeeService, EmployeeSalaryService employeeSalaryService) {
        this.employeeService = employeeService;
        this.employeeSalaryService = employeeSalaryService;
    }

    EmployeeSalaryRaisingList raiseSalary() {
        List<Employee> activeEmployees = employeeService.findActiveEmployees();
        Date date = new Date(2022, 1, 1);
        List<EmployeeSalary> employeeSalaries = employeeSalaryService.tryToRaiseSalaryUntil(activeEmployees, date);
        employeeSalaryService.saveSalaries(employeeSalaries);
        return createEmployeeSalaryRaisingList(employeeSalaries, activeEmployees);
    }

    private EmployeeSalaryRaisingList createEmployeeSalaryRaisingList(List<EmployeeSalary> employeeSalaries, List<Employee> employees) {
        Map<Integer, Employee> employeeIdToEntity = employees.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
        List<EmployeeSalaryRaising> salaries = employeeSalaries.stream().map(s -> new EmployeeSalaryRaising(s.getEmployeeId(), employeeIdToEntity.get(s.getEmployeeId()).getName(), s.getAmount())).collect(Collectors.toList());
        return new EmployeeSalaryRaisingList(salaries);
    }
}
