package org.university.service.impl.employee_service_impl;

import org.university.dto.EmployeeDto;
import org.university.entity.Employee;
import org.university.exception.DAOException;
import org.university.service.contract.employee_service.*;
import org.university.util.DriverQualifications;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class EmployeeGeneralServiceImpl implements EmployeeGeneralService {
    private final EmployeeCRUDService crud;
    private final EmployeeFilterService filter;
    private final EmployeeSortingService sorting;
    private final EmployeeReportService report;

    public EmployeeGeneralServiceImpl(EmployeeCRUDService crud, EmployeeFilterService filter, EmployeeSortingService sorting, EmployeeReportService report) {
        this.crud = crud;
        this.filter = filter;
        this.sorting = sorting;
        this.report = report;
    }

    @Override
    public Employee mapToEntity(EmployeeDto employeeDto) {
        return crud.mapToEntity(employeeDto);
    }

    @Override
    public EmployeeDto mapToDto(Employee employee) {
        return mapToDto(employee);
    }

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) throws DAOException {
        return crud.createEmployee(employeeDto);
    }

    @Override
    public EmployeeDto getEmployeeById(Long id) throws DAOException {
        return crud.getEmployeeById(id);
    }

    @Override
    public Set<EmployeeDto> getAllEmployees() {
        return crud.getAllEmployees();
    }

    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) throws DAOException {
        return crud.updateEmployee(id, employeeDto);
    }

    @Override
    public void deleteEmployee(Long id) throws DAOException {
        crud.deleteEmployee(id);
    }

    @Override
    public Long getCompanyIdForEmployee(Long employeeId) throws DAOException {
        return crud.getCompanyIdForEmployee(employeeId);
    }

    @Override
    public Long getDrivingLicenseIdForEmployee(Long employeeId) throws DAOException {
        return crud.getDrivingLicenseIdForEmployee(employeeId);
    }

    @Override
    public Set<Long> getAllVehicleIdsForEmployee(Long employeeId) throws DAOException {
        return crud.getAllVehicleIdsForEmployee(employeeId);
    }

    @Override
    public Set<Long> getAllTransportIdsForEmployee(Long employeeId) throws DAOException {
        return crud.getAllTransportIdsForEmployee(employeeId);
    }

    @Override
    public List<Employee> filterEmployeeByQualification(DriverQualifications qualification) {
        return filter.filterEmployeeByQualification(qualification);
    }

    @Override
    public List<Employee> filterEmployeesBySalary(BigDecimal salary) {
        return filter.filterEmployeesBySalary(salary);
    }

    @Override
    public List<Employee> sortEmployeesBySalaryAscending() {
        return sorting.sortEmployeesBySalaryAscending();
    }

    @Override
    public List<Employee> sortEmployeesBySalaryDescending() {
        return sorting.sortEmployeesBySalaryDescending();
    }

    @Override
    public List<Employee> sortEmployeesByQualificationAscending() {
        return sorting.sortEmployeesByQualificationAscending();
    }

    @Override
    public List<Employee> sortEmployeesByQualificationDescending() {
        return sorting.sortEmployeesByQualificationDescending();
    }

    @Override
    public BigDecimal getAverageTransportRevenuePerEmployee(Long employeeId) {
        return report.getAverageTransportRevenuePerEmployee(employeeId);
    }
}
