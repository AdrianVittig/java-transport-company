package org.university.service.contract.transport_service;

import org.university.dto.TransportDto;
import org.university.entity.Transport;
import org.university.exception.DAOException;

import java.math.BigDecimal;

public interface TransportPricingSystemService {
    BigDecimal calculateTotalPrice(Transport transport) throws DAOException;
}
