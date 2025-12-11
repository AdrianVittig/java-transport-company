package org.university.service.impl.customer_service_impl;

import org.university.dao.CustomerDao;
import org.university.dao.TransportDao;
import org.university.entity.Customer;
import org.university.entity.Transport;
import org.university.exception.DAOException;
import org.university.service.contract.customer_service.CustomerReportService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CustomerReportServiceImpl implements CustomerReportService {
    private final CustomerDao customerDao;
    private final TransportDao transportDao;

    public CustomerReportServiceImpl(CustomerDao customerDao, TransportDao transportDao) {
        this.customerDao = customerDao;
        this.transportDao = transportDao;
    }

    @Override
    public BigDecimal getCustomerTotalSpent(Long customerId) throws DAOException {
        Customer customer = customerDao.getCustomerById(customerId);
        if(customer == null){
            throw new DAOException("Customer with id " + customerId + " does not exist");
        }
        BigDecimal totalSpent = BigDecimal.ZERO;
        if(customer.getTransportSet() == null){
            return BigDecimal.ZERO;
        }
        for(Transport transport : customer.getTransportSet()){
            BigDecimal price = transport.getTotalPrice();
            if(price == null){
                price = BigDecimal.ZERO;
            }
            totalSpent = totalSpent.add(price);
        }
        return totalSpent;
    }

    @Override
    public int getCustomerTransportsCount(Long customerId) {
        Customer customer = customerDao.getCustomerById(customerId);
        if(customer == null){
            throw new DAOException("Customer with id " + customerId + " does not exist");
        }

        return customer.getTransportSet() != null ? customer.getTransportSet().size() : 0;
    }

    @Override
    public BigDecimal getAverageSpendingPerTransport(Long customerId) throws DAOException{
        Customer customer = customerDao.getCustomerById(customerId);
        if(customer == null){
            throw new DAOException("Customer with id " + customerId + " does not exist");
        }

        List<Transport> transportList = transportDao.getAllTransports()
                .stream()
                .filter(transport -> transport.getId() != null
                        && transport.getCustomer() != null
                        && transport.getCustomer().getId().equals(customerId))
                .toList();

        if(transportList.isEmpty()){
            return BigDecimal.ZERO;
        }

        BigDecimal total = transportList.stream()
                .map(transport -> transport.getTotalPrice() != null
                ? transport.getTotalPrice() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total.divide(BigDecimal.valueOf(transportList.size()), RoundingMode.HALF_UP);
    }
}
