package org.university.service.contract.company_service;

import org.university.entity.Company;

import java.util.List;

public interface CompanySortingService {
    List<Company> sortCompaniesByNameAscending();
    List<Company> sortCompaniesByNameDescending();
    List<Company> sortCompaniesByRevenueAscending();
    List<Company> sortCompaniesByRevenueDescending();
}
