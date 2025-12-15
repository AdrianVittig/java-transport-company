package org.university.service.impl.company_service_impl;

import org.university.dao.CompanyDao;
import org.university.entity.Company;
import org.university.service.contract.company_service.CompanyFilterService;

import java.math.BigDecimal;
import java.util.List;

public class CompanyFilterServiceImpl implements CompanyFilterService {
    private final CompanyDao companyDao;

    public CompanyFilterServiceImpl(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }

    @Override
    public List<Company> filterCompaniesWithRevenueOver(BigDecimal threshold) {
        if (threshold == null) {
            return List.of();
        }

        return companyDao.filterByRevenue(threshold);
    }

    @Override
    public List<Company> filterCompaniesWhichNameIncludes(String matchString) {
        if (matchString == null || matchString.isBlank()) {
            return List.of();
        }

        return companyDao.filterByName(matchString.trim());
    }
}
