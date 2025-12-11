package org.university.service.impl.vehicle_service_impl;

import org.university.dto.VehicleDto;
import org.university.entity.Vehicle;
import org.university.exception.DAOException;
import org.university.service.contract.vehicle_service.VehicleCRUDService;
import org.university.service.contract.vehicle_service.VehicleGeneralService;
import org.university.service.contract.vehicle_service.VehicleReportService;
import org.university.util.VehicleType;

import java.math.BigDecimal;
import java.util.Set;

public class VehicleGeneralServiceImpl implements VehicleGeneralService {
    private final VehicleCRUDService crud;
    private final VehicleReportService report;

    public VehicleGeneralServiceImpl(VehicleCRUDService crud, VehicleReportService report) {
        this.crud = crud;
        this.report = report;
    }

    @Override
    public Vehicle mapToEntity(VehicleDto vehicleDto) {
        return crud.mapToEntity(vehicleDto);
    }

    @Override
    public VehicleDto mapToDto(Vehicle vehicle) {
        return crud.mapToDto(vehicle);
    }

    @Override
    public VehicleDto createVehicle(VehicleDto vehicleDto) throws DAOException {
        return crud.createVehicle(vehicleDto);
    }

    @Override
    public VehicleDto getVehicleById(Long id) throws DAOException {
        return crud.getVehicleById(id);
    }

    @Override
    public Set<VehicleDto> getAllVehicles() {
        return crud.getAllVehicles();
    }

    @Override
    public VehicleDto updateVehicle(Long id, VehicleDto vehicleDto) throws DAOException {
        return crud.updateVehicle(id, vehicleDto);
    }

    @Override
    public void deleteVehicle(Long id) throws DAOException {
        crud.deleteVehicle(id);
    }

    @Override
    public Long getCompanyIdForVehicle(Long vehicleId) throws DAOException {
        return crud.getCompanyIdForVehicle(vehicleId);
    }

    @Override
    public Long getEmployeeIdForVehicle(Long vehicleId) throws DAOException {
        return crud.getEmployeeIdForVehicle(vehicleId);
    }

    @Override
    public Set<Long> getAllTransportIdsForVehicle(Long vehicleId) throws DAOException {
        return crud.getAllTransportIdsForVehicle(vehicleId);
    }

    @Override
    public BigDecimal getRevenueByVehicleType(VehicleType type) {
        return report.getRevenueByVehicleType(type);
    }

    @Override
    public BigDecimal getAverageRevenueForAVehiclePerTransport(Long vehicleId) throws DAOException {
        return report.getAverageRevenueForAVehiclePerTransport(vehicleId);
    }
}
