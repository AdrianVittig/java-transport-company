package org.university.service.impl.driving_license_service_impl;

import org.university.dao.DrivingLicenseDao;
import org.university.dao.EmployeeDao;
import org.university.dto.DrivingLicenseDto;
import org.university.entity.DrivingLicense;
import org.university.entity.Employee;
import org.university.exception.DAOException;
import org.university.service.contract.driving_license_service.DrivingLicenseService;

import java.util.Set;
import java.util.stream.Collectors;

public class DrivingLicenseServiceImpl implements DrivingLicenseService {
    private final DrivingLicenseDao drivingLicenseDao;
    private final EmployeeDao employeeDao;

    public DrivingLicenseServiceImpl(DrivingLicenseDao drivingLicenseDao, EmployeeDao employeeDao) {
        this.drivingLicenseDao = drivingLicenseDao;
        this.employeeDao = employeeDao;
    }

    @Override
    public DrivingLicense mapToEntity(DrivingLicenseDto drivingLicenseDto) {
        DrivingLicense drivingLicense = new DrivingLicense();
        drivingLicense.setDrivingLicenseNumber(drivingLicenseDto.getDrivingLicenseNumber());
        drivingLicense.setIssueDate(drivingLicenseDto.getIssueDate());
        drivingLicense.setExpiryDate(drivingLicenseDto.getExpiryDate());
        drivingLicense.setDrivingLicenseCategories(drivingLicenseDto.getDrivingLicenseCategories());

        return drivingLicense;
    }

    @Override
    public DrivingLicenseDto mapToDto(DrivingLicense drivingLicense) {
        DrivingLicenseDto drivingLicenseDto = new DrivingLicenseDto();
        drivingLicenseDto.setId(drivingLicense.getId());
        drivingLicenseDto.setDrivingLicenseNumber(drivingLicense.getDrivingLicenseNumber());
        drivingLicenseDto.setIssueDate(drivingLicense.getIssueDate());
        drivingLicenseDto.setExpiryDate(drivingLicense.getExpiryDate());
        drivingLicenseDto.setDrivingLicenseCategories(drivingLicense.getDrivingLicenseCategories());

        if(drivingLicense.getEmployee() != null){
            drivingLicenseDto.setEmployeeId(drivingLicense.getEmployee().getId());
        }

        return drivingLicenseDto;
    }

    @Override
    public DrivingLicenseDto createDrivingLicense(DrivingLicenseDto drivingLicenseDto) throws DAOException {
        DrivingLicense drivingLicense = mapToEntity(drivingLicenseDto);
        drivingLicenseDao.createDrivingLicense(drivingLicense);
        return mapToDto(drivingLicense);
    }

    @Override
    public DrivingLicenseDto getDrivingLicenseById(Long id) throws DAOException {
        DrivingLicense drivingLicense = drivingLicenseDao.getDrivingLicenseById(id);
        if(drivingLicense == null) {
            throw new DAOException("Driving License with id " + id + " does not exist");
        }
        return mapToDto(drivingLicense);
    }

    @Override
    public Set<DrivingLicenseDto> getAllDrivingLicenses() {
        return drivingLicenseDao.getAllDrivingLicenses()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toSet());
    }

    @Override
    public DrivingLicenseDto updateDrivingLicense(Long id, DrivingLicenseDto drivingLicenseDto) throws DAOException {
        DrivingLicense drivingLicense = drivingLicenseDao.getDrivingLicenseById(id);
        if(drivingLicense == null){
            throw new DAOException("Driving License with id " + id + " does not exist");
        }

        drivingLicense.setDrivingLicenseNumber(drivingLicenseDto.getDrivingLicenseNumber());
        drivingLicense.setIssueDate(drivingLicenseDto.getIssueDate());
        drivingLicense.setExpiryDate(drivingLicenseDto.getExpiryDate());
        drivingLicense.setDrivingLicenseCategories(drivingLicenseDto.getDrivingLicenseCategories());

        drivingLicenseDao.updateDrivingLicense(id, drivingLicense);
        return mapToDto(drivingLicense);
    }

    @Override
    public void deleteDrivingLicense(Long id) throws DAOException {
        DrivingLicense drivingLicense = drivingLicenseDao.getDrivingLicenseById(id);
        if(drivingLicense == null){
            throw new DAOException("Driving License with id " + id + " does not exist");
        }
        drivingLicenseDao.deleteDrivingLicense(id);
    }

    @Override
    public void assignDrivingLicenseToEmployee(Long licenseId, Long employeeId) throws DAOException {
        DrivingLicense drivingLicense = drivingLicenseDao.getDrivingLicenseById(licenseId);
        if(drivingLicense == null){
            throw new DAOException("Driving License with id " + licenseId + " does not exist");
        }

        Employee employee = employeeDao.getEmployeeById(employeeId);
        if(employee == null){
            throw new DAOException("Employee with id " + employeeId + " does not exist");
        }

        if(drivingLicense.getEmployee() != null
                && !drivingLicense.getEmployee().getId().equals(employeeId)){
            throw new DAOException("Driving License with id " + licenseId + " is already assigned to another employee with id "
            + drivingLicense.getEmployee().getId());
        }

        if(employee.getDrivingLicense() != null
                && !employee.getDrivingLicense().getId().equals(licenseId)){
            DrivingLicense oldLicense = employee.getDrivingLicense();
            oldLicense.setEmployee(null);
        }

        drivingLicense.setEmployee(employee);
        employee.setDrivingLicense(drivingLicense);

        employeeDao.updateEmployee(employeeId, employee);
    }

    @Override
    public void removeDrivingLicenseFromEmployee(Long licenseId, Long employeeId) throws DAOException {
        DrivingLicense drivingLicense = drivingLicenseDao.getDrivingLicenseById(licenseId);
        if(drivingLicense == null){
            throw new DAOException("Driving License with id " + licenseId + " does not exist");
        }

        Employee employee = employeeDao.getEmployeeById(employeeId);
        if(employee == null){
            throw new DAOException("Employee with id " + employeeId + " does not exist");
        }

        if(employee.getDrivingLicense() == null
                || !employee.getDrivingLicense().getId().equals(licenseId)){
            throw new DAOException("Employee with id " + employeeId +
                    " is not assigned driving license with id " + licenseId);
        }

        drivingLicense.setEmployee(null);
        employee.setDrivingLicense(null);

        employeeDao.updateEmployee(employeeId, employee);
    }

    @Override
    public Long getEmployeeIdForDrivingLicense(Long licenseId) throws DAOException {
        DrivingLicense drivingLicense = drivingLicenseDao.getDrivingLicenseById(licenseId);
        if(drivingLicense == null){
            throw new DAOException("DrivingLicense with id " + licenseId + " does not exist");
        }

        Employee employee = drivingLicense.getEmployee();
        if(employee == null){
            throw new DAOException("DrivingLicense with id " + licenseId + " is not assigned to any employee");
        }

        return employee.getId();
    }
}
