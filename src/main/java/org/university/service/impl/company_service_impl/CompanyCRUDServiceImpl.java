package org.university.service.impl.company_service_impl;

import org.university.dao.*;
import org.university.dto.CompanyDto;
import org.university.entity.Company;
import org.university.entity.Employee;
import org.university.entity.Transport;
import org.university.entity.Vehicle;
import org.university.exception.DAOException;
import org.university.service.contract.company_service.CompanyCRUDService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CompanyCRUDServiceImpl implements CompanyCRUDService {
    private final CompanyDao companyDao;
    private final EmployeeDao employeeDao;
    private final VehicleDao vehicleDao;
    private final TransportDao transportDao;

    public CompanyCRUDServiceImpl(CompanyDao companyDao, EmployeeDao employeeDao, VehicleDao vehicleDao, TransportDao transportDao) {
        this.companyDao = companyDao;
        this.employeeDao = employeeDao;
        this.vehicleDao = vehicleDao;
        this.transportDao = transportDao;
    }


    @Override
    public Company mapToEntity(CompanyDto companyDto) {
        Company company = new Company();
        company.setName(companyDto.getName());
        company.setRevenue(companyDto.getRevenue());
        return company;
    }

    @Override
    public CompanyDto mapToDto(Company company) {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setId(company.getId());
        companyDto.setName(company.getName());
        companyDto.setRevenue(company.getRevenue());
        return companyDto;
    }

    @Override
    public CompanyDto createCompany(CompanyDto companyDto) throws DAOException {
        Company company = mapToEntity(companyDto);
        List<Company> companies = companyDao.getAllCompanies();
        if(companies.stream().anyMatch(c -> c.getName().equals(companyDto.getName()))){
            throw new DAOException("Company with name " + companyDto.getName() + " already exists");
        }
        companyDao.createCompany(company);
        return mapToDto(company);
    }

    @Override
    public CompanyDto getCompanyById(Long id) {
        Company company = companyDao.getCompanyById(id);
        if(company != null) {
            return mapToDto(company);
        }else{
            throw new DAOException("Company with id " + id + " does not exist");
        }
    }

    @Override
    public Set<CompanyDto> getAllCompanies() {
        return companyDao.getAllCompanies()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toSet());
    }

    @Override
    public CompanyDto updateCompany(Long id, CompanyDto companyDto) throws DAOException {
        Company company = companyDao.getCompanyById(id);
        if(company == null) {
            throw new DAOException("Company with id " + id + " does not exist");
        }

        company.setName(companyDto.getName());
        company.setRevenue(companyDto.getRevenue());

        companyDao.updateCompany(id, company);
        return mapToDto(company);
    }

    @Override
    public void deleteCompany(Long id) throws DAOException {
        Company company = companyDao.getCompanyById(id);
        if(company == null) {
            throw new DAOException("Company with id " + id + " does not exist");
        }
        companyDao.deleteCompany(id);
    }

    @Override
    public void addEmployeeToCompany(Long employeeId, Long companyId) throws DAOException {
        Company company = companyDao.getCompanyById(companyId);
        if(company == null) {
            throw new DAOException("Company with id " + companyId + " does not exist");
        }

        Employee employee = employeeDao.getEmployeeById(employeeId);
        if(employee == null){
            throw new DAOException("Employee with id " + employeeId + " does not exist");
        }

        if(employee.getCompany() != null && employee.getCompany().getId() == companyId){
            throw new DAOException("Employee with id " + employeeId + " is already assigned to company with id " + companyId);
        }

        if(employee.getCompany() != null &&
                employee.getCompany().getId() != companyId){
            throw new DAOException("Employee with id " + employeeId + " is already assigned to another company");
        }

        employee.setCompany(company);
        company.getEmployeeSet().add(employee);

        employeeDao.updateEmployee(employee.getId(), employee);
    }

    @Override
    public void removeEmployeeFromCompany(Long employeeId, Long companyId) throws DAOException {
        Company company = companyDao.getCompanyById(companyId);
        if(company == null) {
            throw new DAOException("Company with id " + companyId + " does not exist");
        }

        Employee employee = employeeDao.getEmployeeById(employeeId);
        if(employee == null){
            throw new DAOException("Employee with id " + employeeId + " does not exist");
        }

        if(employee.getCompany() == null || !employee.getCompany().getId().equals(companyId)){
            throw new DAOException("Employee with id " + employeeId + " is not assigned to company with id " + companyId);
        }

        company.getEmployeeSet().remove(employee);
        employee.setCompany(null);

        employeeDao.updateEmployee(employee.getId(), employee);
    }

    @Override
    public Set<Long> getAllEmployeeIdsForCompany(Long companyId) throws DAOException {
        Company company = companyDao.getCompanyById(companyId);
        if(company == null) {
            throw new DAOException("Company with id " + companyId + " does not exist");
        }
        return employeeDao.getAllEmployees()
                .stream()
                .filter(employee -> employee.getCompany() != null
                        && employee.getCompany().getId().equals(companyId))
                .map(Employee::getId)
                .collect(Collectors.toSet());
    }

    @Override
    public void addVehicleToCompany(Long companyId, Long vehicleId) throws DAOException {
        Company company = companyDao.getCompanyById(companyId);
        if(company == null) {
            throw new DAOException("Company with id " + companyId + " does not exist");
        }

        Vehicle vehicle = vehicleDao.getVehicleById(vehicleId);
        if(vehicle == null){
            throw new DAOException("Vehicle with id " + vehicleId + " does not exist");
        }

        if(vehicle.getCompany() != null && vehicle.getCompany().getId().equals(companyId)){
            throw new DAOException("Vehicle with id " + vehicleId + " is already assigned to company with id " + companyId);
        }

        if(vehicle.getCompany() != null &&
                !vehicle.getCompany().getId().equals(companyId) ){
            throw new DAOException("Vehicle with id " + vehicleId + " is already assigned to another company");
        }

        company.getVehicleSet().add(vehicle);
        vehicle.setCompany(company);

        vehicleDao.updateVehicle(vehicle.getId(), vehicle);
    }

    @Override
    public void removeVehicleFromCompany(Long companyId, Long vehicleId) throws DAOException {
        Vehicle vehicle = vehicleDao.getVehicleById(vehicleId);
        if(vehicle == null){
            throw new DAOException("Vehicle with id " + vehicleId + " does not exist");
        }

        Company company = companyDao.getCompanyById(companyId);
        if(company == null) {
            throw new DAOException("Company with id " + companyId + " does not exist");
        }

        if(vehicle.getCompany() == null || vehicle.getCompany().getId() != companyId){
            throw new DAOException("Vehicle with id " + vehicleId + " is not assigned to company with id " + companyId);
        }

        company.getVehicleSet().remove(vehicle);
        vehicle.setCompany(null);

       vehicleDao.updateVehicle(vehicle.getId(), vehicle);
    }

    @Override
    public Set<Long> getAllVehicleIdsForCompany(Long companyId) throws DAOException {
        Company company = companyDao.getCompanyById(companyId);
        if(company == null) {
            throw new DAOException("Company with id " + companyId + " does not exist");
        }

        return vehicleDao.getAllVehicles().
                stream()
                .filter(vehicle -> vehicle.getCompany() != null
                        && vehicle.getCompany().getId().equals(companyId))
                .map(Vehicle::getId)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Long> getAllTransportIdsForCompany(Long companyId) throws DAOException {
        Company company = companyDao.getCompanyById(companyId);
        if(company == null){
            throw new DAOException("Company with id " + companyId + " does not exist");
        }

        return transportDao.getAllTransports()
                .stream()
                .filter(transport -> transport.getCompany() != null
                        && transport.getCompany().getId().equals(companyId))
                .map(Transport::getId)
                .collect(Collectors.toSet());
    }
}
