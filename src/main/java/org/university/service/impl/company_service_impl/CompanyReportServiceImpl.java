package org.university.service.impl.company_service_impl;

import org.university.dao.CompanyDao;
import org.university.dao.TransportDao;
import org.university.entity.Company;
import org.university.entity.Transport;
import org.university.exception.DAOException;
import org.university.service.contract.company_service.CompanyReportService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CompanyReportServiceImpl implements CompanyReportService {
    private final CompanyDao companyDao;
    private final TransportDao transportDao;

    public CompanyReportServiceImpl(CompanyDao companyDao, TransportDao transportDao) {
        this.companyDao = companyDao;
        this.transportDao = transportDao;
    }

    @Override
    public BigDecimal getCompanyTotalRevenue(Long companyId) throws DAOException {
        Company company = companyDao.getCompanyById(companyId);
        if (company == null) {
            throw new DAOException("Company with id " + companyId + " does not exist");
        }
        return company.getRevenue() != null ? company.getRevenue() : BigDecimal.ZERO;
    }

    @Override
    public int getCompanyTransportsCount(Long companyId) throws DAOException {
        Company company = companyDao.getCompanyById(companyId);
        if (company == null) {
            throw new DAOException("Company with id " + companyId + " does not exist");
        }

        return (int) transportDao.getAllTransports().stream()
                .filter(t -> t.getCompany() != null && companyId.equals(t.getCompany().getId()))
                .count();
    }

    @Override
    public BigDecimal getCompanyAverageTransportRevenue(Long companyId) throws DAOException {
        Company company = companyDao.getCompanyById(companyId);
        if (company == null) {
            throw new DAOException("Company with id " + companyId + " does not exist");
        }

        List<Transport> list = transportDao.getAllTransports().stream()
                .filter(t -> t.getCompany() != null && companyId.equals(t.getCompany().getId()))
                .toList();

        if (list.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = list.stream()
                .map(t -> t.getTotalPrice() != null ? t.getTotalPrice() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total.divide(BigDecimal.valueOf(list.size()), 2, RoundingMode.HALF_UP);
    }
}
