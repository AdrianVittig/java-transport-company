package org.university.service.impl.customer_service_impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.university.dto.CustomerDto;
import org.university.entity.Customer;
import org.university.exception.DAOException;
import org.university.service.contract.customer_service.CustomerCRUDService;
import org.university.service.contract.customer_service.CustomerReportService;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerGeneralServiceImplTest {

    @Mock CustomerCRUDService crud;
    @Mock CustomerReportService report;

    CustomerGeneralServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CustomerGeneralServiceImpl(crud, report);
    }

    @Test
    void mapToEntity() {
        CustomerDto dto = new CustomerDto();
        Customer c = new Customer();

        when(crud.mapToEntity(dto)).thenReturn(c);

        assertSame(c, service.mapToEntity(dto));
        verify(crud).mapToEntity(dto);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(report);
    }

    @Test
    void mapToDto() {
        Customer c = new Customer();
        CustomerDto dto = new CustomerDto();

        when(crud.mapToDto(c)).thenReturn(dto);

        assertSame(dto, service.mapToDto(c));
        verify(crud).mapToDto(c);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(report);
    }

    @Test
    void createCustomer() throws DAOException {
        CustomerDto in = new CustomerDto();
        CustomerDto out = new CustomerDto();

        when(crud.createCustomer(in)).thenReturn(out);

        assertSame(out, service.createCustomer(in));
        verify(crud).createCustomer(in);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(report);
    }

    @Test
    void getCustomerById() throws DAOException {
        CustomerDto out = new CustomerDto();
        when(crud.getCustomerById(1L)).thenReturn(out);

        assertSame(out, service.getCustomerById(1L));
        verify(crud).getCustomerById(1L);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(report);
    }

    @Test
    void getAllCustomers() {
        Set<CustomerDto> set = Set.of(new CustomerDto(), new CustomerDto());
        when(crud.getAllCustomers()).thenReturn(set);

        assertSame(set, service.getAllCustomers());
        verify(crud).getAllCustomers();
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(report);
    }

    @Test
    void updateCustomer() throws DAOException {
        CustomerDto in = new CustomerDto();
        CustomerDto out = new CustomerDto();

        when(crud.updateCustomer(1L, in)).thenReturn(out);

        assertSame(out, service.updateCustomer(1L, in));
        verify(crud).updateCustomer(1L, in);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(report);
    }

    @Test
    void deleteCustomer() throws DAOException {
        doNothing().when(crud).deleteCustomer(1L);

        service.deleteCustomer(1L);

        verify(crud).deleteCustomer(1L);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(report);
    }

    @Test
    void addTransportToCustomer() throws DAOException {
        doNothing().when(crud).addTransportToCustomer(1L, 10L);

        service.addTransportToCustomer(1L, 10L);

        verify(crud).addTransportToCustomer(1L, 10L);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(report);
    }

    @Test
    void removeTransportFromCustomer() throws DAOException {
        doNothing().when(crud).removeTransportFromCustomer(1L, 10L);

        service.removeTransportFromCustomer(1L, 10L);

        verify(crud).removeTransportFromCustomer(1L, 10L);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(report);
    }

    @Test
    void getAllTransportIdsForCustomer() throws DAOException {
        Set<Long> ids = Set.of(100L, 101L);
        when(crud.getAllTransportIdsForCustomer(1L)).thenReturn(ids);

        assertSame(ids, service.getAllTransportIdsForCustomer(1L));
        verify(crud).getAllTransportIdsForCustomer(1L);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(report);
    }

    @Test
    void getCustomerTotalSpent() throws DAOException {
        when(report.getCustomerTotalSpent(1L)).thenReturn(new BigDecimal("12.34"));

        BigDecimal result = service.getCustomerTotalSpent(1L);

        assertEquals(0, new BigDecimal("12.34").compareTo(result));
        verify(report).getCustomerTotalSpent(1L);
        verifyNoMoreInteractions(report);
        verifyNoInteractions(crud);
    }

    @Test
    void getCustomerTransportsCount() throws DAOException {
        when(report.getCustomerTransportsCount(1L)).thenReturn(7);

        assertEquals(7, service.getCustomerTransportsCount(1L));
        verify(report).getCustomerTransportsCount(1L);
        verifyNoMoreInteractions(report);
        verifyNoInteractions(crud);
    }

    @Test
    void getAverageSpendingPerTransport() {
        when(report.getAverageSpendingPerTransport(1L)).thenReturn(new BigDecimal("3.21"));

        BigDecimal result = service.getAverageSpendingPerTransport(1L);

        assertEquals(0, new BigDecimal("3.21").compareTo(result));
        verify(report).getAverageSpendingPerTransport(1L);
        verifyNoMoreInteractions(report);
        verifyNoInteractions(crud);
    }
}
