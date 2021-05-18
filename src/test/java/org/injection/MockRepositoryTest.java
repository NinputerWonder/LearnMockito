package org.injection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MockRepositoryTest {

    @Test
    public void should_raise_salary_for_employee_who_engaged_more_than_2_years_and_never_raised_salary(){
        EmployeeRepository mockedEmployeeRepository = mock(EmployeeRepository.class);
        EmployeeSalaryRepository mockedEmployeeSalaryRepository = mock(EmployeeSalaryRepository.class);

        when(mockedEmployeeRepository.findAll()).thenReturn(Arrays.asList(
                new Employee(1, "Bob", true),
                new Employee(2, "Andy", true)
        ));

        when(mockedEmployeeSalaryRepository.findByEmployeeIds(any())).thenReturn(
                Arrays.asList(
                        new EmployeeSalary(LocalDate.parse("2019-02-02"), 1, 10000),
                        new EmployeeSalary(LocalDate.parse("2018-10-09"), 2, 20000)
                ));

        EmployeeService employeeService = new EmployeeService(mockedEmployeeRepository);
        EmployeeSalaryService employeeSalaryService = new EmployeeSalaryService(mockedEmployeeSalaryRepository);
        ManageEmployeeSalary manageEmployeeSalary = new ManageEmployeeSalary(employeeService, employeeSalaryService);


        EmployeeSalaryRaisingList result = manageEmployeeSalary.raiseSalary();

        assertEquals(2, result.salaryList.size());

        Optional<EmployeeSalaryRaising> BobSalary = result.salaryList.stream().filter(s -> s.id == 1).findFirst();
        assertEquals("Bob", BobSalary.get().name);
        assertEquals(11000, BobSalary.get().currentSalary);

        Optional<EmployeeSalaryRaising> AndySalary = result.salaryList.stream().filter(s -> s.id == 2).findFirst();
        assertEquals("Andy", AndySalary.get().name);
        assertEquals(22000, AndySalary.get().currentSalary);

        verify(mockedEmployeeRepository, times(1)).findAll();
        verify(mockedEmployeeSalaryRepository, times(1)).findByEmployeeIds(Arrays.asList(1, 2));

        ArgumentCaptor<List> argument = ArgumentCaptor.forClass(List.class);
        verify(mockedEmployeeSalaryRepository, times(1)).save(argument.capture());
        List<EmployeeSalary> employeeSalaries = argument.getValue();

        Optional<EmployeeSalary> bobSalary = employeeSalaries.stream().filter(s -> s.getEmployeeId() == 1).findFirst();
        assertEquals(11000, bobSalary.get().getAmount());
        assertEquals("2021-01-01", bobSalary.get().getEffectiveDate().toString());

        Optional<EmployeeSalary> andySalary = employeeSalaries.stream().filter(s -> s.getEmployeeId() == 2).findFirst();
        assertEquals(22000, andySalary.get().getAmount());
        assertEquals("2021-01-01", andySalary.get().getEffectiveDate().toString());
    }

    @Test
    public void should_not_raise_salary_for_employee_who_engaged_less_than_2_years(){
        EmployeeRepository mockedEmployeeRepository = mock(EmployeeRepository.class);
        EmployeeSalaryRepository mockedEmployeeSalaryRepository = mock(EmployeeSalaryRepository.class);

        when(mockedEmployeeRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(
                new Employee(1, "Bob", true),
                new Employee(2, "Andy", true)
        )));

        when(mockedEmployeeSalaryRepository.findByEmployeeIds(new ArrayList<>(Arrays.asList(1, 2)))).thenReturn(
                new ArrayList<>(Arrays.asList(
                        new EmployeeSalary(LocalDate.parse("2019-02-02"), 1, 10000),
                        new EmployeeSalary(LocalDate.parse("2020-02-02"), 2, 20000)
                )));

        EmployeeService employeeService = new EmployeeService(mockedEmployeeRepository);
        EmployeeSalaryService employeeSalaryService = new EmployeeSalaryService(mockedEmployeeSalaryRepository);
        ManageEmployeeSalary manageEmployeeSalary = new ManageEmployeeSalary(employeeService, employeeSalaryService);

        EmployeeSalaryRaisingList result = manageEmployeeSalary.raiseSalary();

        assertEquals(1, result.salaryList.size());

        Optional<EmployeeSalaryRaising> BobSalary = result.salaryList.stream().filter(s -> s.id == 1).findFirst();
        assertEquals(11000, BobSalary.get().currentSalary);
    }

    @Test
    public void should_not_raise_salary_for_employee_who_raised_salary(){
        EmployeeRepository mockedEmployeeRepository = mock(EmployeeRepository.class);
        EmployeeSalaryRepository mockedEmployeeSalaryRepository = mock(EmployeeSalaryRepository.class);

        when(mockedEmployeeRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(
                new Employee(1, "Bob", true),
                new Employee(2, "Andy", true)
        )));

        when(mockedEmployeeSalaryRepository.findByEmployeeIds(new ArrayList<>(Arrays.asList(1, 2)))).thenReturn(
                new ArrayList<>(Arrays.asList(
                        new EmployeeSalary(LocalDate.parse("2019-02-02"), 1, 10000),
                        new EmployeeSalary(LocalDate.parse("2018-02-02"), 2, 20000),
                        new EmployeeSalary(LocalDate.parse("2017-02-02"), 2, 10000)
                )));

        EmployeeService employeeService = new EmployeeService(mockedEmployeeRepository);
        EmployeeSalaryService employeeSalaryService = new EmployeeSalaryService(mockedEmployeeSalaryRepository);
        ManageEmployeeSalary manageEmployeeSalary = new ManageEmployeeSalary(employeeService, employeeSalaryService);

        EmployeeSalaryRaisingList result = manageEmployeeSalary.raiseSalary();

        assertEquals(1, result.salaryList.size());

        Optional<EmployeeSalaryRaising> BobSalary = result.salaryList.stream().filter(s -> s.id == 1).findFirst();
        assertEquals(11000, BobSalary.get().currentSalary);
    }

    @Test
    public void should_not_raise_salary_for_employee_who_is_inactive(){
        EmployeeRepository mockedEmployeeRepository = mock(EmployeeRepository.class);
        EmployeeSalaryRepository mockedEmployeeSalaryRepository = mock(EmployeeSalaryRepository.class);

        when(mockedEmployeeRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(
                new Employee(1, "Bob", true),
                new Employee(2, "Andy", false)
        )));

        when(mockedEmployeeSalaryRepository.findByEmployeeIds(any())).thenReturn(
                new ArrayList<>(Arrays.asList(
                        new EmployeeSalary(LocalDate.parse("2019-02-02"), 1, 10000)
                )));

        EmployeeService employeeService = new EmployeeService(mockedEmployeeRepository);
        EmployeeSalaryService employeeSalaryService = new EmployeeSalaryService(mockedEmployeeSalaryRepository);
        ManageEmployeeSalary manageEmployeeSalary = new ManageEmployeeSalary(employeeService, employeeSalaryService);

        EmployeeSalaryRaisingList result = manageEmployeeSalary.raiseSalary();

        assertEquals(1, result.salaryList.size());

        Optional<EmployeeSalaryRaising> BobSalary = result.salaryList.stream().filter(s -> s.id == 1).findFirst();
        assertEquals(11000, BobSalary.get().currentSalary);
    }
}


