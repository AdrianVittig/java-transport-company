package org.university.service.contract.transport_service;

import org.university.dto.TransportDto;
import org.university.exception.DAOException;

public interface TransportFileService {
    void saveTransport(Long transportId) throws DAOException;
    TransportDto loadTransportsFromFile(Long transportId) throws DAOException;
}
