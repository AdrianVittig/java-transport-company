package org.university.service.impl.customer_service_impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.university.dao.CustomerDao;
import org.university.dao.TransportDao;
import org.university.entity.Customer;
import org.university.entity.Transport;
import org.university.exception.DAOException;
import org.university.util.PaymentStatus;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerReportServiceImplTest {

    @Mock CustomerDao customerDao;
    @Mock TransportDao transportDao;

    CustomerReportServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CustomerReportServiceImpl(customerDao, transportDao);
    }

    @Test
    void getCustomerTotalSpent_shouldThrow_whenCustomerMissing() {
        when(customerDao.getCustomerById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.getCustomerTotalSpent(1L));
        verifyNoInteractions(transportDao);
    }

    @Test
    void getCustomerTotalSpent_shouldSumOnlyPaidForCustomer_andTreatNullPriceAsZero() throws DAOException {
        Customer c1 = new Customer();
        c1.setId(1L);

        Customer c2 = new Customer();
        c2.setId(2L);

        Transport paid1 = Transport.builder().paymentStatus(PaymentStatus.PAID).totalPrice(new BigDecimal("10.00")).build();
        paid1.setId(101L);
        paid1.setCustomer(c1);

        Transport paid2Null = Transport.builder().paymentStatus(PaymentStatus.PAID).totalPrice(null).build();
        paid2Null.setId(102L);
        paid2Null.setCustomer(c1);

        Transport notPaid = Transport.builder().paymentStatus(PaymentStatus.NOT_PAID).totalPrice(new BigDecimal("999.00")).build();
        notPaid.setId(103L);
        notPaid.setCustomer(c1);

        Transport otherCustomerPaid = Transport.builder().paymentStatus(PaymentStatus.PAID).totalPrice(new BigDecimal("50.00")).build();
        otherCustomerPaid.setId(104L);
        otherCustomerPaid.setCustomer(c2);

        when(customerDao.getCustomerById(1L)).thenReturn(c1);
        when(transportDao.getAllTransports()).thenReturn(List.of(paid1, paid2Null, notPaid, otherCustomerPaid));

        BigDecimal total = service.getCustomerTotalSpent(1L);

        assertEquals(new BigDecimal("10.00"), total);
    }

    @Test
    void getCustomerTransportsCount_shouldThrow_whenCustomerMissing() {
        when(customerDao.getCustomerById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.getCustomerTransportsCount(1L));
        verifyNoInteractions(transportDao);
    }

    @Test
    void getCustomerTransportsCount_shouldCountOnlyPaidForCustomer() throws DAOException {
        Customer c1 = new Customer();
        c1.setId(1L);

        Customer c2 = new Customer();
        c2.setId(2L);

        Transport paid1 = Transport.builder().paymentStatus(PaymentStatus.PAID).totalPrice(new BigDecimal("10.00")).build();
        paid1.setId(101L);
        paid1.setCustomer(c1);

        Transport paid2 = Transport.builder().paymentStatus(PaymentStatus.PAID).totalPrice(new BigDecimal("20.00")).build();
        paid2.setId(102L);
        paid2.setCustomer(c1);

        Transport notPaid = Transport.builder().paymentStatus(PaymentStatus.NOT_PAID).totalPrice(new BigDecimal("30.00")).build();
        notPaid.setId(103L);
        notPaid.setCustomer(c1);

        Transport otherCustomerPaid = Transport.builder().paymentStatus(PaymentStatus.PAID).totalPrice(new BigDecimal("40.00")).build();
        otherCustomerPaid.setId(104L);
        otherCustomerPaid.setCustomer(c2);

        when(customerDao.getCustomerById(1L)).thenReturn(c1);
        when(transportDao.getAllTransports()).thenReturn(List.of(paid1, paid2, notPaid, otherCustomerPaid));

        int count = service.getCustomerTransportsCount(1L);

        assertEquals(2, count);
    }

    @Test
    void getAverageSpendingPerTransport_shouldThrow_whenCustomerMissing() {
        when(customerDao.getCustomerById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.getAverageSpendingPerTransport(1L));
        verifyNoInteractions(transportDao);
    }

    @Test
    void getAverageSpendingPerTransport_shouldReturnZero_whenNoTransportsForCustomer() throws DAOException {
        Customer c1 = new Customer();
        c1.setId(1L);

        Customer other = new Customer();
        other.setId(2L);

        Transport tOther = Transport.builder().totalPrice(new BigDecimal("10.00")).build();
        tOther.setId(201L);
        tOther.setCustomer(other);

        when(customerDao.getCustomerById(1L)).thenReturn(c1);
        when(transportDao.getAllTransports()).thenReturn(List.of(tOther));

        BigDecimal avg = service.getAverageSpendingPerTransport(1L);

        assertEquals(BigDecimal.ZERO, avg);
    }

    @Test
    void getAverageSpendingPerTransport_shouldComputeAverageAndRound_andTreatNullPriceAsZero() throws DAOException {
        Customer c1 = new Customer();
        c1.setId(1L);

        Transport t1 = Transport.builder().totalPrice(new BigDecimal("10.00")).build();
        t1.setId(201L);
        t1.setCustomer(c1);

        Transport t2 = Transport.builder().totalPrice(new BigDecimal("0.00")).build();
        t2.setId(202L);
        t2.setCustomer(c1);

        Transport t3 = Transport.builder().totalPrice(null).build();
        t3.setId(203L);
        t3.setCustomer(c1);

        when(customerDao.getCustomerById(1L)).thenReturn(c1);
        when(transportDao.getAllTransports()).thenReturn(List.of(t1, t2, t3));

        BigDecimal avg = service.getAverageSpendingPerTransport(1L);

        assertEquals(new BigDecimal("3.33"), avg);
    }

    @Test
    void getAverageSpendingPerTransport_shouldIgnoreTransportsWithNullId() throws DAOException {
        Customer c1 = new Customer();
        c1.setId(1L);

        Transport nullId = Transport.builder().totalPrice(new BigDecimal("100.00")).build();
        nullId.setId(null);
        nullId.setCustomer(c1);

        Transport ok = Transport.builder().totalPrice(new BigDecimal("10.00")).build();
        ok.setId(301L);
        ok.setCustomer(c1);

        when(customerDao.getCustomerById(1L)).thenReturn(c1);
        when(transportDao.getAllTransports()).thenReturn(List.of(nullId, ok));

        BigDecimal avg = service.getAverageSpendingPerTransport(1L);

        assertEquals(new BigDecimal("10.00"), avg);
    }
}
