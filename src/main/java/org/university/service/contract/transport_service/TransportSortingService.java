package org.university.service.contract.transport_service;

import org.university.entity.Transport;

import java.util.List;

public interface TransportSortingService {
    List<Transport> sortTransportsByDestinationAscending();
    List<Transport> sortTransportsByDestinationDescending();
}
