package org.university.service.impl.transport_service_impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.university.dto.TransportDto;
import org.university.exception.DAOException;
import org.university.service.contract.transport_service.TransportGeneralService;
import org.university.util.CargoType;
import org.university.util.PaymentStatus;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransportFileServiceImplTest {

    private static final String DIR = "src/main/java/org/university/transport_files";

    @Mock TransportGeneralService transportGeneralService;

    TransportFileServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new TransportFileServiceImpl(transportGeneralService);
        new File(DIR).mkdirs();
        deleteFileIfExists(1L);
        deleteFileIfExists(2L);
    }

    @AfterEach
    void tearDown() {
        deleteFileIfExists(1L);
        deleteFileIfExists(2L);
    }

    @Test
    void saveTransport_shouldCreateFile() throws DAOException {
        TransportDto dto = new TransportDto();
        dto.setId(1L);
        dto.setStartPoint("S");
        dto.setEndPoint("E");
        dto.setDepartureDate(LocalDate.of(2025, 1, 1));
        dto.setArrivalDate(LocalDate.of(2025, 1, 2));
        dto.setCargoType(CargoType.GOODS);
        dto.setQuantity(new BigDecimal("2.00"));
        dto.setInitPrice(new BigDecimal("10.00"));
        dto.setPaymentStatus(PaymentStatus.NOT_PAID);
        dto.setTotalPrice(new BigDecimal("20.00"));

        when(transportGeneralService.getTransportById(1L)).thenReturn(dto);

        service.saveTransport(1L);

        File file = fileFor(1L);
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
        verify(transportGeneralService).getTransportById(1L);
    }

    @Test
    void saveTransport_shouldThrow_whenTransportMissing() {
        when(transportGeneralService.getTransportById(1L)).thenReturn(null);

        assertThrows(DAOException.class, () -> service.saveTransport(1L));
        assertFalse(fileFor(1L).exists());
        verify(transportGeneralService).getTransportById(1L);
    }

    @Test
    void loadTransportsFromFile_shouldReturnDto() throws DAOException {
        TransportDto dto = new TransportDto();
        dto.setId(2L);
        dto.setStartPoint("S2");
        dto.setEndPoint("E2");
        dto.setDepartureDate(LocalDate.of(2025, 2, 1));
        dto.setArrivalDate(LocalDate.of(2025, 2, 2));
        dto.setCargoType(CargoType.ADR);
        dto.setQuantity(new BigDecimal("1.00"));
        dto.setInitPrice(new BigDecimal("3.00"));
        dto.setPaymentStatus(PaymentStatus.PAID);
        dto.setTotalPrice(new BigDecimal("3.00"));

        when(transportGeneralService.getTransportById(2L)).thenReturn(dto);
        service.saveTransport(2L);

        TransportDto loaded = service.loadTransportsFromFile(2L);

        assertEquals(2L, loaded.getId());
        assertEquals("S2", loaded.getStartPoint());
        assertEquals("E2", loaded.getEndPoint());
        assertEquals(LocalDate.of(2025, 2, 1), loaded.getDepartureDate());
        assertEquals(LocalDate.of(2025, 2, 2), loaded.getArrivalDate());
        assertEquals(CargoType.ADR, loaded.getCargoType());
        assertEquals(new BigDecimal("1.00"), loaded.getQuantity());
        assertEquals(new BigDecimal("3.00"), loaded.getInitPrice());
        assertEquals(PaymentStatus.PAID, loaded.getPaymentStatus());
        assertEquals(new BigDecimal("3.00"), loaded.getTotalPrice());
    }

    @Test
    void loadTransportsFromFile_shouldThrow_whenFileMissing() {
        deleteFileIfExists(1L);
        assertThrows(DAOException.class, () -> service.loadTransportsFromFile(1L));
    }

    private static File fileFor(Long id) {
        return new File(DIR + File.separator + "transport_" + id + ".ser");
    }

    private static void deleteFileIfExists(Long id) {
        File f = fileFor(id);
        if (f.exists()) {
            f.delete();
        }
    }
}
