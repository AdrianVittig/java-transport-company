package org.university.service.impl.transport_service_impl;

import org.university.dao.TransportDao;
import org.university.entity.Transport;
import org.university.service.contract.transport_service.TransportSortingService;

import java.util.List;

public class TransportSortingServiceImpl implements TransportSortingService {
    private final TransportDao transportDao;

    public TransportSortingServiceImpl(TransportDao transportDao) {
        this.transportDao = transportDao;
    }

    @Override
    public List<Transport> sortTransportsByDestinationAscending(boolean isAscending) {
        return transportDao.sortByDestinationAscending(isAscending);
    }
}
