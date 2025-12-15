package org.university.service.impl.company_service_impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.university.dto.CompanyDto;
import org.university.entity.Company;
import org.university.exception.DAOException;
import org.university.service.contract.company_service.CompanyCRUDService;
import org.university.service.contract.company_service.CompanyFilterService;
import org.university.service.contract.company_service.CompanyReportService;
import org.university.service.contract.company_service.CompanySortingService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyGeneralServiceImplTest {

    @Mock CompanyCRUDService crud;
    @Mock CompanyFilterService filter;
    @Mock CompanySortingService sorting;
    @Mock CompanyReportService report;

    CompanyGeneralServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CompanyGeneralServiceImpl(crud, filter, sorting, report);
    }

    @Test
    void mapToEntity() {
        CompanyDto dto = new CompanyDto();
        Company c = new Company();

        when(crud.mapToEntity(dto)).thenReturn(c);

        assertSame(c, service.mapToEntity(dto));
        verify(crud).mapToEntity(dto);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(filter, sorting, report);
    }

    @Test
    void mapToDto() {
        Company c = new Company();
        CompanyDto dto = new CompanyDto();

        when(crud.mapToDto(c)).thenReturn(dto);

        assertSame(dto, service.mapToDto(c));
        verify(crud).mapToDto(c);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(filter, sorting, report);
    }

    @Test
    void createCompany() throws DAOException {
        CompanyDto in = new CompanyDto();
        CompanyDto out = new CompanyDto();

        when(crud.createCompany(in)).thenReturn(out);

        assertSame(out, service.createCompany(in));
        verify(crud).createCompany(in);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(filter, sorting, report);
    }

    @Test
    void getCompanyById() {
        CompanyDto out = new CompanyDto();
        when(crud.getCompanyById(1L)).thenReturn(out);

        assertSame(out, service.getCompanyById(1L));
        verify(crud).getCompanyById(1L);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(filter, sorting, report);
    }

    @Test
    void getAllCompanies() {
        Set<CompanyDto> set = Set.of(new CompanyDto(), new CompanyDto());
        when(crud.getAllCompanies()).thenReturn(set);

        assertSame(set, service.getAllCompanies());
        verify(crud).getAllCompanies();
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(filter, sorting, report);
    }

    @Test
    void updateCompany() throws DAOException {
        CompanyDto in = new CompanyDto();
        CompanyDto out = new CompanyDto();

        when(crud.updateCompany(1L, in)).thenReturn(out);

        assertSame(out, service.updateCompany(1L, in));
        verify(crud).updateCompany(1L, in);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(filter, sorting, report);
    }

    @Test
    void deleteCompany() throws DAOException {
        doNothing().when(crud).deleteCompany(1L);

        service.deleteCompany(1L);

        verify(crud).deleteCompany(1L);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(filter, sorting, report);
    }

    @Test
    void addEmployeeToCompany() throws DAOException {
        doNothing().when(crud).addEmployeeToCompany(10L, 1L);

        service.addEmployeeToCompany(10L, 1L);

        verify(crud).addEmployeeToCompany(10L, 1L);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(filter, sorting, report);
    }

    @Test
    void removeEmployeeFromCompany() throws DAOException {
        doNothing().when(crud).removeEmployeeFromCompany(10L, 1L);

        service.removeEmployeeFromCompany(10L, 1L);

        verify(crud).removeEmployeeFromCompany(10L, 1L);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(filter, sorting, report);
    }

    @Test
    void getAllEmployeeIdsForCompany() throws DAOException {
        Set<Long> ids = Set.of(1L, 2L);
        when(crud.getAllEmployeeIdsForCompany(1L)).thenReturn(ids);

        assertSame(ids, service.getAllEmployeeIdsForCompany(1L));
        verify(crud).getAllEmployeeIdsForCompany(1L);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(filter, sorting, report);
    }

    @Test
    void addVehicleToCompany() throws DAOException {
        doNothing().when(crud).addVehicleToCompany(1L, 100L);

        service.addVehicleToCompany(1L, 100L);

        verify(crud).addVehicleToCompany(1L, 100L);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(filter, sorting, report);
    }

    @Test
    void removeVehicleFromCompany() throws DAOException {
        doNothing().when(crud).removeVehicleFromCompany(1L, 100L);

        service.removeVehicleFromCompany(1L, 100L);

        verify(crud).removeVehicleFromCompany(1L, 100L);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(filter, sorting, report);
    }

    @Test
    void getAllVehicleIdsForCompany() throws DAOException {
        Set<Long> ids = Set.of(10L, 11L);
        when(crud.getAllVehicleIdsForCompany(1L)).thenReturn(ids);

        assertSame(ids, service.getAllVehicleIdsForCompany(1L));
        verify(crud).getAllVehicleIdsForCompany(1L);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(filter, sorting, report);
    }

    @Test
    void getAllTransportIdsForCompany() throws DAOException {
        Set<Long> ids = Set.of(1000L, 1001L);
        when(crud.getAllTransportIdsForCompany(1L)).thenReturn(ids);

        assertSame(ids, service.getAllTransportIdsForCompany(1L));
        verify(crud).getAllTransportIdsForCompany(1L);
        verifyNoMoreInteractions(crud);
        verifyNoInteractions(filter, sorting, report);
    }

    @Test
    void filterCompaniesWithRevenueOver() {
        List<Company> list = List.of(new Company(), new Company());
        BigDecimal threshold = new BigDecimal("100.00");

        when(filter.filterCompaniesWithRevenueOver(threshold)).thenReturn(list);

        assertSame(list, service.filterCompaniesWithRevenueOver(threshold));
        verify(filter).filterCompaniesWithRevenueOver(threshold);
        verifyNoMoreInteractions(filter);
        verifyNoInteractions(crud, sorting, report);
    }

    @Test
    void filterCompaniesWhichNameIncludes() {
        List<Company> list = List.of(new Company());
        when(filter.filterCompaniesWhichNameIncludes("ac")).thenReturn(list);

        assertSame(list, service.filterCompaniesWhichNameIncludes("ac"));
        verify(filter).filterCompaniesWhichNameIncludes("ac");
        verifyNoMoreInteractions(filter);
        verifyNoInteractions(crud, sorting, report);
    }

    @Test
    void sortCompaniesByNameAscending() {
        List<Company> list = List.of(new Company(), new Company());
        when(sorting.sortCompaniesByNameAscending(true)).thenReturn(list);

        assertSame(list, service.sortCompaniesByNameAscending(false));
        verify(sorting).sortCompaniesByNameAscending(true);
        verify(sorting, never()).sortCompaniesByNameAscending(false);
        verifyNoMoreInteractions(sorting);
        verifyNoInteractions(crud, filter, report);
    }

    @Test
    void sortCompaniesByRevenueAscending() {
        List<Company> list = List.of(new Company());
        when(sorting.sortCompaniesByRevenueAscending(true)).thenReturn(list);

        assertSame(list, service.sortCompaniesByRevenueAscending(false));
        verify(sorting).sortCompaniesByRevenueAscending(true);
        verify(sorting, never()).sortCompaniesByRevenueAscending(false);
        verifyNoMoreInteractions(sorting);
        verifyNoInteractions(crud, filter, report);
    }

    @Test
    void getCompanyTotalRevenue() throws DAOException {
        when(report.getCompanyTotalRevenue(1L)).thenReturn(new BigDecimal("123.45"));

        BigDecimal result = service.getCompanyTotalRevenue(1L);

        assertEquals(0, new BigDecimal("123.45").compareTo(result));
        verify(report).getCompanyTotalRevenue(1L);
        verifyNoMoreInteractions(report);
        verifyNoInteractions(crud, filter, sorting);
    }

    @Test
    void getCompanyTransportsCount() throws DAOException {
        when(report.getCompanyTransportsCount(1L)).thenReturn(7);

        assertEquals(7, service.getCompanyTransportsCount(1L));
        verify(report).getCompanyTransportsCount(1L);
        verifyNoMoreInteractions(report);
        verifyNoInteractions(crud, filter, sorting);
    }

    @Test
    void getCompanyAverageTransportRevenue() throws DAOException {
        when(report.getCompanyAverageTransportRevenue(1L)).thenReturn(new BigDecimal("10.50"));

        BigDecimal result = service.getCompanyAverageTransportRevenue(1L);

        assertEquals(0, new BigDecimal("10.50").compareTo(result));
        verify(report).getCompanyAverageTransportRevenue(1L);
        verifyNoMoreInteractions(report);
        verifyNoInteractions(crud, filter, sorting);
    }
}
