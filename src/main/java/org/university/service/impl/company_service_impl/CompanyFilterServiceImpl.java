package org.university.service.impl.company_service_impl;

import org.university.dao.CompanyDao;
import org.university.dao.EmployeeDao;
import org.university.entity.Company;
import org.university.service.contract.company_service.CompanyFilterService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class CompanyFilterServiceImpl implements CompanyFilterService {
    private final CompanyDao companyDao;
    private final EmployeeDao employeeDao;

    public CompanyFilterServiceImpl(CompanyDao companyDao, EmployeeDao employeeDao) {
        this.companyDao = companyDao;
        this.employeeDao = employeeDao;
    }

    @Override
    public List<Company> filterCompaniesWithRevenueOver(BigDecimal threshold) {
        return companyDao.getAllCompanies().stream()
                .filter(company -> {
                    BigDecimal companyRevenue = company.getRevenue();
                    if(companyRevenue == null){
                        companyRevenue = BigDecimal.ZERO;
                    }
                    return companyRevenue.compareTo(threshold) > 0;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Company> filterCompaniesWhichNameIncludes(String matchString) {
        return companyDao.getAllCompanies()
                .stream()
                .filter(company -> company.getName().toLowerCase().contains(matchString.toLowerCase()))
                .collect(Collectors.toList());
    }
}
