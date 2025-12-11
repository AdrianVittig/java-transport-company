package org.university.service.contract.employee_service;

import org.university.dto.EmployeeDto;
import org.university.entity.Employee;
import org.university.exception.DAOException;

import java.util.Set;

public interface EmployeeCRUDService {
    Employee mapToEntity(EmployeeDto employeeDto);

    EmployeeDto mapToDto(Employee employee);

    EmployeeDto createEmployee(EmployeeDto employeeDto) throws DAOException;

    EmployeeDto getEmployeeById(Long id) throws DAOException;

    Set<EmployeeDto> getAllEmployees();

    EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) throws DAOException;

    void deleteEmployee(Long id) throws DAOException;

    Long getCompanyIdForEmployee(Long employeeId) throws DAOException;

    Long getDrivingLicenseIdForEmployee(Long employeeId) throws DAOException;

    Set<Long> getAllVehicleIdsForEmployee(Long employeeId) throws DAOException;

    Set<Long> getAllTransportIdsForEmployee(Long employeeId) throws DAOException;
}
