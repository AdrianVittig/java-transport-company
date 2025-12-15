package org.university.service.impl.company_service_impl;

import org.university.dao.CompanyDao;
import org.university.entity.Company;
import org.university.service.contract.company_service.CompanySortingService;

import java.util.List;

public class CompanySortingServiceImpl implements CompanySortingService {
    private final CompanyDao companyDao;

    public CompanySortingServiceImpl(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }


    @Override
    public List<Company> sortCompaniesByNameAscending(boolean isAscending) {
        return companyDao.sortCompaniesByNameAscending(isAscending);
    }

    @Override
    public List<Company> sortCompaniesByRevenueAscending(boolean isAscending) {
        return companyDao.sortCompaniesByRevenue(isAscending);
    }
}
