package org.university.service.impl.employee_service_impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.university.dao.EmployeeDao;
import org.university.entity.Employee;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeSortingServiceImplTest {

    @Mock EmployeeDao employeeDao;

    EmployeeSortingServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new EmployeeSortingServiceImpl(employeeDao);
    }

    @Test
    void sortEmployeesBySalaryAscending_delegatesToDao() {
        Employee e1 = new Employee();
        e1.setId(1L);

        when(employeeDao.sortEmployeesBySalary(true)).thenReturn(List.of(e1));

        List<Employee> result = service.sortEmployeesBySalaryAscending(true);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(employeeDao).sortEmployeesBySalary(true);
    }

    @Test
    void sortEmployeesByQualificationAscending_delegatesToDao() {
        Employee e1 = new Employee();
        e1.setId(1L);

        when(employeeDao.sortEmployeesByQualification(false)).thenReturn(List.of(e1));

        List<Employee> result = service.sortEmployeesByQualificationAscending(false);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(employeeDao).sortEmployeesByQualification(false);
    }
}
