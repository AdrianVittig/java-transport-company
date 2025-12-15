package org.university.service.impl.transport_service_impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.university.dao.CompanyDao;
import org.university.dao.TransportDao;
import org.university.entity.Company;
import org.university.entity.Employee;
import org.university.entity.Transport;
import org.university.exception.DAOException;
import org.university.util.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransportReportServiceImplTest {

    @Mock TransportDao transportDao;
    @Mock CompanyDao companyDao;

    TransportReportServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new TransportReportServiceImpl(transportDao, companyDao);
    }

    @Test
    void getTransportsCount() {
        when(transportDao.getAllTransports()).thenReturn(List.of(new Transport(), new Transport(), new Transport()));
        assertEquals(3, service.getTransportsCount());
    }

    @Test
    void getTotalTransportRevenue_sumsOnlyPaid_andTreatsNullTotalPriceAsZero() {
        Transport t1 = new Transport();
        t1.setPaymentStatus(PaymentStatus.PAID);
        t1.setTotalPrice(new BigDecimal("10.00"));

        Transport t2 = new Transport();
        t2.setPaymentStatus(PaymentStatus.NOT_PAID);
        t2.setTotalPrice(new BigDecimal("999.00"));

        Transport t3 = new Transport();
        t3.setPaymentStatus(PaymentStatus.PAID);
        t3.setTotalPrice(null);

        Transport t4 = new Transport();
        t4.setPaymentStatus(PaymentStatus.PAID);
        t4.setTotalPrice(new BigDecimal("2.50"));

        when(transportDao.getAllTransports()).thenReturn(List.of(t1, t2, t3, t4));

        BigDecimal total = service.getTotalTransportRevenue();

        assertEquals(new BigDecimal("12.50"), total);
    }

    @Test
    void getTransportsCountByDriver_countsOnlyTransportsWithEmployeeAndEmployeeId() {
        Employee e1 = new Employee();
        e1.setId(10L);

        Employee e2 = new Employee();
        e2.setId(20L);

        Employee eNoId = new Employee();
        eNoId.setId(null);

        Transport t1 = new Transport();
        t1.setEmployee(e1);

        Transport t2 = new Transport();
        t2.setEmployee(e1);

        Transport t3 = new Transport();
        t3.setEmployee(e2);

        Transport t4 = new Transport();
        t4.setEmployee(null);

        Transport t5 = new Transport();
        t5.setEmployee(eNoId);

        when(transportDao.getAllTransports()).thenReturn(List.of(t1, t2, t3, t4, t5));

        Map<Long, Integer> result = service.getTransportsCountByDriver();

        assertEquals(2, result.size());
        assertEquals(2, result.get(10L));
        assertEquals(1, result.get(20L));
    }

    @Test
    void getCompanyRevenueForAPeriod_shouldThrow_whenCompanyMissing() {
        when(companyDao.getCompanyById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () ->
                service.getCompanyRevenueForAPeriod(1L, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31))
        );
        verifyNoInteractions(transportDao);
    }

    @Test
    void getCompanyRevenueForAPeriod_sumsPaidForCompanyWithinInclusiveDepartureDateRange() {
        Company c1 = Company.builder().name("C1").revenue(BigDecimal.ZERO).build();
        c1.setId(1L);

        Company c2 = Company.builder().name("C2").revenue(BigDecimal.ZERO).build();
        c2.setId(2L);

        when(companyDao.getCompanyById(1L)).thenReturn(c1);

        Transport inRangePaid1 = new Transport();
        inRangePaid1.setCompany(c1);
        inRangePaid1.setPaymentStatus(PaymentStatus.PAID);
        inRangePaid1.setDepartureDate(LocalDate.of(2025, 1, 10));
        inRangePaid1.setTotalPrice(new BigDecimal("10.00"));

        Transport inRangePaidNullPrice = new Transport();
        inRangePaidNullPrice.setCompany(c1);
        inRangePaidNullPrice.setPaymentStatus(PaymentStatus.PAID);
        inRangePaidNullPrice.setDepartureDate(LocalDate.of(2025, 1, 20));
        inRangePaidNullPrice.setTotalPrice(null);

        Transport inRangeNotPaid = new Transport();
        inRangeNotPaid.setCompany(c1);
        inRangeNotPaid.setPaymentStatus(PaymentStatus.NOT_PAID);
        inRangeNotPaid.setDepartureDate(LocalDate.of(2025, 1, 15));
        inRangeNotPaid.setTotalPrice(new BigDecimal("999.00"));

        Transport outOfRangePaid = new Transport();
        outOfRangePaid.setCompany(c1);
        outOfRangePaid.setPaymentStatus(PaymentStatus.PAID);
        outOfRangePaid.setDepartureDate(LocalDate.of(2025, 2, 1));
        outOfRangePaid.setTotalPrice(new BigDecimal("50.00"));

        Transport otherCompanyPaid = new Transport();
        otherCompanyPaid.setCompany(c2);
        otherCompanyPaid.setPaymentStatus(PaymentStatus.PAID);
        otherCompanyPaid.setDepartureDate(LocalDate.of(2025, 1, 12));
        otherCompanyPaid.setTotalPrice(new BigDecimal("100.00"));

        Transport nullDepartureDatePaid = new Transport();
        nullDepartureDatePaid.setCompany(c1);
        nullDepartureDatePaid.setPaymentStatus(PaymentStatus.PAID);
        nullDepartureDatePaid.setDepartureDate(null);
        nullDepartureDatePaid.setTotalPrice(new BigDecimal("7.00"));

        when(transportDao.getAllTransports()).thenReturn(List.of(
                inRangePaid1,
                inRangePaidNullPrice,
                inRangeNotPaid,
                outOfRangePaid,
                otherCompanyPaid,
                nullDepartureDatePaid
        ));

        BigDecimal total = service.getCompanyRevenueForAPeriod(
                1L,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 31)
        );

        assertEquals(new BigDecimal("10.00"), total);
    }

    @Test
    void getDriverRevenue_sumsPaidRevenuePerDriver_andSkipsNullEmployeeOrEmployeeId_andTreatsNullPriceAsZero() {
        Employee e1 = new Employee();
        e1.setId(10L);

        Employee e2 = new Employee();
        e2.setId(20L);

        Employee eNoId = new Employee();
        eNoId.setId(null);

        Transport paidE1_1 = new Transport();
        paidE1_1.setEmployee(e1);
        paidE1_1.setPaymentStatus(PaymentStatus.PAID);
        paidE1_1.setTotalPrice(new BigDecimal("10.00"));

        Transport paidE1_2_nullPrice = new Transport();
        paidE1_2_nullPrice.setEmployee(e1);
        paidE1_2_nullPrice.setPaymentStatus(PaymentStatus.PAID);
        paidE1_2_nullPrice.setTotalPrice(null);

        Transport notPaidE1 = new Transport();
        notPaidE1.setEmployee(e1);
        notPaidE1.setPaymentStatus(PaymentStatus.NOT_PAID);
        notPaidE1.setTotalPrice(new BigDecimal("999.00"));

        Transport paidE2 = new Transport();
        paidE2.setEmployee(e2);
        paidE2.setPaymentStatus(PaymentStatus.PAID);
        paidE2.setTotalPrice(new BigDecimal("2.50"));

        Transport paidNullEmployee = new Transport();
        paidNullEmployee.setEmployee(null);
        paidNullEmployee.setPaymentStatus(PaymentStatus.PAID);
        paidNullEmployee.setTotalPrice(new BigDecimal("100.00"));

        Transport paidNoIdEmployee = new Transport();
        paidNoIdEmployee.setEmployee(eNoId);
        paidNoIdEmployee.setPaymentStatus(PaymentStatus.PAID);
        paidNoIdEmployee.setTotalPrice(new BigDecimal("100.00"));

        when(transportDao.getAllTransports()).thenReturn(List.of(
                paidE1_1,
                paidE1_2_nullPrice,
                notPaidE1,
                paidE2,
                paidNullEmployee,
                paidNoIdEmployee
        ));

        Map<Long, BigDecimal> result = service.getDriverRevenue();

        assertEquals(2, result.size());
        assertEquals(new BigDecimal("10.00"), result.get(10L));
        assertEquals(new BigDecimal("2.50"), result.get(20L));
    }
}
