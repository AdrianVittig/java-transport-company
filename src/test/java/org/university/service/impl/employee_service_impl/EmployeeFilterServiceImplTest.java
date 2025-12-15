package org.university.service.impl.employee_service_impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.university.dao.EmployeeDao;
import org.university.entity.Employee;
import org.university.util.DriverQualifications;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeFilterServiceImplTest {

    @Mock EmployeeDao employeeDao;

    EmployeeFilterServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new EmployeeFilterServiceImpl(employeeDao);
    }

    @Test
    void filterEmployeeByQualification() {
        Employee e1 = new Employee();
        e1.setId(1L);

        when(employeeDao.filterByQualification(DriverQualifications.CARGO)).thenReturn(List.of(e1));

        List<Employee> result = service.filterEmployeeByQualification(DriverQualifications.CARGO);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(employeeDao).filterByQualification(DriverQualifications.CARGO);
    }

    @Test
    void filterEmployeesBySalary() {
        Employee e1 = new Employee();
        e1.setId(1L);

        BigDecimal minSalary = new BigDecimal("1000.00");
        when(employeeDao.filterByMinSalary(minSalary)).thenReturn(List.of(e1));

        List<Employee> result = service.filterEmployeesBySalary(minSalary);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(employeeDao).filterByMinSalary(minSalary);
    }
}
