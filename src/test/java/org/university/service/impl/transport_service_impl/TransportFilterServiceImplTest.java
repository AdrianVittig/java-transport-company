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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransportFilterServiceImplTest {

    @Mock TransportDao transportDao;

    TransportFilterServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new TransportFilterServiceImpl(transportDao);
    }

    @Test
    void filterTransportsByDestination_shouldReturnEmpty_whenNull() {
        List<Transport> result = service.filterTransportsByDestination(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verifyNoInteractions(transportDao);
    }

    @Test
    void filterTransportsByDestination_shouldReturnEmpty_whenBlank() {
        List<Transport> result = service.filterTransportsByDestination("   ");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verifyNoInteractions(transportDao);
    }

    @Test
    void filterTransportsByDestination_shouldTrimAndLowercase_andDelegateToDao() {
        Transport t1 = new Transport();
        t1.setId(1L);

        Transport t2 = new Transport();
        t2.setId(2L);

        when(transportDao.filterByDestination(eq("sofia"))).thenReturn(List.of(t1, t2));

        List<Transport> result = service.filterTransportsByDestination("  SoFiA  ");

        assertEquals(2, result.size());
        assertSame(t1, result.get(0));
        assertSame(t2, result.get(1));
        verify(transportDao).filterByDestination(eq("sofia"));
        verifyNoMoreInteractions(transportDao);
    }
}
