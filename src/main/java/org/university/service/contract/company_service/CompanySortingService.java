package org.university.service.contract.company_service;

import org.university.entity.Company;

import java.util.List;

public interface CompanySortingService {
    List<Company> sortCompaniesByNameAscending(boolean isAscending);
    List<Company> sortCompaniesByRevenueAscending(boolean isAscending);
}
