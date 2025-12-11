package org.university.service.contract.customer_service;

import org.university.exception.DAOException;

import java.math.BigDecimal;

public interface CustomerReportService {
    BigDecimal getCustomerTotalSpent(Long customerId) throws DAOException;
    int getCustomerTransportsCount(Long customerId) throws DAOException;
    BigDecimal getAverageSpendingPerTransport(Long customerId) throws DAOException;
}
