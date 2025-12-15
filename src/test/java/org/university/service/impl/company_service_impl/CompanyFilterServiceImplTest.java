package org.university.service.impl.company_service_impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.university.dao.CompanyDao;
import org.university.entity.Company;
import org.university.service.contract.company_service.CompanyFilterService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyFilterServiceImplTest {

    @Mock
    CompanyDao companyDao;

    CompanyFilterService service;

    @BeforeEach
    void setUp() {
        service = new CompanyFilterServiceImpl(companyDao);
    }

    @Test
    void filterCompaniesWithRevenueOver_shouldReturnEmpty_whenThresholdNull() {
        List<Company> result = service.filterCompaniesWithRevenueOver(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verifyNoInteractions(companyDao);
    }

    @Test
    void filterCompaniesWithRevenueOver_shouldCallDao_whenThresholdProvided() {
        BigDecimal threshold = new BigDecimal("100.00");

        Company c1 = Company.builder().name("A").revenue(new BigDecimal("150.00")).build();
        Company c2 = Company.builder().name("B").revenue(new BigDecimal("200.00")).build();
        List<Company> expected = List.of(c1, c2);

        when(companyDao.filterByRevenue(threshold)).thenReturn(expected);

        List<Company> result = service.filterCompaniesWithRevenueOver(threshold);

        assertSame(expected, result);
        verify(companyDao, times(1)).filterByRevenue(threshold);
        verifyNoMoreInteractions(companyDao);
    }

    @Test
    void filterCompaniesWhichNameIncludes_shouldReturnEmpty_whenNull() {
        List<Company> result = service.filterCompaniesWhichNameIncludes(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verifyNoInteractions(companyDao);
    }

    @Test
    void filterCompaniesWhichNameIncludes_shouldReturnEmpty_whenBlank() {
        List<Company> result = service.filterCompaniesWhichNameIncludes("   ");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verifyNoInteractions(companyDao);
    }

    @Test
    void filterCompaniesWhichNameIncludes_shouldTrimAndCallDao_whenProvided() {
        String input = "  ac  ";
        String trimmed = "ac";

        Company c1 = Company.builder().name("ACME").revenue(BigDecimal.ZERO).build();
        List<Company> expected = List.of(c1);

        when(companyDao.filterByName(trimmed)).thenReturn(expected);

        List<Company> result = service.filterCompaniesWhichNameIncludes(input);

        assertSame(expected, result);
        verify(companyDao, times(1)).filterByName(trimmed);
        verifyNoMoreInteractions(companyDao);
    }
}