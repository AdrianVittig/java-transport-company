package org.university.service.impl.employee_service_impl;

import org.university.dao.*;
import org.university.dto.EmployeeDto;
import org.university.entity.*;
import org.university.exception.DAOException;
import org.university.service.contract.employee_service.EmployeeCRUDService;

import java.util.Set;
import java.util.stream.Collectors;

public class EmployeeCRUDServiceImpl implements EmployeeCRUDService {
    private final EmployeeDao employeeDao;
    private final DrivingLicenseDao drivingLicenseDao;
    private final VehicleDao vehicleDao;
    private final TransportDao transportDao;
    private final CompanyDao companyDao;

    public EmployeeCRUDServiceImpl(EmployeeDao employeeDao, DrivingLicenseDao drivingLicenseDao, VehicleDao vehicleDao, TransportDao transportDao, CompanyDao companyDao) {
        this.employeeDao = employeeDao;
        this.drivingLicenseDao = drivingLicenseDao;
        this.vehicleDao = vehicleDao;
        this.transportDao = transportDao;
        this.companyDao = companyDao;
    }

    @Override
    public Employee mapToEntity(EmployeeDto employeeDto) {
        Employee employee = new Employee();
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setSalary(employeeDto.getSalary());
        employee.setBirthDate(employeeDto.getBirthDate());
        employee.setDriverQualifications(employeeDto.getDriverQualifications());
        return employee;
    }

    @Override
    public EmployeeDto mapToDto(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(employee.getId());
        employeeDto.setFirstName(employee.getFirstName());
        employeeDto.setLastName(employee.getLastName());
        employeeDto.setSalary(employee.getSalary());
        employeeDto.setBirthDate(employee.getBirthDate());
        employeeDto.setDriverQualifications(employee.getDriverQualifications());

        if(employee.getCompany() != null){
            employeeDto.setCompanyId(employee.getCompany().getId());
        }

        if(employee.getDrivingLicense() != null){
            employeeDto.setDrivingLicenseId(employee.getDrivingLicense().getId());
        }

        if(employee.getVehicleSet() != null){
            employeeDto.setVehicleIds(employee.getVehicleSet()
                    .stream()
                    .map(Vehicle::getId)
                    .collect(Collectors.toSet()));
        }

        if(employee.getTransportSet() != null){
            employeeDto.setTransportIds(
                    employee.getTransportSet().stream()
                            .map(Transport::getId)
                            .collect(Collectors.toSet())
            );
        }

        return employeeDto;
    }

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) throws DAOException {
        Employee employee = mapToEntity(employeeDto);
        employeeDao.createEmployee(employee);
        return mapToDto(employee);
    }

    @Override
    public EmployeeDto getEmployeeById(Long id) throws DAOException {
        Employee employee = employeeDao.getEmployeeById(id);
        if(employee == null) {
            throw new DAOException("Employee with id " + id + " does not exist");
        }
        return mapToDto(employee);
    }

    @Override
    public Set<EmployeeDto> getAllEmployees() {
        return employeeDao.getAllEmployees()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toSet());
    }

    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) throws DAOException {
        Employee employee = employeeDao.getEmployeeById(id);
        if(employee == null) {
            throw new DAOException("Employee with id " + id + " does not exist");
        }
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setSalary(employeeDto.getSalary());
        employee.setBirthDate(employeeDto.getBirthDate());
        employee.setDriverQualifications(employeeDto.getDriverQualifications());
        employeeDao.updateEmployee(id, employee);
        return mapToDto(employee);
    }

    @Override
    public void deleteEmployee(Long id) throws DAOException {
        Employee employee = employeeDao.getEmployeeById(id);
        if(employee == null) {
            throw new DAOException("Employee with id " + id + " does not exist");
        }
        employeeDao.deleteEmployee(id);
    }

    @Override
    public Long getCompanyIdForEmployee(Long employeeId) throws DAOException {
        Employee employee = employeeDao.getEmployeeById(employeeId);
        if(employee == null){
            throw new DAOException("Employee with id " + employeeId + " does not exist");
        }

        Company company = employee.getCompany();
        if(company == null){
            return null;
        }
        return employee.getCompany().getId();
    }

    @Override
    public Long getDrivingLicenseIdForEmployee(Long employeeId) throws DAOException {
        Employee employee = employeeDao.getEmployeeById(employeeId);
        if(employee == null){
            throw new DAOException("Employee with id " + employeeId + " does not exist");
        }

        DrivingLicense drivingLicense = employee.getDrivingLicense();
        if(drivingLicense == null){
            return null;
        }

        return employee.getDrivingLicense().getId();
    }

    @Override
    public Set<Long> getAllVehicleIdsForEmployee(Long employeeId) throws DAOException {
        Employee employee = employeeDao.getEmployeeById(employeeId);
        if(employee == null){
            throw new DAOException("Employee with id " + employeeId + " does not exist");
        }

        return vehicleDao.getAllVehicles()
                .stream()
                .filter(vehicle -> vehicle.getEmployee() != null
                        && vehicle.getEmployee().getId().equals(employeeId))
                .map(Vehicle::getId)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Long> getAllTransportIdsForEmployee(Long employeeId) throws DAOException {
        Employee employee = employeeDao.getEmployeeById(employeeId);
        if(employee == null){
            throw new DAOException("Employee with id " + employeeId + " does not exist");
        }

        return transportDao.getAllTransports()
                .stream()
                .filter(transport -> transport.getEmployee() != null
                        && transport.getEmployee().getId().equals(employeeId))
                .map(Transport::getId)
                .collect(Collectors.toSet());
    }
}
