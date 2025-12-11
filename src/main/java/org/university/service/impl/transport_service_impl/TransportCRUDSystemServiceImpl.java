package org.university.service.impl.transport_service_impl;

import org.university.dao.*;
import org.university.dto.TransportDto;
import org.university.entity.*;
import org.university.exception.DAOException;
import org.university.service.contract.transport_service.TransportCRUDSystemService;

import java.util.Set;
import java.util.stream.Collectors;

public class TransportCRUDSystemServiceImpl implements TransportCRUDSystemService {
    private final TransportDao transportDao;
    private final VehicleDao vehicleDao;
    private final CompanyDao companyDao;
    private final EmployeeDao employeeDao;
    private final CustomerDao customerDao;

    public TransportCRUDSystemServiceImpl(TransportDao transportDao, VehicleDao vehicleDao, CompanyDao companyDao, EmployeeDao employeeDao, CustomerDao customerDao) {
        this.transportDao = transportDao;
        this.vehicleDao = vehicleDao;
        this.companyDao = companyDao;
        this.employeeDao = employeeDao;
        this.customerDao = customerDao;
    }

    @Override
    public Transport mapToEntity(TransportDto transportDto) {
        Transport transport = new Transport();
        transport.setStartPoint(transportDto.getStartPoint());
        transport.setEndPoint(transportDto.getEndPoint());
        transport.setDepartureDate(transportDto.getDepartureDate());
        transport.setArrivalDate(transportDto.getArrivalDate());
        transport.setCargoType(transportDto.getCargoType());
        transport.setQuantity(transportDto.getQuantity());
        transport.setInitPrice(transportDto.getInitPrice());
        transport.setPaymentStatus(transportDto.getPaymentStatus());
        transport.setTotalPrice(transportDto.getTotalPrice());

        if(transportDto.getCompanyId() != null){
            Company company = companyDao.getCompanyById(transportDto.getCompanyId());
            if(company == null){
                throw new DAOException("Company with id " + transportDto.getCompanyId() + " does not exist");
            }
            transport.setCompany(company);
        }
        if(transportDto.getVehicleId() != null){
            Vehicle vehicle = vehicleDao.getVehicleById(transportDto.getVehicleId());
            if(vehicle == null){
                throw new DAOException("Vehicle with id " + transportDto.getVehicleId() + " does not exist");
            }
            transport.setVehicle(vehicle);
        }
        if(transportDto.getEmployeeId() != null){
            Employee employee = employeeDao.getEmployeeById(transportDto.getEmployeeId());
            if(employee == null){
                throw new DAOException("Employee with id " + transportDto.getEmployeeId() + " does not exist");
            }
            transport.setEmployee(employee);
        }
        if(transportDto.getCustomerId() != null){
            Customer customer = customerDao.getCustomerById(transportDto.getCustomerId());
            if(customer == null){
                throw new DAOException("Customer with id " + transportDto.getCustomerId() + " does not exist");
            }
            transport.setCustomer(customer);
        }
        return transport;
    }

    @Override
    public TransportDto mapToDto(Transport transport) {
        TransportDto transportDto = new TransportDto();
        transportDto.setId(transport.getId());
        transportDto.setStartPoint(transport.getStartPoint());
        transportDto.setEndPoint(transport.getEndPoint());
        transportDto.setDepartureDate(transport.getDepartureDate());
        transportDto.setArrivalDate(transport.getArrivalDate());
        transportDto.setCargoType(transport.getCargoType());
        transportDto.setQuantity(transport.getQuantity());
        transportDto.setInitPrice(transport.getInitPrice());
        transportDto.setPaymentStatus(transport.getPaymentStatus());
        transportDto.setTotalPrice(transport.getTotalPrice());

        if(transport.getVehicle() != null){
            transportDto.setVehicleId(transport.getVehicle().getId());
        }
        if(transport.getCustomer() != null){
            transportDto.setCustomerId(transport.getCustomer().getId());
        }
        if(transport.getCompany() != null){
            transportDto.setCompanyId(transport.getCompany().getId());
        }
        if(transport.getEmployee() != null){
            transportDto.setEmployeeId(transport.getEmployee().getId());
        }

        return transportDto;
    }

    @Override
    public TransportDto createTransport(TransportDto transportDto) throws DAOException {
        Transport transport = mapToEntity(transportDto);
        transportDao.createTransport(transport);
        return mapToDto(transport);
    }

    @Override
    public TransportDto getTransportById(Long id) throws DAOException {
        Transport transport = transportDao.getTransportById(id);
        if(transport == null) {
            throw new DAOException("Transport with id " + id + " does not exist");
        }
        return mapToDto(transport);
    }

