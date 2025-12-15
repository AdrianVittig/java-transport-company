package org.university.service.impl.transport_service_impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.university.entity.Transport;
import org.university.exception.DAOException;
import org.university.util.CargoType;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransportPricingSystemServiceImplTest {

    TransportPricingSystemServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new TransportPricingSystemServiceImpl();
    }

    @Test
    void calculateTotalPrice_shouldThrow_whenTransportNull() {
        assertThrows(DAOException.class, () -> service.calculateTotalPrice(null));
    }

    @Test
    void calculateTotalPrice_shouldReturnZero_andSetTotalPriceZero_whenInitOrQuantityNull() throws DAOException {
        Transport t1 = new Transport();
        t1.setInitPrice(null);
        t1.setQuantity(new BigDecimal("2.00"));

        BigDecimal r1 = service.calculateTotalPrice(t1);
        assertEquals(BigDecimal.ZERO, r1);
        assertEquals(BigDecimal.ZERO, t1.getTotalPrice());

        Transport t2 = new Transport();
        t2.setInitPrice(new BigDecimal("10.00"));
        t2.setQuantity(null);

        BigDecimal r2 = service.calculateTotalPrice(t2);
        assertEquals(BigDecimal.ZERO, r2);
        assertEquals(BigDecimal.ZERO, t2.getTotalPrice());
    }

    @Test
    void calculateTotalPrice_shouldThrow_whenCargoTypeNull_andInitAndQuantityPresent() {
        Transport t = new Transport();
        t.setInitPrice(new BigDecimal("10.00"));
        t.setQuantity(new BigDecimal("2.00"));
        t.setCargoType(null);

        assertThrows(DAOException.class, () -> service.calculateTotalPrice(t));
    }

    @Test
    void calculateTotalPrice_shouldCalculateForGoods() throws DAOException {
        Transport t = new Transport();
        t.setInitPrice(new BigDecimal("10.00"));
        t.setQuantity(new BigDecimal("2.00"));
        t.setCargoType(CargoType.GOODS);

        BigDecimal total = service.calculateTotalPrice(t);

        assertEquals(new BigDecimal("30.00"), total);
        assertEquals(new BigDecimal("30.00"), t.getTotalPrice());
    }

    @Test
    void calculateTotalPrice_shouldCalculateForPassengers() throws DAOException {
        Transport t = new Transport();
        t.setInitPrice(new BigDecimal("10.00"));
        t.setQuantity(new BigDecimal("2.00"));
        t.setCargoType(CargoType.PASSENGERS);

        BigDecimal total = service.calculateTotalPrice(t);

        assertEquals(new BigDecimal("27.00"), total);
        assertEquals(new BigDecimal("27.00"), t.getTotalPrice());
    }

    @Test
    void calculateTotalPrice_shouldCalculateForAdr() throws DAOException {
        Transport t = new Transport();
        t.setInitPrice(new BigDecimal("10.00"));
        t.setQuantity(new BigDecimal("2.00"));
        t.setCargoType(CargoType.ADR);

        BigDecimal total = service.calculateTotalPrice(t);

        assertEquals(new BigDecimal("40.00"), total);
        assertEquals(new BigDecimal("40.00"), t.getTotalPrice());
    }
}
