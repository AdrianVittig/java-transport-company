package org.university.service.contract.company_service;

import org.university.exception.DAOException;

import java.math.BigDecimal;

public interface CompanyReportService {
    BigDecimal getCompanyTotalRevenue(Long companyId) throws DAOException;
    int getCompanyTransportsCount(Long companyId) throws DAOException;
    BigDecimal getCompanyAverageTransportRevenue(Long companyId) throws DAOException;
}
