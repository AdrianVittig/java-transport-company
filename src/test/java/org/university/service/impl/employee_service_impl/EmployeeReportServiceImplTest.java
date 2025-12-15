package org.university.service.impl.employee_service_impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.university.dao.EmployeeDao;
import org.university.dao.TransportDao;
import org.university.entity.Employee;
import org.university.entity.Transport;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeReportServiceImplTest {

    @Mock TransportDao transportDao;
    @Mock EmployeeDao employeeDao;

    EmployeeReportServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new EmployeeReportServiceImpl(transportDao, employeeDao);
    }

    @Test
    void getAverageTransportRevenuePerEmployee_returnsZero_whenEmployeeMissing() {
        when(employeeDao.getEmployeeById(1L)).thenReturn(null);

        BigDecimal result = service.getAverageTransportRevenuePerEmployee(1L);

        assertEquals(BigDecimal.ZERO, result);
        verifyNoInteractions(transportDao);
    }

    @Test
    void getAverageTransportRevenuePerEmployee_returnsZero_whenNoMatchingTransports() {
        Employee e = new Employee();
        e.setId(1L);

        when(employeeDao.getEmployeeById(1L)).thenReturn(e);
        when(transportDao.getAllTransports()).thenReturn(List.of());

        BigDecimal result = service.getAverageTransportRevenuePerEmployee(1L);

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void getAverageTransportRevenuePerEmployee_averagesOnlyEmployeeTransportsWithNonNullId_andTreatsNullPriceAsZero() {
        Employee e1 = new Employee();
        e1.setId(1L);

        Employee e2 = new Employee();
        e2.setId(2L);

        Transport t1 = new Transport();
        t1.setId(10L);
        t1.setEmployee(e1);
        t1.setTotalPrice(new BigDecimal("10.00"));

        Transport t2NullPrice = new Transport();
        t2NullPrice.setId(11L);
        t2NullPrice.setEmployee(e1);
        t2NullPrice.setTotalPrice(null);

        Transport t3NullId = new Transport();
        t3NullId.setId(null);
        t3NullId.setEmployee(e1);
        t3NullId.setTotalPrice(new BigDecimal("999.00"));

        Transport t4OtherEmployee = new Transport();
        t4OtherEmployee.setId(12L);
        t4OtherEmployee.setEmployee(e2);
        t4OtherEmployee.setTotalPrice(new BigDecimal("20.00"));

        when(employeeDao.getEmployeeById(1L)).thenReturn(e1);
        when(transportDao.getAllTransports()).thenReturn(List.of(t1, t2NullPrice, t3NullId, t4OtherEmployee));

        BigDecimal result = service.getAverageTransportRevenuePerEmployee(1L);

        assertEquals(new BigDecimal("5.00"), result);
    }
}
