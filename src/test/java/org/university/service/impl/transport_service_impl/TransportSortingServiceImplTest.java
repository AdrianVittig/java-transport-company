package org.university.service.impl.transport_service_impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.university.dao.TransportDao;
import org.university.entity.Transport;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransportSortingServiceImplTest {

    @Mock TransportDao transportDao;

    TransportSortingServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new TransportSortingServiceImpl(transportDao);
    }

    @Test
    void sortTransportsByDestinationAscending_shouldDelegate_true() {
        Transport t1 = new Transport();
        t1.setId(1L);
        Transport t2 = new Transport();
        t2.setId(2L);

        when(transportDao.sortByDestinationAscending(true)).thenReturn(List.of(t1, t2));

        List<Transport> result = service.sortTransportsByDestinationAscending(true);

        assertEquals(2, result.size());
        assertSame(t1, result.get(0));
        assertSame(t2, result.get(1));
        verify(transportDao).sortByDestinationAscending(true);
        verifyNoMoreInteractions(transportDao);
    }

    @Test
    void sortTransportsByDestinationAscending_shouldDelegate_false() {
        when(transportDao.sortByDestinationAscending(false)).thenReturn(List.of());

        List<Transport> result = service.sortTransportsByDestinationAscending(false);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(transportDao).sortByDestinationAscending(false);
        verifyNoMoreInteractions(transportDao);
    }
}
