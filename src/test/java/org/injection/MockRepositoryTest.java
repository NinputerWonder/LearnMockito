package org.injection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MockRepositoryTest {

    @Test
    public void should_raise_salary_for_employee_who_engaged_more_than_2_years_and_never_raised_salary(){
        EmployeeRepository mockedEmployeeRepository = mockEmployeeRepository(
                new Employee(1, "Bob", true),
                new Employee(2, "Andy", true));

        EmployeeSalaryRepository mockedEmployeeSalaryRepository = mockEmployeeSalaryRepository(
                new EmployeeSalary(LocalDate.parse("2019-02-02"), 1, 10000),
                new EmployeeSalary(LocalDate.parse("2018-10-09"), 2, 20000));

        ManageEmployeeSalary manageEmployeeSalary = createManageEmployeeSalary(mockedEmployeeRepository, mockedEmployeeSalaryRepository);

        EmployeeSalaryRaisingList result = manageEmployeeSalary.raiseSalary();

        assertEquals(2, result.salaryList.size());

        EmployeeSalaryRaising BobSalaryRaising = result.salaryList.stream().filter(s1 -> s1.id == 1).findFirst().get();
        assertEquals("Bob", BobSalaryRaising.name);
        assertEquals(11000, BobSalaryRaising.currentSalary);

        EmployeeSalaryRaising AndySalaryRaising = result.salaryList.stream().filter(s1 -> s1.id == 2).findFirst().get();
        assertEquals("Andy", AndySalaryRaising.name);
        assertEquals(22000, AndySalaryRaising.currentSalary);

        verify(mockedEmployeeRepository, times(1)).findAll();
        verify(mockedEmployeeSalaryRepository, times(1)).findByEmployeeIds(Arrays.asList(1, 2));

        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockedEmployeeSalaryRepository, times(1)).save(argumentCaptor.capture());
        List<EmployeeSalary> employeeSalaries = argumentCaptor.getValue();

        EmployeeSalary BobSalary = employeeSalaries.stream().filter(s1 -> s1.getEmployeeId() == 1).findFirst().get();
        assertEquals(11000, BobSalary.getAmount());
        assertEquals("2021-01-01", BobSalary.getEffectiveDate().toString());

        EmployeeSalary AndySalary = employeeSalaries.stream().filter(s -> s.getEmployeeId() == 2).findFirst().get();
        assertEquals(22000, AndySalary.getAmount());
        assertEquals("2021-01-01", AndySalary.getEffectiveDate().toString());
    }

    @Test
    public void should_not_raise_salary_for_employee_who_engaged_less_than_2_years(){
        EmployeeRepository mockedEmployeeRepository = mockEmployeeRepository(
                new Employee(1, "Bob", true),
                new Employee(2, "Andy", true));

        EmployeeSalaryRepository mockedEmployeeSalaryRepository = mockEmployeeSalaryRepository(
                new EmployeeSalary(LocalDate.parse("2019-02-02"), 1, 10000),
                new EmployeeSalary(LocalDate.parse("2020-02-02"), 2, 20000));

        ManageEmployeeSalary manageEmployeeSalary = createManageEmployeeSalary(mockedEmployeeRepository, mockedEmployeeSalaryRepository);

        EmployeeSalaryRaisingList result = manageEmployeeSalary.raiseSalary();

        assertEquals(1, result.salaryList.size());

        EmployeeSalaryRaising BobSalaryRaising = result.salaryList.stream().findFirst().get();
        assertEquals(1, BobSalaryRaising.id);
        assertEquals("Bob", result.salaryList.stream().findFirst().get().name);
        assertEquals(11000, result.salaryList.stream().findFirst().get().currentSalary);

        verify(mockedEmployeeRepository, times(1)).findAll();
        verify(mockedEmployeeSalaryRepository, times(1)).findByEmployeeIds(Arrays.asList(1, 2));

        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockedEmployeeSalaryRepository, times(1)).save(argumentCaptor.capture());
        List<EmployeeSalary> employeeSalaries = argumentCaptor.getValue();

        assertEquals(1, employeeSalaries.size());

        EmployeeSalary BobSalary = employeeSalaries.stream().findFirst().get();
        assertEquals(1, BobSalary.getEmployeeId());
        assertEquals(11000, BobSalary.getAmount());
        assertEquals("2021-01-01", BobSalary.getEffectiveDate().toString());
    }

    @Test
    public void should_not_raise_salary_for_employee_who_raised_salary(){
        EmployeeRepository mockedEmployeeRepository = mockEmployeeRepository(
                new Employee(1, "Bob", true),
                new Employee(2, "Andy", true));

        EmployeeSalaryRepository mockedEmployeeSalaryRepository = mockEmployeeSalaryRepository(
                new EmployeeSalary(LocalDate.parse("2019-02-02"), 1, 10000),
                new EmployeeSalary(LocalDate.parse("2018-02-02"), 2, 20000),
                new EmployeeSalary(LocalDate.parse("2017-02-02"), 2, 10000));

        ManageEmployeeSalary manageEmployeeSalary = createManageEmployeeSalary(mockedEmployeeRepository, mockedEmployeeSalaryRepository);

        EmployeeSalaryRaisingList result = manageEmployeeSalary.raiseSalary();

        assertEquals(1, result.salaryList.size());

        EmployeeSalaryRaising BobSalaryRaising = result.salaryList.stream().findFirst().get();
        assertEquals(1, BobSalaryRaising.id);
        assertEquals("Bob", BobSalaryRaising.name);
        assertEquals(11000, BobSalaryRaising.currentSalary);

        verify(mockedEmployeeRepository, times(1)).findAll();
        verify(mockedEmployeeSalaryRepository, times(1)).findByEmployeeIds(Arrays.asList(1, 2));

        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockedEmployeeSalaryRepository, times(1)).save(argumentCaptor.capture());
        List<EmployeeSalary> employeeSalaries = argumentCaptor.getValue();

        assertEquals(1, employeeSalaries.size());

        EmployeeSalary BobSalary = employeeSalaries.stream().findFirst().get();
        assertEquals(1, BobSalary.getEmployeeId());
        assertEquals(11000, BobSalary.getAmount());
        assertEquals("2021-01-01", BobSalary.getEffectiveDate().toString());
    }

    @Test
    public void should_not_raise_salary_for_employee_who_is_inactive(){
        EmployeeRepository mockedEmployeeRepository = mockEmployeeRepository(
                new Employee(1, "Bob", true),
                new Employee(2, "Andy", false));
        EmployeeSalaryRepository mockedEmployeeSalaryRepository = mockEmployeeSalaryRepository(
                new EmployeeSalary(LocalDate.parse("2019-02-02"), 1, 10000));

        ManageEmployeeSalary manageEmployeeSalary = createManageEmployeeSalary(mockedEmployeeRepository, mockedEmployeeSalaryRepository);

        EmployeeSalaryRaisingList result = manageEmployeeSalary.raiseSalary();

        assertEquals(1, result.salaryList.size());

        EmployeeSalaryRaising BobSalaryRaising = result.salaryList.stream().findFirst().get();
        assertEquals(1, BobSalaryRaising.id);
        assertEquals("Bob", BobSalaryRaising.name);
        assertEquals(11000, BobSalaryRaising.currentSalary);

        verify(mockedEmployeeRepository, times(1)).findAll();
        verify(mockedEmployeeSalaryRepository, times(1)).findByEmployeeIds(Arrays.asList(1));

        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockedEmployeeSalaryRepository, times(1)).save(argumentCaptor.capture());
        List<EmployeeSalary> employeeSalaries = argumentCaptor.getValue();

        assertEquals(1, employeeSalaries.size());

        EmployeeSalary BobSalary = employeeSalaries.stream().findFirst().get();
        assertEquals(1, BobSalary.getEmployeeId());
        assertEquals(11000, BobSalary.getAmount());
        assertEquals("2021-01-01", BobSalary.getEffectiveDate().toString());
    }

    private ManageEmployeeSalary createManageEmployeeSalary(EmployeeRepository mockedEmployeeRepository, EmployeeSalaryRepository mockedEmployeeSalaryRepository) {
        EmployeeService employeeService = new EmployeeService(mockedEmployeeRepository);
        EmployeeSalaryService employeeSalaryService = new EmployeeSalaryService(mockedEmployeeSalaryRepository);
        return new ManageEmployeeSalary(employeeService, employeeSalaryService);
    }

    private EmployeeSalaryRepository mockEmployeeSalaryRepository(EmployeeSalary... employeeSalaries) {
        EmployeeSalaryRepository mockedEmployeeSalaryRepository = mock(EmployeeSalaryRepository.class);
        when(mockedEmployeeSalaryRepository.findByEmployeeIds(any())).thenReturn(
                Arrays.asList(employeeSalaries));
        return mockedEmployeeSalaryRepository;
    }

    private EmployeeRepository mockEmployeeRepository(Employee... employees) {
        EmployeeRepository mockedEmployeeRepository = mock(EmployeeRepository.class);
        when(mockedEmployeeRepository.findAll()).thenReturn(Arrays.asList(employees));
        return mockedEmployeeRepository;
    }
}


