package org.university.service.impl.customer_service_impl;

import org.university.dao.CustomerDao;
import org.university.dao.TransportDao;
import org.university.dto.CustomerDto;
import org.university.entity.Customer;
import org.university.entity.Transport;
import org.university.exception.DAOException;
import org.university.service.contract.customer_service.CustomerCRUDService;

import java.util.Set;
import java.util.stream.Collectors;

public class CustomerCRUDServiceImpl implements CustomerCRUDService {
    private final CustomerDao customerDao;
    private final TransportDao transportDao;

    public CustomerCRUDServiceImpl(CustomerDao customerDao, TransportDao transportDao) {
        this.customerDao = customerDao;
        this.transportDao = transportDao;
    }

    @Override
    public Customer mapToEntity(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setBirthDate(customerDto.getBirthDate());
        customer.setBudget(customerDto.getBudget());
        return customer;
    }

    @Override
    public CustomerDto mapToDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customer.getId());
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setBirthDate(customer.getBirthDate());
        customerDto.setBudget(customer.getBudget());
        return customerDto;
    }

    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) throws DAOException {
        Customer customer = mapToEntity(customerDto);
        customerDao.createCustomer(customer);
        return mapToDto(customer);
    }

    @Override
    public CustomerDto getCustomerById(Long id) throws DAOException {
        Customer customer = customerDao.getCustomerById(id);
        if(customer == null) {
            throw new DAOException("Customer with id " + id + " does not exist");
        }
        return mapToDto(customer);
    }

    @Override
    public Set<CustomerDto> getAllCustomers() {
        return customerDao.getAllCustomers()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toSet());
    }

    @Override
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) throws DAOException {
        Customer customer = customerDao.getCustomerById(id);
        if(customer == null) {
            throw new DAOException("Customer with id " + id + " does not exist");
        }
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setBirthDate(customerDto.getBirthDate());
        customer.setBudget(customerDto.getBudget());

        customerDao.updateCustomer(id, customer);
        return mapToDto(customer);
    }

    @Override
    public void deleteCustomer(Long id) throws DAOException {
        Customer customer = customerDao.getCustomerById(id);
        if(customer == null) {
            throw new DAOException("Customer with id " + id + " does not exist");
        }
        customerDao.deleteCustomer(id);
    }

    @Override
    public void addTransportToCustomer(Long customerId, Long transportId) throws DAOException {
        Customer customer = customerDao.getCustomerById(customerId);
        if(customer == null) {
            throw new DAOException("Customer with id " + customerId + " does not exist");
        }

        Transport transport = transportDao.getTransportById(transportId);
        if(transport == null){
            throw new DAOException("Transport with id " + transportId + " does not exist");
        }

        if(transport.getCustomer() != null &&
        !transport.getCustomer().getId().equals(customerId)){
            throw new DAOException("Transport with id " + transportId + " is already assigned to another customer");
        }

        customer.getTransportSet().add(transport);
        transport.setCustomer(customer);

        transportDao.updateTransport(transportId, transport);
    }

    @Override
    public void removeTransportFromCustomer(Long customerId, Long transportId) throws DAOException {
        Customer customer = customerDao.getCustomerById(customerId);
        if(customer == null) {
            throw new DAOException("Customer with id " + customerId + " does not exist");
        }

        Transport transport = transportDao.getTransportById(transportId);
        if(transport == null){
            throw new DAOException("Transport with id " + transportId + " does not exist");
        }

        if(transport.getCustomer() == null
                || !transport.getCustomer().getId().equals(customerId)){
            throw new DAOException("Transport with id " + transportId + " is not assigned to customer with id " + customerId);
        }

        customer.getTransportSet().remove(transport);
        transport.setCustomer(null);

        transportDao.updateTransport(transportId, transport);
    }

    @Override
    public Set<Long> getAllTransportIdsForCustomer(Long customerId) throws DAOException {
        return transportDao.getAllTransports()
                .stream()
                .filter(transport -> transport.getCustomer() != null
                        &&transport.getCustomer().getId().equals(customerId))
                .map(Transport::getId)
                .collect(Collectors.toSet());
    }
}
