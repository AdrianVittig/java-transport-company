package org.university.service.impl.company_service_impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.university.dao.CompanyDao;
import org.university.dao.EmployeeDao;
import org.university.dao.TransportDao;
import org.university.dao.VehicleDao;
import org.university.dto.CompanyDto;
import org.university.entity.Company;
import org.university.entity.Employee;
import org.university.entity.Transport;
import org.university.entity.Vehicle;
import org.university.exception.DAOException;
import org.university.service.contract.company_service.CompanyCRUDService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyCRUDServiceImplTest {

    @Mock CompanyDao companyDao;
    @Mock VehicleDao vehicleDAO;
    @Mock EmployeeDao employeeDao;
    @Mock TransportDao transportDao;

    CompanyCRUDService service;

    @BeforeEach
    void setUp() {
        service = new CompanyCRUDServiceImpl(companyDao, employeeDao, vehicleDAO, transportDao);
    }

    @Test
    void mapToEntity() {
        CompanyDto dto = CompanyDto.builder()
                .name("ACME")
                .revenue(new BigDecimal("100.00"))
                .build();

        Company c = service.mapToEntity(dto);

        assertEquals("ACME", c.getName());
        assertEquals(new BigDecimal("100.00"), c.getRevenue());
        assertNotNull(c.getEmployeeSet());
        assertNotNull(c.getVehicleSet());
        assertNotNull(c.getTransportSet());
    }

    @Test
    void mapToDto() {
        Company c = Company.builder()
                .name("ACME")
                .revenue(new BigDecimal("200.00"))
                .build();
        c.setId(7L);

        CompanyDto dto = service.mapToDto(c);

        assertEquals(7L, dto.getId());
        assertEquals("ACME", dto.getName());
        assertEquals(new BigDecimal("200.00"), dto.getRevenue());
    }

    @Test
    void createCompany_shouldThrow_whenNameExists() {
        CompanyDto dto = CompanyDto.builder().name("ACME").revenue(BigDecimal.ZERO).build();
        when(companyDao.getAllCompanies()).thenReturn(List.of(Company.builder().name("ACME").build()));

        assertThrows(DAOException.class, () -> service.createCompany(dto));
        verify(companyDao, never()).createCompany(any());
    }

    @Test
    void createCompany_shouldCreate_whenNameDoesNotExist() throws DAOException {
        CompanyDto dto = CompanyDto.builder().name("ACME").revenue(new BigDecimal("10.00")).build();

        when(companyDao.getAllCompanies()).thenReturn(List.of());

        doAnswer(inv -> {
            Company created = inv.getArgument(0);
            created.setId(1L);
            return null;
        }).when(companyDao).createCompany(any(Company.class));

        CompanyDto result = service.createCompany(dto);

        assertEquals(1L, result.getId());
        assertEquals("ACME", result.getName());
        assertEquals(new BigDecimal("10.00"), result.getRevenue());
        verify(companyDao).createCompany(any(Company.class));
    }

    @Test
    void getCompanyById_shouldThrow_whenMissing() {
        when(companyDao.getCompanyById(5L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.getCompanyById(5L));
    }

    @Test
    void getCompanyById_shouldReturnDto() throws DAOException {
        Company c = Company.builder().name("ACME").revenue(BigDecimal.ZERO).build();
        c.setId(5L);
        when(companyDao.getCompanyById(5L)).thenReturn(c);

        CompanyDto dto = service.getCompanyById(5L);
        assertEquals(5L, dto.getId());
        assertEquals("ACME", dto.getName());
    }

    @Test
    void getAllCompanies() {
        Company c1 = Company.builder().name("A").revenue(BigDecimal.ZERO).build();
        c1.setId(1L);
        Company c2 = Company.builder().name("B").revenue(new BigDecimal("2.00")).build();
        c2.setId(2L);

        when(companyDao.getAllCompanies()).thenReturn(List.of(c1, c2));

        Set<CompanyDto> all = service.getAllCompanies();

        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(d -> d.getId().equals(1L) && d.getName().equals("A")));
        assertTrue(all.stream().anyMatch(d -> d.getId().equals(2L) && d.getName().equals("B")));
    }

    @Test
    void updateCompany_shouldThrow_whenMissing() {
        when(companyDao.getCompanyById(1L)).thenReturn(null);

        CompanyDto dto = CompanyDto.builder().name("NEW").revenue(BigDecimal.ONE).build();
        assertThrows(DAOException.class, () -> service.updateCompany(1L, dto));

        verify(companyDao, never()).updateCompany(anyLong(), any());
    }

    @Test
    void updateCompany_shouldUpdate() throws DAOException {
        Company existing = Company.builder().name("OLD").revenue(BigDecimal.ZERO).build();
        existing.setId(1L);

        when(companyDao.getCompanyById(1L)).thenReturn(existing);

        CompanyDto dto = CompanyDto.builder().name("NEW").revenue(new BigDecimal("9.99")).build();

        CompanyDto result = service.updateCompany(1L, dto);

        assertEquals(1L, result.getId());
        assertEquals("NEW", result.getName());
        assertEquals(new BigDecimal("9.99"), result.getRevenue());

        verify(companyDao).updateCompany(eq(1L), any(Company.class));
    }

    @Test
    void deleteCompany_shouldThrow_whenMissing() {
        when(companyDao.getCompanyById(1L)).thenReturn(null);

        assertThrows(DAOException.class, () -> service.deleteCompany(1L));
        verify(companyDao, never()).deleteCompany(anyLong());
    }

    @Test
    void deleteCompany_shouldDelete() throws DAOException {
        Company existing = Company.builder().name("X").revenue(BigDecimal.ZERO).build();
        existing.setId(1L);

        when(companyDao.getCompanyById(1L)).thenReturn(existing);

        service.deleteCompany(1L);

        verify(companyDao).deleteCompany(1L);
    }

    @Test
    void addEmployeeToCompany_shouldAssign() throws DAOException {
        Company company = Company.builder().name("C").revenue(BigDecimal.ZERO).build();
        company.setId(10L);

        Employee employee = new Employee();
        employee.setId(20L);
        employee.setFirstName("Ivan");
        employee.setLastName("Ivanov");
        employee.setBirthDate(LocalDate.of(2000, 1, 1));
        employee.setSalary(BigDecimal.ZERO);

        when(companyDao.getCompanyById(10L)).thenReturn(company);
        when(employeeDao.getEmployeeById(20L)).thenReturn(employee);

        service.addEmployeeToCompany(20L, 10L);

        assertNotNull(employee.getCompany());
        assertEquals(10L, employee.getCompany().getId());

        verify(employeeDao).updateEmployee(eq(20L), same(employee));
    }

    @Test
    void removeEmployeeFromCompany_shouldUnassign() throws DAOException {
        Company company = Company.builder().name("C").revenue(BigDecimal.ZERO).build();
        company.setId(10L);

        Employee employee = new Employee();
        employee.setId(20L);
        employee.setFirstName("Ivan");
        employee.setLastName("Ivanov");
        employee.setBirthDate(LocalDate.of(2000, 1, 1));
        employee.setSalary(BigDecimal.ZERO);

        employee.setCompany(company);

        when(companyDao.getCompanyById(10L)).thenReturn(company);
        when(employeeDao.getEmployeeById(20L)).thenReturn(employee);

        service.removeEmployeeFromCompany(20L, 10L);

        assertNull(employee.getCompany());
        verify(employeeDao).updateEmployee(eq(20L), same(employee));
    }

    @Test
    void getAllEmployeeIdsForCompany() throws DAOException {
        Company company = Company.builder().name("C").revenue(BigDecimal.ZERO).build();
        company.setId(10L);

        Employee e1 = new Employee();
        e1.setId(1L);
        e1.setFirstName("A");
        e1.setLastName("A");
        e1.setBirthDate(LocalDate.of(2000, 1, 1));
        e1.setCompany(company);

        Employee e2 = new Employee();
        e2.setId(2L);
        e2.setFirstName("B");
        e2.setLastName("B");
        e2.setBirthDate(LocalDate.of(2000, 1, 1));
        e2.setCompany(null);

        when(companyDao.getCompanyById(10L)).thenReturn(company);
        when(employeeDao.getAllEmployees()).thenReturn(List.of(e1, e2));

        Set<Long> ids = service.getAllEmployeeIdsForCompany(10L);

        assertEquals(Set.of(1L), ids);
    }

    @Test
    void addVehicleToCompany_shouldAssign() throws DAOException {
        Company company = Company.builder().name("C").revenue(BigDecimal.ZERO).build();
        company.setId(10L);

        Vehicle vehicle = new Vehicle();
        vehicle.setId(30L);
        vehicle.setCompany(null);

        when(companyDao.getCompanyById(10L)).thenReturn(company);
        when(vehicleDAO.getVehicleById(30L)).thenReturn(vehicle);

        service.addVehicleToCompany(10L, 30L);

        assertNotNull(vehicle.getCompany());
        assertEquals(10L, vehicle.getCompany().getId());

        verify(vehicleDAO).updateVehicle(eq(30L), same(vehicle));
    }

    @Test
    void removeVehicleFromCompany_shouldUnassign() throws DAOException {
        Company company = Company.builder().name("C").revenue(BigDecimal.ZERO).build();
        company.setId(10L);

        Vehicle vehicle = new Vehicle();
        vehicle.setId(30L);
        vehicle.setCompany(company);

        when(companyDao.getCompanyById(10L)).thenReturn(company);
        when(vehicleDAO.getVehicleById(30L)).thenReturn(vehicle);

        service.removeVehicleFromCompany(10L, 30L);

        assertNull(vehicle.getCompany());
        verify(vehicleDAO).updateVehicle(eq(30L), same(vehicle));
    }

    @Test
    void getAllVehicleIdsForCompany() throws DAOException {
        Company company = Company.builder().name("C").revenue(BigDecimal.ZERO).build();
        company.setId(10L);

        Vehicle v1 = new Vehicle();
        v1.setId(1L);
        v1.setCompany(company);

        Vehicle v2 = new Vehicle();
        v2.setId(2L);
        v2.setCompany(null);

        when(companyDao.getCompanyById(10L)).thenReturn(company);
        when(vehicleDAO.getAllVehicles()).thenReturn(List.of(v1, v2));

        Set<Long> ids = service.getAllVehicleIdsForCompany(10L);

        assertEquals(Set.of(1L), ids);
    }

    @Test
    void getAllTransportIdsForCompany() throws DAOException {
        Company company = Company.builder().name("C").revenue(BigDecimal.ZERO).build();
        company.setId(10L);

        Transport t1 = new Transport();
        t1.setId(1L);
        t1.setCompany(company);

        Transport t2 = new Transport();
        t2.setId(2L);
        t2.setCompany(null);

        when(companyDao.getCompanyById(10L)).thenReturn(company);
        when(transportDao.getAllTransports()).thenReturn(List.of(t1, t2));

        Set<Long> ids = service.getAllTransportIdsForCompany(10L);

        assertEquals(Set.of(1L), ids);
    }
}
