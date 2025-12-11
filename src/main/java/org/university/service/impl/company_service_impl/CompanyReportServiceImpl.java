package org.university.service.impl.company_service_impl;

import org.university.dao.CompanyDao;
import org.university.entity.Company;
import org.university.exception.DAOException;
import org.university.service.contract.company_service.CompanyReportService;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CompanyReportServiceImpl implements CompanyReportService {
    private final CompanyDao companyDao;

    public CompanyReportServiceImpl(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }

    @Override
    public BigDecimal getCompanyTotalRevenue(Long companyId) throws DAOException {
        Company company = companyDao.getCompanyById(companyId);
        if(company == null){
            throw new DAOException("Company with id " + companyId + " does not exist");
        }
        return company.getRevenue() != null ? company.getRevenue() : BigDecimal.ZERO;
    }

    @Override
    public int getCompanyTransportsCount(Long companyId) throws DAOException{
        Company company = companyDao.getCompanyById(companyId);
        if(company == null){
            throw new DAOException("Company with id " + companyId + " does not exist");
        }
        int count = company.getTransportSet().size();
        return company.getTransportSet() != null ? count : 0;
    }

    @Override
    public BigDecimal getCompanyAverageTransportRevenue(Long companyId) throws DAOException{
        Company company = companyDao.getCompanyById(companyId);
        if(company == null){
            throw new DAOException("Company with id " + companyId + " does not exist");
        }
        if(company.getTransportSet().size() > 0){
            int transportsCount = company.getTransportSet().size();
            BigDecimal totalRevenue = company.getRevenue();
            if(totalRevenue == null){
                totalRevenue = BigDecimal.ZERO;
            }
            BigDecimal averagePrice = totalRevenue.divide(BigDecimal.valueOf(transportsCount), 2, RoundingMode.HALF_UP);
            return averagePrice;
        }
        else{
            return BigDecimal.ZERO;
        }
    }
}
