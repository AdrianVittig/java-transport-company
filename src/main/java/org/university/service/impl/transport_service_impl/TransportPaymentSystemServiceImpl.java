package org.university.service.impl.transport_service_impl;

import org.university.dao.CustomerDao;
import org.university.dao.TransportDao;
import org.university.entity.Customer;
import org.university.entity.Transport;
import org.university.exception.DAOException;
import org.university.service.contract.transport_service.TransportPaymentSystemService;
import org.university.util.PaymentStatus;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

public class TransportPaymentSystemServiceImpl implements TransportPaymentSystemService {
    private final TransportDao transportDao;
    private final CustomerDao customerDao;

    public TransportPaymentSystemServiceImpl(TransportDao transportDao, CustomerDao customerDao) {
        this.transportDao = transportDao;
        this.customerDao = customerDao;
    }

    @Override
    public void markTransportAsPaid(Long transportId) {
        Transport transport = transportDao.getTransportById(transportId);
        if(transport == null){
            throw new DAOException("Transport with id " + transportId + " does not exist");
        }

        if(transport.getPaymentStatus() == PaymentStatus.PAID){
            return;
        }

        transport.setPaymentStatus(PaymentStatus.PAID);
        transportDao.updateTransport(transport.getId(), transport);
    }

    @Override
    public BigDecimal calculateCustomerDebt(Long customerId) throws DAOException{
        Customer customer = customerDao.getCustomerById(customerId);
        if(customer == null){
            throw new DAOException("Customer with id " + customerId + " does not exist");
        }

        return transportDao.getAllTransports()
                .stream()
                .filter(transport -> transport.getCustomer() != null
                        && transport.getCustomer().getId().equals(customerId)
                        && transport.getPaymentStatus() == PaymentStatus.NOT_PAID)
                .map(transport -> transport.getTotalPrice() == null ? BigDecimal.ZERO : transport.getTotalPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void paySingleTransport(Long transportId) throws DAOException{
        Transport transport = transportDao.getTransportById(transportId);
        if(transport == null){
            throw new DAOException("Transport with id " + transportId + " does not exist");
        }
        markTransportAsPaid(transportId);
    }

    @Override
    public Set<Long> getUnpaidTransportIdsForCustomer(Long customerId) throws DAOException{
        Customer customer = customerDao.getCustomerById(customerId);
        if(customer == null){
            throw new DAOException("Customer with id " + customerId + " does not exist");
        }
        return transportDao.getAllTransports()
                .stream()
                .filter(transport -> transport.getCustomer() != null
                        && transport.getCustomer().getId().equals(customerId)
                        && transport.getPaymentStatus() == PaymentStatus.NOT_PAID)
                .map(Transport::getId)
                .collect(Collectors.toSet());
    }
}
