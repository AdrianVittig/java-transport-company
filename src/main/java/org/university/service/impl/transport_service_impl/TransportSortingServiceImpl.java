package org.university.service.impl.transport_service_impl;

import org.university.dao.TransportDao;
import org.university.entity.Transport;
import org.university.service.contract.transport_service.TransportSortingService;

import java.util.Comparator;
import java.util.List;

public class TransportSortingServiceImpl implements TransportSortingService {
    private final TransportDao transportDao;

    public TransportSortingServiceImpl(TransportDao transportDao) {
        this.transportDao = transportDao;
    }

    @Override
    public List<Transport> sortTransportsByDestinationAscending() {
        return transportDao.getAllTransports()
                .stream()
                .sorted(Comparator.comparing(c -> {
                    String destination = c.getEndPoint();
                    return destination == null ? "" : destination.trim().toLowerCase();
                }))
                .toList();
    }

    @Override
    public List<Transport> sortTransportsByDestinationDescending() {
        return transportDao.getAllTransports()
                .stream()
                .sorted(
                        Comparator.comparing((Transport transport) -> {
                            String destination = transport.getEndPoint();
                            return destination == null ? "" : destination.trim().toLowerCase();
                        }).reversed()
                )
                .toList();
    }
}
