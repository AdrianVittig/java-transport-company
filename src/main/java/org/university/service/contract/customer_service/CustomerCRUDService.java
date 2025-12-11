package org.university.service.contract.customer_service;

import org.university.dto.CustomerDto;
import org.university.entity.Customer;
import org.university.exception.DAOException;

import java.util.Set;

public interface CustomerCRUDService {
    Customer mapToEntity(CustomerDto customerDto);
    CustomerDto mapToDto(Customer customer);

    CustomerDto createCustomer(CustomerDto customerDto) throws DAOException;

    CustomerDto getCustomerById(Long id) throws DAOException;

    Set<CustomerDto> getAllCustomers();

    CustomerDto updateCustomer(Long id, CustomerDto customerDto) throws DAOException;

    void deleteCustomer(Long id) throws DAOException;

    void addTransportToCustomer(Long customerId, Long transportId) throws DAOException;

    void removeTransportFromCustomer(Long customerId, Long transportId) throws DAOException;

    Set<Long> getAllTransportIdsForCustomer(Long customerId) throws DAOException;
}
