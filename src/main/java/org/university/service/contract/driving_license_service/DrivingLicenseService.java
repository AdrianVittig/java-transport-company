package org.university.service.contract.driving_license_service;

import org.university.dto.DrivingLicenseDto;
import org.university.entity.DrivingLicense;
import org.university.exception.DAOException;

import java.util.Set;

public interface DrivingLicenseService {
    DrivingLicense mapToEntity(DrivingLicenseDto drivingLicenseDto);

    DrivingLicenseDto mapToDto(DrivingLicense drivingLicense);

    DrivingLicenseDto createDrivingLicense(DrivingLicenseDto drivingLicenseDto) throws DAOException;

    DrivingLicenseDto getDrivingLicenseById(Long id) throws DAOException;

    Set<DrivingLicenseDto> getAllDrivingLicenses();

    DrivingLicenseDto updateDrivingLicense(Long id, DrivingLicenseDto drivingLicenseDto) throws DAOException;

    void deleteDrivingLicense(Long id) throws DAOException;

    void assignDrivingLicenseToEmployee(Long licenseId, Long employeeId) throws DAOException;

    void removeDrivingLicenseFromEmployee(Long licenseId, Long employeeId) throws DAOException;

    Long getEmployeeIdForDrivingLicense(Long licenseId) throws DAOException;

}
