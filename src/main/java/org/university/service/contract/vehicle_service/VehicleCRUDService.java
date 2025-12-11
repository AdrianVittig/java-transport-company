package org.university.service.contract.vehicle_service;

import org.university.dto.VehicleDto;
import org.university.entity.Vehicle;
import org.university.exception.DAOException;

import java.util.Set;

public interface VehicleCRUDService {
    Vehicle mapToEntity(VehicleDto vehicleDto);

    VehicleDto mapToDto(Vehicle vehicle);

    VehicleDto createVehicle(VehicleDto vehicleDto) throws DAOException;

    VehicleDto getVehicleById(Long id) throws DAOException;

    Set<VehicleDto> getAllVehicles();

    VehicleDto updateVehicle(Long id, VehicleDto vehicleDto) throws DAOException;

    void deleteVehicle(Long id) throws DAOException;

    Long getCompanyIdForVehicle(Long vehicleId) throws DAOException;

    Long getEmployeeIdForVehicle(Long vehicleId) throws DAOException;

    Set<Long> getAllTransportIdsForVehicle(Long vehicleId) throws DAOException;
}
