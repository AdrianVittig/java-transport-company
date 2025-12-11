package org.university.service.impl.transport_service_impl;

import org.university.dao.TransportDao;
import org.university.entity.Transport;
import org.university.service.contract.transport_service.TransportFilterService;

import java.util.List;
import java.util.stream.Collectors;

public class TransportFilterServiceImpl implements TransportFilterService {
    private final TransportDao transportDao;

    public TransportFilterServiceImpl(TransportDao transportDao) {
        this.transportDao = transportDao;
    }

    @Override
    public List<Transport> filterTransportsByDestination(String destination) {
        if(destination == null || destination.isBlank()){
            return List.of();
        }
        String managedDestination = destination.trim().toLowerCase();
        return transportDao.getAllTransports()
                .stream()
                .filter(transport -> {
                    String transportEndPoint = transport.getEndPoint();
                    if(transportEndPoint == null){
                        return false;
                    }
                    return transportEndPoint.trim().toLowerCase().contains(managedDestination);
                })
                .collect(Collectors.toList());
    }
}