    @Override
    public Set<TransportDto> getAllTransports() {
        return transportDao.getAllTransports()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toSet());
    }

    @Override
    public TransportDto updateTransport(Long id, TransportDto transportDto) throws DAOException {
        Transport transport = transportDao.getTransportById(id);
        if(transport == null) {
            throw new DAOException("Transport with id " + id + " does not exist");
        }
        transport.setStartPoint(transportDto.getStartPoint());
        transport.setEndPoint(transportDto.getEndPoint());
        transport.setDepartureDate(transportDto.getDepartureDate());
        transport.setArrivalDate(transportDto.getArrivalDate());
        transport.setCargoType(transportDto.getCargoType());
        transport.setQuantity(transportDto.getQuantity());
        transport.setInitPrice(transportDto.getInitPrice());
        transport.setPaymentStatus(transportDto.getPaymentStatus());
        transport.setTotalPrice(transportDto.getTotalPrice());

        if(transportDto.getCompanyId() != null){
            Company company = companyDao.getCompanyById(transportDto.getCompanyId());
            if(company == null){
                throw new DAOException("Company with id " + transportDto.getCompanyId() + " does not exist");
            }
            transport.setCompany(company);
        }
        else{
            transport.setCompany(null);
        }
        if(transportDto.getVehicleId() != null){
            Vehicle vehicle = vehicleDao.getVehicleById(transportDto.getVehicleId());
            if(vehicle == null){
                throw new DAOException("Vehicle with id " + transportDto.getVehicleId() + " does not exist");
            }
            transport.setVehicle(vehicle);
        }
        else{
            transport.setVehicle(null);
        }
        if(transportDto.getEmployeeId() != null){
            Employee employee = employeeDao.getEmployeeById(transportDto.getEmployeeId());
            if(employee == null){
                throw new DAOException("Employee with id " + transportDto.getEmployeeId() + " does not exist");
            }
            transport.setEmployee(employee);
        }
        else{
            transport.setEmployee(null);
        }
        if(transportDto.getCustomerId() != null){
            Customer customer = customerDao.getCustomerById(transportDto.getCustomerId());
            if(customer == null){
                throw new DAOException("Customer with id " + transportDto.getCustomerId() + " does not exist");
            }
            transport.setCustomer(customer);
        }else{
            transport.setCustomer(null);
        }

        transportDao.updateTransport(id, transport);
        return mapToDto(transport);
    }

    @Override
    public void deleteTransport(Long id) throws DAOException {
        Transport transport = transportDao.getTransportById(id);
        if(transport == null) {
            throw new DAOException("Transport with id " + id + " does not exist");
        }
        transportDao.deleteTransport(id);
    }

    @Override
    public Long getCompanyIdForTransport(Long transportId) throws DAOException {
        Transport transport = transportDao.getTransportById(transportId);
        if(transport == null){
            throw new DAOException("Transport with id " + transportId + " does not exist");
        }

        Company company = transport.getCompany();
        if(company == null){
            throw new DAOException("Transport with id " + transportId + " does not belong to any company");
        }
        return transport.getCompany().getId();
    }

    @Override
    public Long getEmployeeIdForTransport(Long transportId) throws DAOException {
        Transport transport = transportDao.getTransportById(transportId);
        if(transport == null){
            throw new DAOException("Transport with id " + transportId + " does not exist");
        }

        Employee employee = transport.getEmployee();
        if(employee == null){
            throw new DAOException("Transport with id " + transportId + " does not belong to any employee");
        }
        return transport.getEmployee().getId();
    }

    @Override
    public Long getCustomerIdForTransport(Long transportId) throws DAOException {
        Transport transport = transportDao.getTransportById(transportId);
        if(transport == null){
            throw new DAOException("Transport with id " + transportId + " does not exist");
        }

        Customer customer = transport.getCustomer();
        if(customer == null){
            throw new DAOException("Transport with id " + transportId + " does not belong to any customer");
        }
        return transport.getCustomer().getId();
    }

    @Override
    public Long getVehicleIdForTransport(Long transportId) throws DAOException {
        Transport transport = transportDao.getTransportById(transportId);
        if(transport == null){
            throw new DAOException("Transport with id " + transportId + " does not exist");
        }

        Vehicle vehicle = transport.getVehicle();
        if(vehicle == null){
            throw new DAOException("Transport with id " + transportId + " does not belong to any vehicle");
        }
        return transport.getVehicle().getId();
    }
}
