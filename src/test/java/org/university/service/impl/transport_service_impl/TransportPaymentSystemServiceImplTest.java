package org.university.service.impl.transport_service_impl;

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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransportPaymentSystemServiceImplTest {

    @Mock TransportDao transportDao;
    @Mock CustomerDao customerDao;

    TransportPaymentSystemServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new TransportPaymentSystemServiceImpl(transportDao, customerDao);
    }

    @Test
    void markTransportAsPaid_shouldThrow_whenTransportMissing() {
        when(transportDao.getTransportById(1L)).thenReturn(null);

        assertThrows(DAOException.class, () -> service.markTransportAsPaid(1L));
        verify(transportDao, never()).updatePaymentStatus(anyLong(), any());
    }

    @Test
    void markTransportAsPaid_shouldReturn_whenAlreadyPaid() {
        Transport t = new Transport();
        t.setId(1L);
        t.setPaymentStatus(PaymentStatus.PAID);

        when(transportDao.getTransportById(1L)).thenReturn(t);

        service.markTransportAsPaid(1L);

        verify(transportDao, never()).updatePaymentStatus(anyLong(), any());
    }

    @Test
    void markTransportAsPaid_shouldUpdateStatus_whenNotPaid() {
        Transport t = new Transport();
        t.setId(1L);
        t.setPaymentStatus(PaymentStatus.NOT_PAID);

        when(transportDao.getTransportById(1L)).thenReturn(t);

        service.markTransportAsPaid(1L);

        verify(transportDao).updatePaymentStatus(1L, PaymentStatus.PAID);
    }

    @Test
    void calculateCustomerDebt_shouldThrow_whenCustomerMissing() {
        when(customerDao.getCustomerById(5L)).thenReturn(null);

        assertThrows(DAOException.class, () -> service.calculateCustomerDebt(5L));
        verifyNoInteractions(transportDao);
    }

    @Test
    void calculateCustomerDebt_shouldSumOnlyNotPaid_forThatCustomer() throws DAOException {
        Customer c = new Customer();
        c.setId(5L);

        Customer other = new Customer();
        other.setId(6L);

        Transport t1 = new Transport();
        t1.setId(1L);
        t1.setCustomer(c);
        t1.setPaymentStatus(PaymentStatus.NOT_PAID);
        t1.setTotalPrice(new BigDecimal("10.00"));

        Transport t2 = new Transport();
        t2.setId(2L);
        t2.setCustomer(c);
        t2.setPaymentStatus(PaymentStatus.PAID);
        t2.setTotalPrice(new BigDecimal("999.00"));

        Transport t3 = new Transport();
        t3.setId(3L);
        t3.setCustomer(c);
        t3.setPaymentStatus(PaymentStatus.NOT_PAID);
        t3.setTotalPrice(null);

        Transport t4 = new Transport();
        t4.setId(4L);
        t4.setCustomer(other);
        t4.setPaymentStatus(PaymentStatus.NOT_PAID);
        t4.setTotalPrice(new BigDecimal("100.00"));

        when(customerDao.getCustomerById(5L)).thenReturn(c);
        when(transportDao.getAllTransports()).thenReturn(List.of(t1, t2, t3, t4));

        BigDecimal debt = service.calculateCustomerDebt(5L);

        assertEquals(new BigDecimal("10.00"), debt);
    }

    @Test
    void paySingleTransport_shouldThrow_whenTransportMissing() {
        when(transportDao.getTransportById(1L)).thenReturn(null);

        assertThrows(DAOException.class, () -> service.paySingleTransport(1L));
        verify(transportDao, never()).updatePaymentStatus(anyLong(), any());
    }

    @Test
    void paySingleTransport_shouldUpdateStatus_whenExistsAndNotPaid() throws DAOException {
        Transport t = new Transport();
        t.setId(1L);
        t.setPaymentStatus(PaymentStatus.NOT_PAID);

        when(transportDao.getTransportById(1L)).thenReturn(t);

        service.paySingleTransport(1L);

        verify(transportDao).updatePaymentStatus(1L, PaymentStatus.PAID);
    }

    @Test
    void paySingleTransport_shouldNotUpdate_whenAlreadyPaid() throws DAOException {
        Transport t = new Transport();
        t.setId(1L);
        t.setPaymentStatus(PaymentStatus.PAID);

        when(transportDao.getTransportById(1L)).thenReturn(t);

        service.paySingleTransport(1L);

        verify(transportDao, never()).updatePaymentStatus(anyLong(), any());
    }

    @Test
    void getUnpaidTransportIdsForCustomer_shouldThrow_whenCustomerMissing() {
        when(customerDao.getCustomerById(5L)).thenReturn(null);

        assertThrows(DAOException.class, () -> service.getUnpaidTransportIdsForCustomer(5L));
        verifyNoInteractions(transportDao);
    }

    @Test
    void getUnpaidTransportIdsForCustomer_shouldReturnOnlyNotPaidIds_forThatCustomer() throws DAOException {
        Customer c = new Customer();
        c.setId(5L);

        Customer other = new Customer();
        other.setId(6L);

        Transport t1 = new Transport();
        t1.setId(1L);
        t1.setCustomer(c);
        t1.setPaymentStatus(PaymentStatus.NOT_PAID);

        Transport t2 = new Transport();
        t2.setId(2L);
        t2.setCustomer(c);
        t2.setPaymentStatus(PaymentStatus.PAID);

        Transport t3 = new Transport();
        t3.setId(3L);
        t3.setCustomer(other);
        t3.setPaymentStatus(PaymentStatus.NOT_PAID);

        Transport t4 = new Transport();
        t4.setId(4L);
        t4.setCustomer(c);
        t4.setPaymentStatus(PaymentStatus.NOT_PAID);

        when(customerDao.getCustomerById(5L)).thenReturn(c);
        when(transportDao.getAllTransports()).thenReturn(List.of(t1, t2, t3, t4));

        Set<Long> ids = service.getUnpaidTransportIdsForCustomer(5L);

        assertEquals(Set.of(1L, 4L), ids);
    }
}
