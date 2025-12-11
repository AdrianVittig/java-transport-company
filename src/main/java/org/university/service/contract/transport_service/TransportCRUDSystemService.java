package org.university.service.contract.transport_service;

import org.university.dto.TransportDto;
import org.university.entity.Transport;
import org.university.exception.DAOException;

import java.util.Set;

public interface TransportCRUDSystemService {
    Transport mapToEntity(TransportDto transportDto);

    TransportDto mapToDto(Transport transport);

    TransportDto createTransport(TransportDto transportDto) throws DAOException;

    TransportDto getTransportById(Long id) throws DAOException;

    Set<TransportDto> getAllTransports();

    TransportDto updateTransport(Long id, TransportDto transportDto) throws DAOException;

    void deleteTransport(Long id) throws DAOException;

    Long getCompanyIdForTransport(Long transportId) throws DAOException;

    Long getEmployeeIdForTransport(Long transportId) throws DAOException;

    Long getCustomerIdForTransport(Long transportId) throws DAOException;

    Long getVehicleIdForTransport(Long transportId) throws DAOException;
}
