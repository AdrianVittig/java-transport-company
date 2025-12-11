package org.university.service.impl.company_service_impl;

import org.university.dao.CompanyDao;
import org.university.entity.Company;
import org.university.service.contract.company_service.CompanySortingService;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

public class CompanySortingServiceImpl implements CompanySortingService {
    private final CompanyDao companyDao;

    public CompanySortingServiceImpl(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }

    @Override
    public List<Company> sortCompaniesByNameAscending() {
        return companyDao.getAllCompanies()
                .stream()
                .sorted(Comparator.comparing(company -> {
                    String name = company.getName();
                    return name == null ? "" : name.toLowerCase();
                }))
                .toList();
    }

    @Override
    public List<Company> sortCompaniesByNameDescending() {
        return companyDao.getAllCompanies()
                .stream()
                .sorted(
                        Comparator.comparing((Company c) -> {
                            String name = c.getName();
                            return name == null ? "" : name.toLowerCase();
                        }).reversed()
                )
                .toList();
    }

    @Override
    public List<Company> sortCompaniesByRevenueAscending() {
        return companyDao.getAllCompanies()
                .stream()
                .sorted(Comparator.comparing(company -> {
                    BigDecimal revenue = company.getRevenue();
                    return revenue != null ? revenue : BigDecimal.ZERO;
                }))
                .toList();
    }

    @Override
    public List<Company> sortCompaniesByRevenueDescending() {
        return companyDao.getAllCompanies()
                .stream()
                .sorted(
                        Comparator.comparing((Company c) -> {
                            BigDecimal revenue = c.getRevenue();
                            return revenue != null ? revenue : BigDecimal.ZERO;
                        }).reversed()
                )
                .toList();
    }

}
