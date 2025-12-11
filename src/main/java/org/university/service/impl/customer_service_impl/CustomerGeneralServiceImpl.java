package org.university.service.impl.customer_service_impl;

import org.university.dto.CustomerDto;
import org.university.entity.Customer;
import org.university.exception.DAOException;
import org.university.service.contract.customer_service.CustomerCRUDService;
import org.university.service.contract.customer_service.CustomerGeneralService;
import org.university.service.contract.customer_service.CustomerReportService;

import java.math.BigDecimal;
import java.util.Set;

public class CustomerGeneralServiceImpl implements CustomerGeneralService {
    private final CustomerCRUDService crud;
    private final CustomerReportService report;

    public CustomerGeneralServiceImpl(CustomerCRUDService crud, CustomerReportService report) {
        this.crud = crud;
        this.report = report;
    }

    @Override
    public Customer mapToEntity(CustomerDto customerDto) {
        return crud.mapToEntity(customerDto);
    }

    @Override
    public CustomerDto mapToDto(Customer customer) {
        return crud.mapToDto(customer);
    }

    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) throws DAOException {
        return crud.createCustomer(customerDto);
    }

    @Override
    public CustomerDto getCustomerById(Long id) throws DAOException {
        return crud.getCustomerById(id);
    }

    @Override
    public Set<CustomerDto> getAllCustomers() {
        return crud.getAllCustomers();
    }

    @Override
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) throws DAOException {
        return crud.updateCustomer(id, customerDto);
    }

    @Override
    public void deleteCustomer(Long id) throws DAOException {
        crud.deleteCustomer(id);
    }

    @Override
    public void addTransportToCustomer(Long customerId, Long transportId) throws DAOException {
        crud.addTransportToCustomer(customerId, transportId);
    }

    @Override
    public void removeTransportFromCustomer(Long customerId, Long transportId) throws DAOException {
        crud.removeTransportFromCustomer(customerId, transportId);
    }

    @Override
    public Set<Long> getAllTransportIdsForCustomer(Long customerId) throws DAOException {
        return crud.getAllTransportIdsForCustomer(customerId);
    }

    @Override
    public BigDecimal getCustomerTotalSpent(Long customerId) throws DAOException {
        return report.getCustomerTotalSpent(customerId);
    }

    @Override
    public int getCustomerTransportsCount(Long customerId) throws DAOException {
        return report.getCustomerTransportsCount(customerId);
    }

    @Override
    public BigDecimal getAverageSpendingPerTransport(Long customerId) {
        return report.getAverageSpendingPerTransport(customerId);
    }
}
