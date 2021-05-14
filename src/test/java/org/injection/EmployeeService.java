package org.injection;

import java.util.List;
import java.util.stream.Collectors;

public class EmployeeService {
    private EmployeeRepository employeeRepository;


    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findActiveEmployees() {
        return employeeRepository.findAll().stream().filter(e -> e.getActive()).collect(Collectors.toList());
    }
}
