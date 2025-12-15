package org.university.service.impl.transport_service_impl;

import org.university.entity.Transport;
import org.university.exception.DAOException;
import org.university.service.contract.transport_service.TransportPricingSystemService;

import java.math.BigDecimal;

public class TransportPricingSystemServiceImpl implements TransportPricingSystemService {
    @Override
    public BigDecimal calculateTotalPrice(Transport transport) throws DAOException {
        if (transport == null) {
            throw new DAOException("Transport cannot be null");
        }

        if (transport.getInitPrice() == null || transport.getQuantity() == null) {
            transport.setTotalPrice(BigDecimal.ZERO);
            return BigDecimal.ZERO;
        }

        if (transport.getCargoType() == null) {
            throw new DAOException("Cargo type cannot be null");
        }

        BigDecimal coefficient = switch (transport.getCargoType()) {
            case GOODS -> new BigDecimal("1.50");
            case PASSENGERS -> new BigDecimal("1.35");
            case ADR -> new BigDecimal("2.00");
        };

        BigDecimal total = transport.getInitPrice()
                .multiply(transport.getQuantity())
                .multiply(coefficient)
                .setScale(2, java.math.RoundingMode.HALF_UP);

        transport.setTotalPrice(total);
        return total;
    }
}
