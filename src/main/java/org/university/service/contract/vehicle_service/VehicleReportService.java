package org.university.service.contract.vehicle_service;

import org.university.exception.DAOException;
import org.university.util.VehicleType;

import java.math.BigDecimal;

public interface VehicleReportService {
    BigDecimal getRevenueByVehicleType(VehicleType type);
    BigDecimal getAverageRevenueForAVehiclePerTransport(Long vehicleId) throws DAOException;
}
