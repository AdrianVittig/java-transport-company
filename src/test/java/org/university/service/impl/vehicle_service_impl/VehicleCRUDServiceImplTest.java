package org.university.service.impl.vehicle_service_impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.university.dao.CompanyDao;
import org.university.dao.EmployeeDao;
import org.university.dao.TransportDao;
import org.university.dao.VehicleDao;
import org.university.dto.VehicleDto;
import org.university.entity.Company;
import org.university.entity.Employee;
import org.university.entity.Transport;
import org.university.entity.Vehicle;
import org.university.exception.DAOException;
import org.university.util.VehicleType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleCRUDServiceImplTest {

    @Mock VehicleDao vehicleDao;
    @Mock CompanyDao companyDao;
    @Mock EmployeeDao employeeDao;
    @Mock TransportDao transportDao;

    VehicleCRUDServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new VehicleCRUDServiceImpl(vehicleDao, companyDao, employeeDao, transportDao);
    }

    @Test
    void mapToEntity_shouldMapBasicFields_whenNoRelations() {
        VehicleDto dto = new VehicleDto();
        dto.setVehicleType(VehicleType.CAR);
        dto.setDistanceTraveled(new BigDecimal("123.45"));
        dto.setCompanyId(null);
        dto.setEmployeeId(null);

        Vehicle v = service.mapToEntity(dto);

        assertEquals(VehicleType.CAR, v.getVehicleType());
        assertEquals(new BigDecimal("123.45"), v.getDistanceTraveled());
        assertNull(v.getCompany());
        assertNull(v.getEmployee());
        verifyNoInteractions(companyDao, employeeDao);
    }

    @Test
    void mapToEntity_shouldSetCompany_whenCompanyIdPresent() {
        Company c = Company.builder().name("C").revenue(BigDecimal.ZERO).build();
        c.setId(10L);

        when(companyDao.getCompanyById(10L)).thenReturn(c);

        VehicleDto dto = new VehicleDto();
        dto.setVehicleType(VehicleType.TRUCK);
        dto.setDistanceTraveled(BigDecimal.ZERO);
        dto.setCompanyId(10L);

        Vehicle v = service.mapToEntity(dto);

        assertEquals(10L, v.getCompany().getId());
        verify(companyDao).getCompanyById(10L);
    }

    @Test
    void mapToEntity_shouldThrow_whenCompanyIdPresentButMissing() {
        when(companyDao.getCompanyById(10L)).thenReturn(null);

        VehicleDto dto = new VehicleDto();
        dto.setVehicleType(VehicleType.TRUCK);
        dto.setDistanceTraveled(BigDecimal.ZERO);
        dto.setCompanyId(10L);

        assertThrows(DAOException.class, () -> service.mapToEntity(dto));
        verify(companyDao).getCompanyById(10L);
    }

    @Test
    void mapToEntity_shouldSetEmployee_whenEmployeeIdPresent() {
        Employee e = new Employee();
        e.setId(20L);

        when(employeeDao.getEmployeeById(20L)).thenReturn(e);

        VehicleDto dto = new VehicleDto();
        dto.setVehicleType(VehicleType.BUS);
        dto.setDistanceTraveled(BigDecimal.ONE);
        dto.setEmployeeId(20L);

        Vehicle v = service.mapToEntity(dto);

        assertEquals(20L, v.getEmployee().getId());
        verify(employeeDao).getEmployeeById(20L);
    }

    @Test
    void mapToEntity_shouldThrow_whenEmployeeIdPresentButMissing() {
        when(employeeDao.getEmployeeById(20L)).thenReturn(null);

        VehicleDto dto = new VehicleDto();
        dto.setVehicleType(VehicleType.BUS);
        dto.setDistanceTraveled(BigDecimal.ONE);
        dto.setEmployeeId(20L);

        assertThrows(DAOException.class, () -> service.mapToEntity(dto));
        verify(employeeDao).getEmployeeById(20L);
    }

    @Test
    void mapToDto_shouldMapIds_whenRelationsPresent() {
        Company c = Company.builder().name("C").revenue(BigDecimal.ZERO).build();
        c.setId(10L);

        Employee e = new Employee();
        e.setId(20L);

        Vehicle v = new Vehicle();
        v.setId(1L);
        v.setVehicleType(VehicleType.CISTERN);
        v.setDistanceTraveled(new BigDecimal("5.00"));
        v.setCompany(c);
        v.setEmployee(e);

        VehicleDto dto = service.mapToDto(v);

        assertEquals(1L, dto.getId());
        assertEquals(VehicleType.CISTERN, dto.getVehicleType());
        assertEquals(new BigDecimal("5.00"), dto.getDistanceTraveled());
        assertEquals(10L, dto.getCompanyId());
        assertEquals(20L, dto.getEmployeeId());
    }

    @Test
    void createVehicle_shouldCreateAndReturnDto() throws DAOException {
        VehicleDto dto = new VehicleDto();
        dto.setVehicleType(VehicleType.CAR);
        dto.setDistanceTraveled(new BigDecimal("1.00"));

        doNothing().when(vehicleDao).createVehicle(any(Vehicle.class));

        VehicleDto result = service.createVehicle(dto);

        assertNull(result.getId());
        assertEquals(VehicleType.CAR, result.getVehicleType());
        assertEquals(new BigDecimal("1.00"), result.getDistanceTraveled());
        verify(vehicleDao).createVehicle(any(Vehicle.class));
    }

    @Test
    void getVehicleById_shouldThrow_whenMissing() {
        when(vehicleDao.getVehicleById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.getVehicleById(1L));
    }

    @Test
    void getVehicleById_shouldReturnDto() throws DAOException {
        Vehicle v = new Vehicle();
        v.setId(1L);
        v.setVehicleType(VehicleType.TRUCK);
        v.setDistanceTraveled(BigDecimal.ZERO);

        when(vehicleDao.getVehicleById(1L)).thenReturn(v);

        VehicleDto dto = service.getVehicleById(1L);

        assertEquals(1L, dto.getId());
        assertEquals(VehicleType.TRUCK, dto.getVehicleType());
    }

    @Test
    void getAllVehicles() {
        Vehicle v1 = new Vehicle();
        v1.setId(1L);
        v1.setVehicleType(VehicleType.CAR);
        v1.setDistanceTraveled(BigDecimal.ZERO);

        Vehicle v2 = new Vehicle();
        v2.setId(2L);
        v2.setVehicleType(VehicleType.BUS);
        v2.setDistanceTraveled(BigDecimal.ONE);

        when(vehicleDao.getAllVehicles()).thenReturn(List.of(v1, v2));

        Set<VehicleDto> result = service.getAllVehicles();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(d -> d.getId().equals(1L) && d.getVehicleType() == VehicleType.CAR));
        assertTrue(result.stream().anyMatch(d -> d.getId().equals(2L) && d.getVehicleType() == VehicleType.BUS));
    }

    @Test
    void updateVehicle_shouldThrow_whenMissing() {
        when(vehicleDao.getVehicleById(1L)).thenReturn(null);

        VehicleDto dto = new VehicleDto();
        dto.setVehicleType(VehicleType.TRUCK);
        dto.setDistanceTraveled(BigDecimal.TEN);

        assertThrows(DAOException.class, () -> service.updateVehicle(1L, dto));
        verify(vehicleDao, never()).updateVehicle(anyLong(), any());
    }

    @Test
    void updateVehicle_shouldUpdateBasic_andNullOutRelations_whenIdsNull() throws DAOException {
        Vehicle existing = new Vehicle();
        existing.setId(1L);
        existing.setVehicleType(VehicleType.CAR);
        existing.setDistanceTraveled(BigDecimal.ZERO);
        existing.setCompany(Company.builder().name("C").revenue(BigDecimal.ZERO).build());
        existing.getCompany().setId(10L);
        Employee e = new Employee();
        e.setId(20L);
        existing.setEmployee(e);

        when(vehicleDao.getVehicleById(1L)).thenReturn(existing);

        VehicleDto dto = new VehicleDto();
        dto.setVehicleType(VehicleType.BUS);
        dto.setDistanceTraveled(new BigDecimal("9.00"));
        dto.setCompanyId(null);
        dto.setEmployeeId(null);

        VehicleDto result = service.updateVehicle(1L, dto);

        assertEquals(1L, result.getId());
        assertEquals(VehicleType.BUS, result.getVehicleType());
        assertEquals(new BigDecimal("9.00"), result.getDistanceTraveled());
        assertNull(existing.getCompany());
        assertNull(existing.getEmployee());

        verify(vehicleDao).updateVehicle(eq(1L), same(existing));
    }

    @Test
    void updateVehicle_shouldSetCompanyAndEmployee_whenIdsProvided() throws DAOException {
        Vehicle existing = new Vehicle();
        existing.setId(1L);

        Company c = Company.builder().name("C").revenue(BigDecimal.ZERO).build();
        c.setId(10L);

        Employee e = new Employee();
        e.setId(20L);

        when(vehicleDao.getVehicleById(1L)).thenReturn(existing);
        when(companyDao.getCompanyById(10L)).thenReturn(c);
        when(employeeDao.getEmployeeById(20L)).thenReturn(e);

        VehicleDto dto = new VehicleDto();
        dto.setVehicleType(VehicleType.CISTERN);
        dto.setDistanceTraveled(new BigDecimal("100.00"));
        dto.setCompanyId(10L);
        dto.setEmployeeId(20L);

        VehicleDto result = service.updateVehicle(1L, dto);

        assertEquals(VehicleType.CISTERN, result.getVehicleType());
        assertEquals(10L, result.getCompanyId());
        assertEquals(20L, result.getEmployeeId());
        assertEquals(c, existing.getCompany());
        assertEquals(e, existing.getEmployee());

        verify(vehicleDao).updateVehicle(eq(1L), same(existing));
    }

    @Test
    void deleteVehicle_shouldThrow_whenMissing() {
        when(vehicleDao.getVehicleById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.deleteVehicle(1L));
        verify(vehicleDao, never()).deleteVehicle(anyLong());
    }

    @Test
    void deleteVehicle_shouldDelete() throws DAOException {
        Vehicle existing = new Vehicle();
        existing.setId(1L);

        when(vehicleDao.getVehicleById(1L)).thenReturn(existing);

        service.deleteVehicle(1L);

        verify(vehicleDao).deleteVehicle(1L);
    }

    @Test
    void getCompanyIdForVehicle_shouldThrow_whenVehicleMissing() {
        when(vehicleDao.getVehicleById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.getCompanyIdForVehicle(1L));
    }

    @Test
    void getCompanyIdForVehicle_shouldThrow_whenNoCompany() {
        Vehicle v = new Vehicle();
        v.setId(1L);
        v.setCompany(null);

        when(vehicleDao.getVehicleById(1L)).thenReturn(v);

        assertThrows(DAOException.class, () -> service.getCompanyIdForVehicle(1L));
    }

    @Test
    void getCompanyIdForVehicle_shouldReturnCompanyId() throws DAOException {
        Company c = Company.builder().name("C").revenue(BigDecimal.ZERO).build();
        c.setId(10L);

        Vehicle v = new Vehicle();
        v.setId(1L);
        v.setCompany(c);

        when(vehicleDao.getVehicleById(1L)).thenReturn(v);

        assertEquals(10L, service.getCompanyIdForVehicle(1L));
    }

    @Test
    void getEmployeeIdForVehicle_shouldThrow_whenVehicleMissing() {
        when(vehicleDao.getVehicleById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.getEmployeeIdForVehicle(1L));
    }

    @Test
    void getEmployeeIdForVehicle_shouldThrow_whenNoEmployee() {
        Vehicle v = new Vehicle();
        v.setId(1L);
        v.setEmployee(null);

        when(vehicleDao.getVehicleById(1L)).thenReturn(v);

        assertThrows(DAOException.class, () -> service.getEmployeeIdForVehicle(1L));
    }

    @Test
    void getEmployeeIdForVehicle_shouldReturnEmployeeId() throws DAOException {
        Employee e = new Employee();
        e.setId(20L);

        Vehicle v = new Vehicle();
        v.setId(1L);
        v.setEmployee(e);

        when(vehicleDao.getVehicleById(1L)).thenReturn(v);

        assertEquals(20L, service.getEmployeeIdForVehicle(1L));
    }

    @Test
    void getAllTransportIdsForVehicle_shouldThrow_whenVehicleMissing() {
        when(vehicleDao.getVehicleById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.getAllTransportIdsForVehicle(1L));
        verifyNoInteractions(transportDao);
    }

    @Test
    void getAllTransportIdsForVehicle_shouldReturnMatchingIds() throws DAOException {
        Vehicle v = new Vehicle();
        v.setId(1L);

        Vehicle otherV = new Vehicle();
        otherV.setId(2L);

        Transport t1 = new Transport();
        t1.setId(100L);
        t1.setVehicle(v);

        Transport t2 = new Transport();
        t2.setId(101L);
        t2.setVehicle(null);

        Transport t3 = new Transport();
        t3.setId(102L);
        t3.setVehicle(otherV);

        when(vehicleDao.getVehicleById(1L)).thenReturn(v);
        when(transportDao.getAllTransports()).thenReturn(List.of(t1, t2, t3));

        Set<Long> ids = service.getAllTransportIdsForVehicle(1L);

        assertEquals(Set.of(100L), ids);
    }
}
