package org.university.service.contract.company_service;

import org.university.dto.CompanyDto;
import org.university.entity.Company;
import org.university.exception.DAOException;

import java.util.Set;

public interface CompanyCRUDService {
    Company mapToEntity(CompanyDto companyDto);

    CompanyDto mapToDto(Company company);

    CompanyDto createCompany(CompanyDto companyDto) throws DAOException;

    CompanyDto getCompanyById(Long id);

    Set<CompanyDto> getAllCompanies();

    CompanyDto updateCompany(Long id, CompanyDto companyDto) throws DAOException;

    void deleteCompany(Long id) throws DAOException;

    void addEmployeeToCompany(Long employeeId, Long companyId) throws DAOException;

    void removeEmployeeFromCompany(Long employeeId, Long companyId) throws DAOException;

    Set<Long> getAllEmployeeIdsForCompany(Long companyId) throws DAOException;

    void addVehicleToCompany(Long companyId, Long vehicleId) throws DAOException;

    void removeVehicleFromCompany(Long companyId, Long vehicleId) throws DAOException;

    Set<Long> getAllVehicleIdsForCompany(Long companyId) throws DAOException;

    Set<Long> getAllTransportIdsForCompany(Long companyId) throws DAOException;
}
