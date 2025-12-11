package org.university.service.contract.transport_service;

import org.university.exception.DAOException;

import java.math.BigDecimal;
import java.util.Set;

public interface TransportPaymentSystemService {
    void markTransportAsPaid(Long transportId);
    BigDecimal calculateCustomerDebt(Long customerId) throws DAOException;
    void paySingleTransport(Long transportId) throws DAOException;
    Set<Long> getUnpaidTransportIdsForCustomer(Long customerId) throws DAOException;
}
