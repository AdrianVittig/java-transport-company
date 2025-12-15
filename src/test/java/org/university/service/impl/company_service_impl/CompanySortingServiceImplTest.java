package org.university.service.impl.company_service_impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.university.dao.CompanyDao;
import org.university.entity.Company;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanySortingServiceImplTest {

    @Mock CompanyDao companyDao;

    CompanySortingServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CompanySortingServiceImpl(companyDao);
    }

    @Test
    void sortCompaniesByNameAscending_shouldDelegateToDao_true() {
        Company c1 = Company.builder().name("A").revenue(BigDecimal.ZERO).build();
        Company c2 = Company.builder().name("B").revenue(BigDecimal.ZERO).build();
        List<Company> expected = List.of(c1, c2);

        when(companyDao.sortCompaniesByNameAscending(true)).thenReturn(expected);

        List<Company> result = service.sortCompaniesByNameAscending(true);

        assertSame(expected, result);
        verify(companyDao).sortCompaniesByNameAscending(true);
        verifyNoMoreInteractions(companyDao);
    }

    @Test
    void sortCompaniesByNameAscending_shouldDelegateToDao_false() {
        Company c1 = Company.builder().name("B").revenue(BigDecimal.ZERO).build();
        Company c2 = Company.builder().name("A").revenue(BigDecimal.ZERO).build();
        List<Company> expected = List.of(c1, c2);

        when(companyDao.sortCompaniesByNameAscending(false)).thenReturn(expected);

        List<Company> result = service.sortCompaniesByNameAscending(false);

        assertSame(expected, result);
        verify(companyDao).sortCompaniesByNameAscending(false);
        verifyNoMoreInteractions(companyDao);
    }

    @Test
    void sortCompaniesByRevenueAscending_shouldDelegateToDao_true() {
        Company c1 = Company.builder().name("A").revenue(new BigDecimal("1.00")).build();
        Company c2 = Company.builder().name("B").revenue(new BigDecimal("2.00")).build();
        List<Company> expected = List.of(c1, c2);

        when(companyDao.sortCompaniesByRevenue(true)).thenReturn(expected);

        List<Company> result = service.sortCompaniesByRevenueAscending(true);

        assertSame(expected, result);
        verify(companyDao).sortCompaniesByRevenue(true);
        verifyNoMoreInteractions(companyDao);
    }

    @Test
    void sortCompaniesByRevenueAscending_shouldDelegateToDao_false() {
        Company c1 = Company.builder().name("B").revenue(new BigDecimal("2.00")).build();
        Company c2 = Company.builder().name("A").revenue(new BigDecimal("1.00")).build();
        List<Company> expected = List.of(c1, c2);

        when(companyDao.sortCompaniesByRevenue(false)).thenReturn(expected);

        List<Company> result = service.sortCompaniesByRevenueAscending(false);

        assertSame(expected, result);
        verify(companyDao).sortCompaniesByRevenue(false);
        verifyNoMoreInteractions(companyDao);
    }
}