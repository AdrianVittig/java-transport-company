package org.university.service.impl.transport_service_impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.university.dto.TransportDto;
import org.university.entity.Transport;
import org.university.exception.DAOException;
import org.university.service.contract.transport_service.*;
import org.university.util.CargoType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransportGeneralServiceImplTest {

    @Mock TransportCRUDSystemService crud;
    @Mock TransportPricingSystemService pricing;
    @Mock TransportPaymentSystemService payment;
    @Mock TransportFilterService filter;
    @Mock TransportSortingService sorting;
    @Mock TransportReportService report;

    TransportGeneralServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new TransportGeneralServiceImpl(crud, pricing, payment, filter, sorting, report);
    }

    @Test
    void mapToEntity() {
        TransportDto dto = new TransportDto();
        Transport t = new Transport();

        when(crud.mapToEntity(dto)).thenReturn(t);

        assertSame(t, service.mapToEntity(dto));
        verify(crud).mapToEntity(dto);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(pricing, payment, filter, sorting, report);
    }

    @Test
    void mapToDto() {
        Transport t = new Transport();
        TransportDto dto = new TransportDto();

        when(crud.mapToDto(t)).thenReturn(dto);

        assertSame(dto, service.mapToDto(t));
        verify(crud).mapToDto(t);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(pricing, payment, filter, sorting, report);
    }

    @Test
    void createTransport() {
        TransportDto in = new TransportDto();
        TransportDto out = new TransportDto();

        when(crud.createTransport(in)).thenReturn(out);

        assertSame(out, service.createTransport(in));
        verify(crud).createTransport(in);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(pricing, payment, filter, sorting, report);
    }

    @Test
    void getTransportById() throws DAOException {
        TransportDto out = new TransportDto();
        when(crud.getTransportById(1L)).thenReturn(out);

        assertSame(out, service.getTransportById(1L));
        verify(crud).getTransportById(1L);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(pricing, payment, filter, sorting, report);
    }

    @Test
    void getAllTransports() {
        Set<TransportDto> set = Set.of(new TransportDto(), new TransportDto());
        when(crud.getAllTransports()).thenReturn(set);

        assertSame(set, service.getAllTransports());
        verify(crud).getAllTransports();
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(pricing, payment, filter, sorting, report);
    }

    @Test
    void updateTransport() throws DAOException {
        TransportDto in = new TransportDto();
        TransportDto out = new TransportDto();
        when(crud.updateTransport(1L, in)).thenReturn(out);

        assertSame(out, service.updateTransport(1L, in));
        verify(crud).updateTransport(1L, in);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(pricing, payment, filter, sorting, report);
    }

    @Test
    void deleteTransport() throws DAOException {
        doNothing().when(crud).deleteTransport(1L);

        service.deleteTransport(1L);

        verify(crud).deleteTransport(1L);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(pricing, payment, filter, sorting, report);
    }

    @Test
    void getCompanyIdForTransport() throws DAOException {
        when(crud.getCompanyIdForTransport(1L)).thenReturn(10L);

        assertEquals(10L, service.getCompanyIdForTransport(1L));
        verify(crud).getCompanyIdForTransport(1L);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(pricing, payment, filter, sorting, report);
    }

    @Test
    void getEmployeeIdForTransport() throws DAOException {
        when(crud.getEmployeeIdForTransport(1L)).thenReturn(20L);

        assertEquals(20L, service.getEmployeeIdForTransport(1L));
        verify(crud).getEmployeeIdForTransport(1L);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(pricing, payment, filter, sorting, report);
    }

    @Test
    void getCustomerIdForTransport() throws DAOException {
        when(crud.getCustomerIdForTransport(1L)).thenReturn(30L);

        assertEquals(30L, service.getCustomerIdForTransport(1L));
        verify(crud).getCustomerIdForTransport(1L);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(pricing, payment, filter, sorting, report);
    }

    @Test
    void getVehicleIdForTransport() throws DAOException {
        when(crud.getVehicleIdForTransport(1L)).thenReturn(40L);

        assertEquals(40L, service.getVehicleIdForTransport(1L));
        verify(crud).getVehicleIdForTransport(1L);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(pricing, payment, filter, sorting, report);
    }

    @Test
    void filterTransportsByDestination() {
        Transport t1 = new Transport();
        Transport t2 = new Transport();
        List<Transport> list = List.of(t1, t2);

        when(filter.filterTransportsByDestination("Sofia")).thenReturn(list);

        assertSame(list, service.filterTransportsByDestination("Sofia"));
        verify(filter).filterTransportsByDestination("Sofia");
        verifyNoMoreInteractions(filter);
        verifyNoInteractions(crud, pricing, payment, sorting, report);
    }

    @Test
    void markTransportAsPaid() {
        doNothing().when(payment).markTransportAsPaid(1L);

        service.markTransportAsPaid(1L);

        verify(payment).markTransportAsPaid(1L);
        verifyNoMoreInteractions(payment);
        verifyNoInteractions(crud, pricing, filter, sorting, report);
    }

    @Test
    void calculateCustomerDebt() throws DAOException {
        when(payment.calculateCustomerDebt(1L)).thenReturn(new BigDecimal("12.34"));

        BigDecimal result = service.calculateCustomerDebt(1L);

        assertEquals(0, new BigDecimal("12.34").compareTo(result));
        verify(payment).calculateCustomerDebt(1L);
        verifyNoMoreInteractions(payment);
        verifyNoInteractions(crud, pricing, filter, sorting, report);
    }

    @Test
    void paySingleTransport() throws DAOException {
        doNothing().when(payment).paySingleTransport(1L);

        service.paySingleTransport(1L);

        verify(payment).paySingleTransport(1L);
        verifyNoMoreInteractions(payment);
        verifyNoInteractions(crud, pricing, filter, sorting, report);
    }

    @Test
    void getUnpaidTransportIdsForCustomer() throws DAOException {
        Set<Long> ids = Set.of(1L, 2L, 3L);
        when(payment.getUnpaidTransportIdsForCustomer(10L)).thenReturn(ids);

        assertSame(ids, service.getUnpaidTransportIdsForCustomer(10L));
        verify(payment).getUnpaidTransportIdsForCustomer(10L);
        verifyNoMoreInteractions(payment);
        verifyNoInteractions(crud, pricing, filter, sorting, report);
    }

    @Test
    void calculateTotalPrice() throws DAOException {
        Transport t = new Transport();
        t.setCargoType(CargoType.GOODS);

        when(pricing.calculateTotalPrice(t)).thenReturn(new BigDecimal("30.00"));

        BigDecimal result = service.calculateTotalPrice(t);

        assertEquals(0, new BigDecimal("30.00").compareTo(result));
        verify(pricing).calculateTotalPrice(t);
        verifyNoMoreInteractions(pricing);
        verifyNoInteractions(crud, payment, filter, sorting, report);
    }

    @Test
    void sortTransportsByDestinationAscending() {
        List<Transport> list = List.of(new Transport(), new Transport());
        when(sorting.sortTransportsByDestinationAscending(true)).thenReturn(list);

        assertSame(list, service.sortTransportsByDestinationAscending(true));
        verify(sorting).sortTransportsByDestinationAscending(true);
        verifyNoMoreInteractions(sorting);
        verifyNoInteractions(crud, pricing, payment, filter, report);
    }

    @Test
    void getTransportsCount() {
        when(report.getTransportsCount()).thenReturn(7);

        assertEquals(7, service.getTransportsCount());
        verify(report).getTransportsCount();
        verifyNoMoreInteractions(report);
        verifyNoInteractions(crud, pricing, payment, filter, sorting);
    }

    @Test
    void getTotalTransportRevenue() {
        when(report.getTotalTransportRevenue()).thenReturn(new BigDecimal("100.00"));

        BigDecimal result = service.getTotalTransportRevenue();

        assertEquals(0, new BigDecimal("100.00").compareTo(result));
        verify(report).getTotalTransportRevenue();
        verifyNoMoreInteractions(report);
        verifyNoInteractions(crud, pricing, payment, filter, sorting);
    }

    @Test
    void getTransportsCountByDriver() {
        Map<Long, Integer> map = Map.of(1L, 2, 3L, 4);
        when(report.getTransportsCountByDriver()).thenReturn(map);

        assertSame(map, service.getTransportsCountByDriver());
        verify(report).getTransportsCountByDriver();
        verifyNoMoreInteractions(report);
        verifyNoInteractions(crud, pricing, payment, filter, sorting);
    }

    @Test
    void getCompanyRevenueForAPeriod() {
        BigDecimal value = new BigDecimal("12.34");
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 1, 31);

        when(report.getCompanyRevenueForAPeriod(1L, start, end)).thenReturn(value);

        BigDecimal result = service.getCompanyRevenueForAPeriod(1L, start, end);

        assertEquals(0, value.compareTo(result));
        verify(report).getCompanyRevenueForAPeriod(1L, start, end);
        verifyNoMoreInteractions(report);
        verifyNoInteractions(crud, pricing, payment, filter, sorting);
    }

    @Test
    void getDriverRevenue() {
        Map<Long, BigDecimal> map = Map.of(
                1L, new BigDecimal("10.00"),
                2L, new BigDecimal("0.00")
        );

        when(report.getDriverRevenue()).thenReturn(map);

        assertSame(map, service.getDriverRevenue());
        verify(report).getDriverRevenue();
        verifyNoMoreInteractions(report);
        verifyNoInteractions(crud, pricing, payment, filter, sorting);
    }
}
