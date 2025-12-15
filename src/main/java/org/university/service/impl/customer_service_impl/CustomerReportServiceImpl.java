package org.university.service.impl.customer_service_impl;

import org.university.dao.CustomerDao;
import org.university.dao.TransportDao;
import org.university.entity.Customer;
import org.university.entity.Transport;
import org.university.exception.DAOException;
import org.university.service.contract.customer_service.CustomerReportService;
import org.university.util.PaymentStatus;

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
        if (customer == null) throw new DAOException("Customer with id " + customerId + " does not exist");

        return transportDao.getAllTransports().stream()
                .filter(t -> t.getCustomer() != null && customerId.equals(t.getCustomer().getId()))
                .filter(t -> t.getPaymentStatus() == PaymentStatus.PAID)
                .map(t -> t.getTotalPrice() != null ? t.getTotalPrice() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public int getCustomerTransportsCount(Long customerId) throws DAOException {
        Customer customer = customerDao.getCustomerById(customerId);
        if (customer == null) throw new DAOException("Customer with id " + customerId + " does not exist");

        return (int) transportDao.getAllTransports().stream()
                .filter(t -> t.getCustomer() != null && customerId.equals(t.getCustomer().getId()))
                .filter(t -> t.getPaymentStatus() == PaymentStatus.PAID)
                .count();
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

        return total.divide(BigDecimal.valueOf(transportList.size()),2, RoundingMode.HALF_UP);
    }
}
