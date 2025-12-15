package org.university.service.impl.company_service_impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.university.dao.CompanyDao;
import org.university.dao.TransportDao;
import org.university.entity.Company;
import org.university.entity.Transport;
import org.university.exception.DAOException;
import org.university.service.contract.company_service.CompanyReportService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyReportServiceImplTest {


    @Mock CompanyDao companyDao;
    @Mock TransportDao transportDao;

    CompanyReportService service;

    @BeforeEach
    void setUp() {
        service = new CompanyReportServiceImpl(companyDao, transportDao);
    }

    @Test
    void getCompanyTotalRevenue_shouldThrow_whenCompanyMissing() {
        when(companyDao.getCompanyById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.getCompanyTotalRevenue(1L));
    }

    @Test
    void getCompanyTotalRevenue_shouldReturnRevenue_whenNotNull() throws DAOException {
        Company c = Company.builder().name("C").revenue(new BigDecimal("123.45")).build();
        c.setId(1L);
        when(companyDao.getCompanyById(1L)).thenReturn(c);

        BigDecimal result = service.getCompanyTotalRevenue(1L);

        assertEquals(new BigDecimal("123.45"), result);
    }

    @Test
    void getCompanyTotalRevenue_shouldReturnZero_whenRevenueNull() throws DAOException {
        Company c = Company.builder().name("C").revenue(null).build();
        c.setId(1L);
        when(companyDao.getCompanyById(1L)).thenReturn(c);

        BigDecimal result = service.getCompanyTotalRevenue(1L);

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void getCompanyTransportsCount_shouldThrow_whenCompanyMissing() {
        when(companyDao.getCompanyById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.getCompanyTransportsCount(1L));
    }

    @Test
    void getCompanyTransportsCount_shouldCountOnlyForCompanyId() throws DAOException {
        Company c1 = Company.builder().name("C1").revenue(BigDecimal.ZERO).build();
        c1.setId(1L);
        Company c2 = Company.builder().name("C2").revenue(BigDecimal.ZERO).build();
        c2.setId(2L);

        Transport t1 = Transport.builder().totalPrice(new BigDecimal("10.00")).build();
        t1.setId(11L);
        t1.setCompany(c1);

        Transport t2 = Transport.builder().totalPrice(new BigDecimal("20.00")).build();
        t2.setId(12L);
        t2.setCompany(c1);

        Transport t3 = Transport.builder().totalPrice(new BigDecimal("30.00")).build();
        t3.setId(13L);
        t3.setCompany(c2);

        Transport t4 = Transport.builder().totalPrice(new BigDecimal("40.00")).build();
        t4.setId(14L);
        t4.setCompany(null);

        when(companyDao.getCompanyById(1L)).thenReturn(c1);
        when(transportDao.getAllTransports()).thenReturn(List.of(t1, t2, t3, t4));

        int count = service.getCompanyTransportsCount(1L);

        assertEquals(2, count);
    }

    @Test
    void getCompanyAverageTransportRevenue_shouldThrow_whenCompanyMissing() {
        when(companyDao.getCompanyById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.getCompanyAverageTransportRevenue(1L));
    }

    @Test
    void getCompanyAverageTransportRevenue_shouldReturnZero_whenNoCompanyTransports() throws DAOException {
        Company c1 = Company.builder().name("C1").revenue(BigDecimal.ZERO).build();
        c1.setId(1L);

        Company c2 = Company.builder().name("C2").revenue(BigDecimal.ZERO).build();
        c2.setId(2L);

        Transport tOther = Transport.builder().totalPrice(new BigDecimal("10.00")).build();
        tOther.setId(11L);
        tOther.setCompany(c2);

        when(companyDao.getCompanyById(1L)).thenReturn(c1);
        when(transportDao.getAllTransports()).thenReturn(List.of(tOther));

        BigDecimal avg = service.getCompanyAverageTransportRevenue(1L);

        assertEquals(BigDecimal.ZERO, avg);
    }

    @Test
    void getCompanyAverageTransportRevenue_shouldComputeAverageAndRound() throws DAOException {
        Company c1 = Company.builder().name("C1").revenue(BigDecimal.ZERO).build();
        c1.setId(1L);

        Transport t1 = Transport.builder().totalPrice(new BigDecimal("10.00")).build();
        t1.setId(11L);
        t1.setCompany(c1);

        Transport t2 = Transport.builder().totalPrice(new BigDecimal("0.00")).build();
        t2.setId(12L);
        t2.setCompany(c1);

        Transport t3 = Transport.builder().totalPrice(new BigDecimal("0.00")).build();
        t3.setId(13L);
        t3.setCompany(c1);

        when(companyDao.getCompanyById(1L)).thenReturn(c1);
        when(transportDao.getAllTransports()).thenReturn(List.of(t1, t2, t3));

        BigDecimal avg = service.getCompanyAverageTransportRevenue(1L);

        assertEquals(new BigDecimal("3.33"), avg);
    }

    @Test
    void getCompanyAverageTransportRevenue_shouldTreatNullTotalPriceAsZero() throws DAOException {
        Company c1 = Company.builder().name("C1").revenue(BigDecimal.ZERO).build();
        c1.setId(1L);

        Transport t1 = Transport.builder().totalPrice(null).build();
        t1.setId(11L);
        t1.setCompany(c1);

        Transport t2 = Transport.builder().totalPrice(new BigDecimal("1.00")).build();
        t2.setId(12L);
        t2.setCompany(c1);

        when(companyDao.getCompanyById(1L)).thenReturn(c1);
        when(transportDao.getAllTransports()).thenReturn(List.of(t1, t2));

        BigDecimal avg = service.getCompanyAverageTransportRevenue(1L);

        assertEquals(new BigDecimal("0.50"), avg);
    }
}