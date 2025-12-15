package org.university.service.impl.employee_service_impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.university.dao.*;
import org.university.dto.EmployeeDto;
import org.university.entity.*;
import org.university.exception.DAOException;
import org.university.util.DriverQualifications;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeCRUDServiceImplTest {

    @Mock EmployeeDao employeeDao;
    @Mock DrivingLicenseDao drivingLicenseDao;
    @Mock VehicleDao vehicleDao;
    @Mock TransportDao transportDao;
    @Mock CompanyDao companyDao;

    EmployeeCRUDServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new EmployeeCRUDServiceImpl(employeeDao, drivingLicenseDao, vehicleDao, transportDao, companyDao);
    }

    @Test
    void mapToEntity() {
        EmployeeDto dto = new EmployeeDto();
        dto.setFirstName("Ivan");
        dto.setLastName("Ivanov");
        dto.setSalary(new BigDecimal("1000.00"));
        dto.setBirthDate(LocalDate.of(2000, 1, 1));
        dto.setDriverQualifications(Set.of(DriverQualifications.CARGO));

        Employee e = service.mapToEntity(dto);

        assertEquals("Ivan", e.getFirstName());
        assertEquals("Ivanov", e.getLastName());
        assertEquals(new BigDecimal("1000.00"), e.getSalary());
        assertEquals(LocalDate.of(2000, 1, 1), e.getBirthDate());
        assertEquals(Set.of(DriverQualifications.CARGO), e.getDriverQualifications());
    }

    @Test
    void mapToDto_setsCompanyIdAndDrivingLicenseId_whenPresent() {
        Company company = Company.builder().name("C").revenue(BigDecimal.ZERO).build();
        company.setId(10L);

        DrivingLicense dl = DrivingLicense.builder()
                .drivingLicenseNumber("DL")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .drivingLicenseCategories(Set.of())
                .build();
        dl.setId(20L);

        Employee e = new Employee();
        e.setId(1L);
        e.setFirstName("Ivan");
        e.setLastName("Ivanov");
        e.setSalary(new BigDecimal("1000.00"));
        e.setBirthDate(LocalDate.of(2000, 1, 1));
        e.setDriverQualifications(Set.of(DriverQualifications.CARGO));
        e.setCompany(company);
        e.setDrivingLicense(dl);

        EmployeeDto dto = service.mapToDto(e);

        assertEquals(1L, dto.getId());
        assertEquals("Ivan", dto.getFirstName());
        assertEquals("Ivanov", dto.getLastName());
        assertEquals(new BigDecimal("1000.00"), dto.getSalary());
        assertEquals(LocalDate.of(2000, 1, 1), dto.getBirthDate());
        assertEquals(Set.of(DriverQualifications.CARGO), dto.getDriverQualifications());
        assertEquals(10L, dto.getCompanyId());
        assertEquals(20L, dto.getDrivingLicenseId());
    }

    @Test
    void mapToDto_leavesCompanyIdAndDrivingLicenseIdNull_whenMissing() {
        Employee e = new Employee();
        e.setId(1L);
        e.setFirstName("Ivan");
        e.setLastName("Ivanov");
        e.setSalary(new BigDecimal("1000.00"));
        e.setBirthDate(LocalDate.of(2000, 1, 1));
        e.setDriverQualifications(Set.of(DriverQualifications.CARGO));
        e.setCompany(null);
        e.setDrivingLicense(null);

        EmployeeDto dto = service.mapToDto(e);

        assertNull(dto.getCompanyId());
        assertNull(dto.getDrivingLicenseId());
    }

    @Test
    void createEmployee_shouldCreate() throws DAOException {
        EmployeeDto dto = new EmployeeDto();
        dto.setFirstName("Ivan");
        dto.setLastName("Ivanov");
        dto.setSalary(new BigDecimal("1000.00"));
        dto.setBirthDate(LocalDate.of(2000, 1, 1));
        dto.setDriverQualifications(Set.of(DriverQualifications.CARGO));

        doNothing().when(employeeDao).createEmployee(any(Employee.class));

        EmployeeDto result = service.createEmployee(dto);

        assertNull(result.getId());
        assertEquals("Ivan", result.getFirstName());
        verify(employeeDao).createEmployee(any(Employee.class));
    }

    @Test
    void getEmployeeById_shouldThrow_whenMissing() {
        when(employeeDao.getEmployeeById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.getEmployeeById(1L));
    }

    @Test
    void getEmployeeById_shouldReturnDto() throws DAOException {
        Employee e = new Employee();
        e.setId(1L);
        e.setFirstName("Ivan");
        e.setLastName("Ivanov");
        e.setSalary(new BigDecimal("1000.00"));
        e.setBirthDate(LocalDate.of(2000, 1, 1));
        e.setDriverQualifications(Set.of(DriverQualifications.CARGO));

        when(employeeDao.getEmployeeById(1L)).thenReturn(e);

        EmployeeDto dto = service.getEmployeeById(1L);

        assertEquals(1L, dto.getId());
        assertEquals("Ivan", dto.getFirstName());
        assertEquals(new BigDecimal("1000.00"), dto.getSalary());
        assertEquals(Set.of(DriverQualifications.CARGO), dto.getDriverQualifications());
    }

    @Test
    void getAllEmployees() {
        Employee e1 = new Employee();
        e1.setId(1L);
        e1.setFirstName("A");
        e1.setLastName("A");
        e1.setBirthDate(LocalDate.of(2000, 1, 1));
        e1.setSalary(BigDecimal.ZERO);
        e1.setDriverQualifications(Set.of(DriverQualifications.CARGO));

        Employee e2 = new Employee();
        e2.setId(2L);
        e2.setFirstName("B");
        e2.setLastName("B");
        e2.setBirthDate(LocalDate.of(2000, 1, 1));
        e2.setSalary(BigDecimal.ONE);
        e2.setDriverQualifications(Set.of(DriverQualifications.ADR));

        when(employeeDao.getAllEmployees()).thenReturn(List.of(e1, e2));

        Set<EmployeeDto> result = service.getAllEmployees();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(d -> d.getId().equals(1L) && d.getFirstName().equals("A")));
        assertTrue(result.stream().anyMatch(d -> d.getId().equals(2L) && d.getFirstName().equals("B")));
    }

    @Test
    void updateEmployee_shouldThrow_whenMissing() {
        when(employeeDao.getEmployeeById(1L)).thenReturn(null);

        EmployeeDto dto = new EmployeeDto();
        dto.setFirstName("New");
        dto.setLastName("Name");
        dto.setSalary(new BigDecimal("999.00"));
        dto.setBirthDate(LocalDate.of(2001, 1, 1));
        dto.setDriverQualifications(Set.of(DriverQualifications.ADR));

        assertThrows(DAOException.class, () -> service.updateEmployee(1L, dto));
        verify(employeeDao, never()).updateEmployee(anyLong(), any());
    }

    @Test
    void updateEmployee_shouldUpdate() throws DAOException {
        Employee existing = new Employee();
        existing.setId(1L);
        existing.setFirstName("Old");
        existing.setLastName("Old");
        existing.setSalary(new BigDecimal("10.00"));
        existing.setBirthDate(LocalDate.of(2000, 1, 1));
        existing.setDriverQualifications(Set.of(DriverQualifications.CARGO));

        when(employeeDao.getEmployeeById(1L)).thenReturn(existing);

        EmployeeDto dto = new EmployeeDto();
        dto.setFirstName("New");
        dto.setLastName("Name");
        dto.setSalary(new BigDecimal("999.00"));
        dto.setBirthDate(LocalDate.of(2001, 1, 1));
        dto.setDriverQualifications(Set.of(DriverQualifications.ADR));

        EmployeeDto result = service.updateEmployee(1L, dto);

        assertEquals(1L, result.getId());
        assertEquals("New", result.getFirstName());
        assertEquals(new BigDecimal("999.00"), result.getSalary());
        assertEquals(Set.of(DriverQualifications.ADR), result.getDriverQualifications());

        verify(employeeDao).updateEmployee(eq(1L), same(existing));
    }

    @Test
    void deleteEmployee_shouldThrow_whenMissing() {
        when(employeeDao.getEmployeeById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.deleteEmployee(1L));
        verify(employeeDao, never()).deleteEmployee(anyLong());
    }

    @Test
    void deleteEmployee_shouldDelete() throws DAOException {
        Employee existing = new Employee();
        existing.setId(1L);

        when(employeeDao.getEmployeeById(1L)).thenReturn(existing);

        service.deleteEmployee(1L);

        verify(employeeDao).deleteEmployee(1L);
    }

    @Test
    void getCompanyIdForEmployee_shouldThrow_whenEmployeeMissing() {
        when(employeeDao.getEmployeeById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.getCompanyIdForEmployee(1L));
    }

    @Test
    void getCompanyIdForEmployee_shouldReturnNull_whenNoCompany() throws DAOException {
        Employee e = new Employee();
        e.setId(1L);
        e.setCompany(null);

        when(employeeDao.getEmployeeById(1L)).thenReturn(e);

        assertNull(service.getCompanyIdForEmployee(1L));
    }

    @Test
    void getCompanyIdForEmployee_shouldReturnCompanyId_whenPresent() throws DAOException {
        Company company = Company.builder().name("C").revenue(BigDecimal.ZERO).build();
        company.setId(10L);

        Employee e = new Employee();
        e.setId(1L);
        e.setCompany(company);

        when(employeeDao.getEmployeeById(1L)).thenReturn(e);

        assertEquals(10L, service.getCompanyIdForEmployee(1L));
    }

    @Test
    void getDrivingLicenseIdForEmployee_shouldThrow_whenEmployeeMissing() {
        when(employeeDao.getEmployeeById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.getDrivingLicenseIdForEmployee(1L));
    }

    @Test
    void getDrivingLicenseIdForEmployee_shouldReturnNull_whenNoLicense() throws DAOException {
        Employee e = new Employee();
        e.setId(1L);
        e.setDrivingLicense(null);

        when(employeeDao.getEmployeeById(1L)).thenReturn(e);

        assertNull(service.getDrivingLicenseIdForEmployee(1L));
    }

    @Test
    void getDrivingLicenseIdForEmployee_shouldReturnLicenseId_whenPresent() throws DAOException {
        DrivingLicense dl = DrivingLicense.builder()
                .drivingLicenseNumber("DL")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .drivingLicenseCategories(Set.of())
                .build();
        dl.setId(20L);

        Employee e = new Employee();
        e.setId(1L);
        e.setDrivingLicense(dl);

        when(employeeDao.getEmployeeById(1L)).thenReturn(e);

        assertEquals(20L, service.getDrivingLicenseIdForEmployee(1L));
    }

    @Test
    void getAllVehicleIdsForEmployee_shouldThrow_whenEmployeeMissing() {
        when(employeeDao.getEmployeeById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.getAllVehicleIdsForEmployee(1L));
        verifyNoInteractions(vehicleDao);
    }

    @Test
    void getAllVehicleIdsForEmployee_shouldReturnMatchingIds() throws DAOException {
        Employee e = new Employee();
        e.setId(1L);

        Employee other = new Employee();
        other.setId(2L);

        Vehicle v1 = new Vehicle();
        v1.setId(10L);
        v1.setEmployee(e);

        Vehicle v2 = new Vehicle();
        v2.setId(11L);
        v2.setEmployee(null);

        Vehicle v3 = new Vehicle();
        v3.setId(12L);
        v3.setEmployee(other);

        when(employeeDao.getEmployeeById(1L)).thenReturn(e);
        when(vehicleDao.getAllVehicles()).thenReturn(List.of(v1, v2, v3));

        Set<Long> ids = service.getAllVehicleIdsForEmployee(1L);

        assertEquals(Set.of(10L), ids);
    }

    @Test
    void getAllTransportIdsForEmployee_shouldThrow_whenEmployeeMissing() {
        when(employeeDao.getEmployeeById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.getAllTransportIdsForEmployee(1L));
        verifyNoInteractions(transportDao);
    }

    @Test
    void getAllTransportIdsForEmployee_shouldReturnMatchingIds() throws DAOException {
        Employee e = new Employee();
        e.setId(1L);

        Employee other = new Employee();
        other.setId(2L);

        Transport t1 = new Transport();
        t1.setId(100L);
        t1.setEmployee(e);

        Transport t2 = new Transport();
        t2.setId(101L);
        t2.setEmployee(null);

        Transport t3 = new Transport();
        t3.setId(102L);
        t3.setEmployee(other);

        when(employeeDao.getEmployeeById(1L)).thenReturn(e);
        when(transportDao.getAllTransports()).thenReturn(List.of(t1, t2, t3));

        Set<Long> ids = service.getAllTransportIdsForEmployee(1L);

        assertEquals(Set.of(100L), ids);
    }
}
