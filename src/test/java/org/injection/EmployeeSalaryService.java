package org.injection;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeSalaryService {
    private EmployeeSalaryRepository employeeSalaryRepository;

    public EmployeeSalaryService(EmployeeSalaryRepository employeeSalaryRepository) {
        this.employeeSalaryRepository = employeeSalaryRepository;
    }

    public List<EmployeeSalary> tryToRaiseSalaryUntil(List<Employee> activeEmployees, LocalDate date) {
        List<EmployeeSalary> salaries = employeeSalaryRepository.findByEmployeeIds(activeEmployees.stream().map(e -> e.getId()).collect(Collectors.toList()));

        List<EmployeeSalary> salariesToRaise = salaries
                .stream()
                .collect(Collectors.groupingBy(s -> s.getEmployeeId()))
                .entrySet()
                .stream()
                .filter(s -> s.getValue().size() == 1 && (date.getYear() - s.getValue().get(0).getEffectiveDate().getYear() >=2))
                .map(s -> s.getValue())
                .flatMap(e -> e.stream())
                .collect(Collectors.toList());

        return salariesToRaise.stream().map(s -> new EmployeeSalary(date, s.getEmployeeId(), (int)(s.getAmount()*1.1))).collect(Collectors.toList());
    }

    public void saveSalaries(List<EmployeeSalary> employeeSalaries) {
        employeeSalaryRepository.save(employeeSalaries);
    }
}
