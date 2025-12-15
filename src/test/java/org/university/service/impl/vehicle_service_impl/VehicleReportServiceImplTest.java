package org.university.service.impl.vehicle_service_impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.university.dao.TransportDao;
import org.university.dao.VehicleDao;
import org.university.entity.Transport;
import org.university.entity.Vehicle;
import org.university.exception.DAOException;
import org.university.util.VehicleType;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleReportServiceImplTest {

    @Mock VehicleDao vehicleDao;
    @Mock TransportDao transportDao;

    VehicleReportServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new VehicleReportServiceImpl(vehicleDao, transportDao);
    }

    @Test
    void getRevenueByVehicleType_shouldReturnZero_whenTypeNull() {
        BigDecimal result = service.getRevenueByVehicleType(null);
        assertEquals(BigDecimal.ZERO, result);
        verifyNoInteractions(transportDao);
    }

    @Test
    void getRevenueByVehicleType_shouldSumOnlyMatchingType_andTreatNullTotalPriceAsZero() {
        Vehicle vCar = new Vehicle();
        vCar.setVehicleType(VehicleType.CAR);

        Vehicle vTruck = new Vehicle();
        vTruck.setVehicleType(VehicleType.TRUCK);

        Transport t1 = new Transport();
        t1.setVehicle(vCar);
        t1.setTotalPrice(new BigDecimal("10.00"));

        Transport t2 = new Transport();
        t2.setVehicle(vCar);
        t2.setTotalPrice(null);

        Transport t3 = new Transport();
        t3.setVehicle(vTruck);
        t3.setTotalPrice(new BigDecimal("999.00"));

        Transport t4 = new Transport();
        t4.setVehicle(null);
        t4.setTotalPrice(new BigDecimal("5.00"));

        when(transportDao.getAllTransports()).thenReturn(List.of(t1, t2, t3, t4));

        BigDecimal result = service.getRevenueByVehicleType(VehicleType.CAR);

        assertEquals(new BigDecimal("10.00"), result);
    }

    @Test
    void getAverageRevenueForAVehiclePerTransport_shouldThrow_whenVehicleMissing() {
        when(vehicleDao.getVehicleById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.getAverageRevenueForAVehiclePerTransport(1L));
        verifyNoInteractions(transportDao);
    }

    @Test
    void getAverageRevenueForAVehiclePerTransport_shouldReturnZero_whenNoMatchingTransports() throws DAOException {
        Vehicle v = new Vehicle();
        v.setId(1L);

        Vehicle other = new Vehicle();
        other.setId(2L);

        Transport t1 = new Transport();
        t1.setId(10L);
        t1.setVehicle(other);
        t1.setTotalPrice(new BigDecimal("10.00"));

        Transport t2 = new Transport();
        t2.setId(null);
        t2.setVehicle(v);
        t2.setTotalPrice(new BigDecimal("10.00"));

        Transport t3 = new Transport();
        t3.setId(11L);
        t3.setVehicle(null);
        t3.setTotalPrice(new BigDecimal("10.00"));

        when(vehicleDao.getVehicleById(1L)).thenReturn(v);
        when(transportDao.getAllTransports()).thenReturn(List.of(t1, t2, t3));

        BigDecimal avg = service.getAverageRevenueForAVehiclePerTransport(1L);

        assertEquals(BigDecimal.ZERO, avg);
    }

    @Test
    void getAverageRevenueForAVehiclePerTransport_shouldAverageMatching_andRoundTo2() throws DAOException {
        Vehicle v = new Vehicle();
        v.setId(1L);

        Transport t1 = new Transport();
        t1.setId(10L);
        t1.setVehicle(v);
        t1.setTotalPrice(new BigDecimal("10.00"));

        Transport t2 = new Transport();
        t2.setId(11L);
        t2.setVehicle(v);
        t2.setTotalPrice(new BigDecimal("0.01"));

        Transport t3 = new Transport();
        t3.setId(12L);
        t3.setVehicle(v);
        t3.setTotalPrice(null);

        when(vehicleDao.getVehicleById(1L)).thenReturn(v);
        when(transportDao.getAllTransports()).thenReturn(List.of(t1, t2, t3));

        BigDecimal avg = service.getAverageRevenueForAVehiclePerTransport(1L);

        assertEquals(new BigDecimal("3.34"), avg);
    }
}
