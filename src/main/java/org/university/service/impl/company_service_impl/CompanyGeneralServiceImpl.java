package org.university.service.impl.company_service_impl;

import org.university.dto.CompanyDto;
import org.university.entity.Company;
import org.university.exception.DAOException;
import org.university.service.contract.company_service.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class CompanyGeneralServiceImpl implements CompanyGeneralService {
    private final CompanyCRUDService crud;
    private final CompanyFilterService filter;
    private final CompanySortingService sorting;
    private final CompanyReportService report;

    public CompanyGeneralServiceImpl(CompanyCRUDService crud, CompanyFilterService filter, CompanySortingService sorting, CompanyReportService report) {
        this.crud = crud;
        this.filter = filter;
        this.sorting = sorting;
        this.report = report;
    }


    @Override
    public Company mapToEntity(CompanyDto companyDto) {
        return crud.mapToEntity(companyDto);
    }

    @Override
    public CompanyDto mapToDto(Company company) {
        return crud.mapToDto(company);
    }

    @Override
    public CompanyDto createCompany(CompanyDto companyDto) throws DAOException {
        return crud.createCompany(companyDto);
    }

    @Override
    public CompanyDto getCompanyById(Long id) {
        return crud.getCompanyById(id);
    }

    @Override
    public Set<CompanyDto> getAllCompanies() {
        return crud.getAllCompanies();
    }

    @Override
    public CompanyDto updateCompany(Long id, CompanyDto companyDto) throws DAOException {
        return crud.updateCompany(id, companyDto);
    }

    @Override
    public void deleteCompany(Long id) throws DAOException {
        crud.deleteCompany(id);
    }

    @Override
    public void addEmployeeToCompany(Long employeeId, Long companyId) throws DAOException {
        crud.addEmployeeToCompany(employeeId, companyId);
    }

    @Override
    public void removeEmployeeFromCompany(Long employeeId, Long companyId) throws DAOException {
        crud.removeEmployeeFromCompany(employeeId, companyId);
    }

    @Override
    public Set<Long> getAllEmployeeIdsForCompany(Long companyId) throws DAOException {
        return crud.getAllEmployeeIdsForCompany(companyId);
    }

    @Override
    public void addVehicleToCompany(Long companyId, Long vehicleId) throws DAOException {
        crud.addVehicleToCompany(companyId, vehicleId);
    }

    @Override
    public void removeVehicleFromCompany(Long companyId, Long vehicleId) throws DAOException {
        crud.removeVehicleFromCompany(companyId, vehicleId);
    }

    @Override
    public Set<Long> getAllVehicleIdsForCompany(Long companyId) throws DAOException {
        return crud.getAllVehicleIdsForCompany(companyId);
    }

    @Override
    public Set<Long> getAllTransportIdsForCompany(Long companyId) throws DAOException {
        return crud.getAllTransportIdsForCompany(companyId);
    }

    @Override
    public List<Company> filterCompaniesWithRevenueOver(BigDecimal threshold) {
        return filter.filterCompaniesWithRevenueOver(threshold);
    }

    @Override
    public List<Company> filterCompaniesWhichNameIncludes(String matchString) {
        return filter.filterCompaniesWhichNameIncludes(matchString);
    }

    @Override
    public List<Company> sortCompaniesByNameAscending(boolean isAscending) {
        return sorting.sortCompaniesByNameAscending(true);
    }

    @Override
    public List<Company> sortCompaniesByRevenueAscending(boolean isAscending) {
        return sorting.sortCompaniesByRevenueAscending(true);
    }

    @Override
    public BigDecimal getCompanyTotalRevenue(Long companyId) throws DAOException {
        return report.getCompanyTotalRevenue(companyId);
    }

    @Override
    public int getCompanyTransportsCount(Long companyId) throws DAOException {
        return report.getCompanyTransportsCount(companyId);
    }

    @Override
    public BigDecimal getCompanyAverageTransportRevenue(Long companyId) throws DAOException {
        return report.getCompanyAverageTransportRevenue(companyId);
    }
}
