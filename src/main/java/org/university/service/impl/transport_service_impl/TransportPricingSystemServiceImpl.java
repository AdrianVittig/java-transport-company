package org.university.service.impl.transport_service_impl;

import org.university.entity.Transport;
import org.university.exception.DAOException;
import org.university.service.contract.transport_service.TransportPricingSystemService;
import org.university.util.PaymentStatus;

import java.math.BigDecimal;

public class TransportPricingSystemServiceImpl implements TransportPricingSystemService {
    @Override
    public BigDecimal calculateTotalPrice(Transport transport) throws DAOException {

        if(transport == null){
            throw new DAOException("Transport cannot be null");
        }

        if(transport.getInitPrice() == null
                || transport.getQuantity() == null){
            transport.setTotalPrice(BigDecimal.ZERO);
        }

        if(transport.getCargoType() == null){
            throw new DAOException("Cargo type cannot be null");
        }

        BigDecimal coefficient;

        switch(transport.getCargoType()){
            case GOODS -> coefficient = BigDecimal.valueOf(1.5);
            case PASSENGERS -> coefficient = BigDecimal.valueOf(1.35);
            case ADR -> coefficient = BigDecimal.valueOf(2);
            default -> coefficient = BigDecimal.ONE;
        }

        BigDecimal total = transport.getInitPrice()
                .multiply(transport.getQuantity()).multiply(coefficient);
        transport.setTotalPrice(total);

        transport.setPaymentStatus(PaymentStatus.NOT_PAID);

        return total;
    }
}
