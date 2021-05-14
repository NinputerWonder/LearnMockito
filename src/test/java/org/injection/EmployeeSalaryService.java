package org.injection;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeSalaryService {
    private EmployeeSalaryRepository employeeSalaryRepository;

    public EmployeeSalaryService(EmployeeSalaryRepository employeeSalaryRepository) {
        this.employeeSalaryRepository = employeeSalaryRepository;
    }

    public List<EmployeeSalary> tryToRaiseSalaryUntil(List<Employee> activeEmployees, Date date) {
        List<EmployeeSalary> salaries = employeeSalaryRepository.findByEmployeeIds(activeEmployees.stream().map(e -> e.getId()).collect(Collectors.toList()));

        List<EmployeeSalary> salariesToRaise = salaries
                .stream()
                .collect(Collectors.groupingBy(s -> s.getEmployeeId()))
                .entrySet()
                .stream()
                .filter(e1 -> e1.getValue().size() == 1 && (date.getYear() - e1.getValue().get(0).getEffectiveDate().getYear() >=2))
                .map(e1 -> e1.getValue())
                .flatMap(e -> e.stream())
                .collect(Collectors.toList());

        return salaries.stream().map(s -> new EmployeeSalary(date, s.getEmployeeId(), (int)(s.getAmount()*1.1))).collect(Collectors.toList());
    }

    public void saveSalaries(List<EmployeeSalary> employeeSalaries) {
        employeeSalaryRepository.save(employeeSalaries);
    }
}
