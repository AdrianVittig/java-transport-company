package org.university.service.contract.company_service;

import org.university.entity.Company;

import java.math.BigDecimal;
import java.util.List;

public interface CompanyFilterService {
    List<Company> filterCompaniesWithRevenueOver(BigDecimal threshold);
    List<Company> filterCompaniesWhichNameIncludes(String matchString);
}
