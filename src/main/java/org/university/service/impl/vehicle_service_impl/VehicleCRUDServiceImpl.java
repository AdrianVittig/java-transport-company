package org.university.service.impl.vehicle_service_impl;

import org.university.dao.CompanyDao;
import org.university.dao.EmployeeDao;
import org.university.dao.TransportDao;
import org.university.dao.VehicleDao;
import org.university.dto.VehicleDto;
import org.university.entity.Company;
import org.university.entity.Employee;
import org.university.entity.Transport;
import org.university.entity.Vehicle;
import org.university.exception.DAOException;
import org.university.service.contract.vehicle_service.VehicleCRUDService;

import java.util.Set;
import java.util.stream.Collectors;

public class VehicleCRUDServiceImpl implements VehicleCRUDService {
    private final VehicleDao vehicleDao;
    private final CompanyDao companyDao;
    private final EmployeeDao employeeDao;

    public VehicleCRUDServiceImpl(VehicleDao vehicleDao, CompanyDao companyDao, EmployeeDao employeeDao, TransportDao transportDao) {
        this.vehicleDao = vehicleDao;
        this.companyDao = companyDao;
        this.employeeDao = employeeDao;
    }

    @Override
    public Vehicle mapToEntity(VehicleDto vehicleDto) {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleType(vehicleDto.getVehicleType());
        vehicle.setDistanceTraveled(vehicleDto.getDistanceTraveled());
        if(vehicleDto.getCompanyId() != null){
            Company company = companyDao.getCompanyById(vehicleDto.getCompanyId());
            if(company == null){
                throw new DAOException("Company with id " + vehicleDto.getCompanyId() + " does not exist");
            }
            vehicle.setCompany(company);
        }

        if(vehicleDto.getEmployeeId() != null){
            Employee employee = employeeDao.getEmployeeById(vehicleDto.getEmployeeId());
            if(employee == null){
                throw new DAOException("Employee with id " + vehicleDto.getEmployeeId() + " does not exist");
            }
            vehicle.setEmployee(employee);
        }

        return vehicle;
    }

    @Override
    public VehicleDto mapToDto(Vehicle vehicle) {
        VehicleDto vehicleDto = new VehicleDto();
        vehicleDto.setId(vehicle.getId());
        vehicleDto.setVehicleType(vehicle.getVehicleType());
        vehicleDto.setDistanceTraveled(vehicle.getDistanceTraveled());

        if(vehicle.getCompany() != null){
            vehicleDto.setCompanyId(vehicle.getCompany().getId());
        }
        if(vehicle.getEmployee() != null){
            vehicleDto.setEmployeeId(vehicle.getEmployee().getId());
        }
        return vehicleDto;
    }

    @Override
    public VehicleDto createVehicle(VehicleDto vehicleDto) throws DAOException {
        Vehicle vehicle = mapToEntity(vehicleDto);
        vehicleDao.createVehicle(vehicle);
        return mapToDto(vehicle);
    }

    @Override
    public VehicleDto getVehicleById(Long id) throws DAOException {
        Vehicle vehicle = vehicleDao.getVehicleById(id);
        if(vehicle == null) {
            throw new DAOException("Vehicle with id " + id + " does not exist");
        }
        return mapToDto(vehicle);
    }

    @Override
    public Set<VehicleDto> getAllVehicles() {
        return vehicleDao.getAllVehicles()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toSet());
    }

    @Override
    public VehicleDto updateVehicle(Long id, VehicleDto vehicleDto) throws DAOException {
        Vehicle vehicle = vehicleDao.getVehicleById(id);
        if(vehicle == null) {
            throw new DAOException("Vehicle with id " + id + " does not exist");
        }
        vehicle.setVehicleType(vehicleDto.getVehicleType());
        vehicle.setDistanceTraveled(vehicleDto.getDistanceTraveled());
        if(vehicleDto.getCompanyId() != null){
            Company company = companyDao.getCompanyById(vehicleDto.getCompanyId());
            if(company == null){
                throw new DAOException("Company with id " + vehicleDto.getCompanyId() + " does not exist");
            }
            vehicle.setCompany(company);
        }else{
            vehicle.setCompany(null);
        }

        if(vehicleDto.getEmployeeId() != null){
            Employee employee = employeeDao.getEmployeeById(vehicleDto.getEmployeeId());
            if(employee == null){
                throw new DAOException("Employee with id " + vehicleDto.getEmployeeId() + " does not exist");
            }
            vehicle.setEmployee(employee);
        }else{
            vehicle.setEmployee(null);
        }

        vehicleDao.updateVehicle(id, vehicle);
        return mapToDto(vehicle);
    }

    @Override
    public void deleteVehicle(Long id) throws DAOException {
        Vehicle vehicle = vehicleDao.getVehicleById(id);
        if(vehicle == null) {
            throw new DAOException("Vehicle with id " + id + " does not exist");
        }
        vehicleDao.deleteVehicle(id);
    }

    @Override
    public Long getCompanyIdForVehicle(Long vehicleId) throws DAOException {
        Vehicle vehicle = vehicleDao.getVehicleById(vehicleId);
        if(vehicle == null){
            throw new DAOException("Vehicle with id " + vehicleId + " does not exist");
        }
        if(vehicle.getCompany() == null){
            throw new DAOException("Vehicle with id " + vehicleId + " is not assigned to any company");
        }
        return vehicle.getCompany().getId();
    }

    @Override
    public Long getEmployeeIdForVehicle(Long vehicleId) throws DAOException {
        Vehicle vehicle = vehicleDao.getVehicleById(vehicleId);
        if(vehicle == null){
            throw new DAOException("Vehicle with id " + vehicleId + " does not exist");
        }

        if(vehicle.getEmployee() == null){
            throw new DAOException("Vehicle with id " + vehicleId + " is not assigned to any employee");
        }
        return vehicle.getEmployee().getId();
    }

    @Override
    public Set<Long> getAllTransportIdsForVehicle(Long vehicleId) throws DAOException {
        Vehicle vehicle = vehicleDao.getVehicleById(vehicleId);
        if(vehicle == null){
            throw new DAOException("Vehicle with id " + vehicleId + " does not exist");
        }

        return vehicle.getTransportSet()
                .stream()
                .map(Transport::getId)
                .collect(Collectors.toSet());
    }
}
