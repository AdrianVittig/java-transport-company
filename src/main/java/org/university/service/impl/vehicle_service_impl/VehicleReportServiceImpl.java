package org.university.service.impl.vehicle_service_impl;

import org.university.dao.TransportDao;
import org.university.dao.VehicleDao;
import org.university.entity.Transport;
import org.university.entity.Vehicle;
import org.university.exception.DAOException;
import org.university.service.contract.vehicle_service.VehicleReportService;
import org.university.util.VehicleType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class VehicleReportServiceImpl implements VehicleReportService {
    private final VehicleDao vehicleDao;
    private final TransportDao transportDao;

    public VehicleReportServiceImpl(VehicleDao vehicleDao, TransportDao transportDao) {
        this.vehicleDao = vehicleDao;
        this.transportDao = transportDao;
    }

    @Override
    public BigDecimal getRevenueByVehicleType(VehicleType type) {
        if(type == null){
            return BigDecimal.ZERO;
        }

        return transportDao.getAllTransports()
                .stream()
                .filter(transport ->
                        transport.getVehicle() != null
                && transport.getVehicle().getVehicleType() == type)
                .map(transport -> {
                    BigDecimal price = transport.getTotalPrice();
                    return price != null ? price : BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getAverageRevenueForAVehiclePerTransport(Long vehicleId) throws DAOException{
        Vehicle vehicle = vehicleDao.getVehicleById(vehicleId);
        if(vehicle == null){
            throw new DAOException("Vehicle with id " + vehicleId + " does not exist");
        }
        List<Transport> transportList = transportDao.getAllTransports()
                .stream()
                .filter(transport ->
                        transport.getVehicle() != null
                                && transport.getId() != null
                                &&transport.getVehicle().getId().equals(vehicleId))
                .toList();

        if(transportList.isEmpty()){
            return BigDecimal.ZERO;
        }

        BigDecimal total = transportList
                .stream()
                .map(transport -> {
                    BigDecimal price = transport.getTotalPrice();
                    return price != null ? price : BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.divide(BigDecimal.valueOf(transportList.size()), RoundingMode.HALF_UP);
    }
}
