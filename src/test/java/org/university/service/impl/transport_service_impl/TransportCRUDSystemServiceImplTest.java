package org.university.service.impl.transport_service_impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.university.dao.*;
import org.university.dto.TransportDto;
import org.university.entity.*;
import org.university.exception.DAOException;
import org.university.util.CargoType;
import org.university.util.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransportCRUDSystemServiceImplTest {

    @Mock TransportDao transportDao;
    @Mock VehicleDao vehicleDao;
    @Mock CompanyDao companyDao;
    @Mock EmployeeDao employeeDao;
    @Mock CustomerDao customerDao;

    TransportCRUDSystemServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new TransportCRUDSystemServiceImpl(transportDao, vehicleDao, companyDao, employeeDao, customerDao);
    }

    @Test
    void mapToEntity_mapsFields_withoutRelations_whenIdsNull() {
        TransportDto dto = new TransportDto();
        dto.setStartPoint("S");
        dto.setEndPoint("E");
        dto.setDepartureDate(LocalDate.of(2025, 1, 1));
        dto.setArrivalDate(LocalDate.of(2025, 1, 2));
        dto.setCargoType(CargoType.GOODS);
        dto.setQuantity(new BigDecimal("5.00"));
        dto.setInitPrice(new BigDecimal("10.00"));
        dto.setPaymentStatus(PaymentStatus.NOT_PAID);
        dto.setTotalPrice(new BigDecimal("50.00"));

        Transport t = service.mapToEntity(dto);

        assertEquals("S", t.getStartPoint());
        assertEquals("E", t.getEndPoint());
        assertEquals(LocalDate.of(2025, 1, 1), t.getDepartureDate());
        assertEquals(LocalDate.of(2025, 1, 2), t.getArrivalDate());
        assertEquals(CargoType.GOODS, t.getCargoType());
        assertEquals(new BigDecimal("5.00"), t.getQuantity());
        assertEquals(new BigDecimal("10.00"), t.getInitPrice());
        assertEquals(PaymentStatus.NOT_PAID, t.getPaymentStatus());
        assertEquals(new BigDecimal("50.00"), t.getTotalPrice());

        assertNull(t.getCompany());
        assertNull(t.getVehicle());
        assertNull(t.getEmployee());
        assertNull(t.getCustomer());

        verifyNoInteractions(companyDao, vehicleDao, employeeDao, customerDao);
    }

    @Test
    void mapToEntity_setsRelations_whenIdsProvided() {
        Company c = Company.builder().name("C").revenue(BigDecimal.ZERO).build();
        c.setId(1L);

        Vehicle v = Vehicle.builder().vehicleType(null).distanceTraveled(BigDecimal.ZERO).build();
        v.setId(2L);

        Employee e = new Employee();
        e.setId(3L);

        Customer cu = new Customer();
        cu.setId(4L);

        when(companyDao.getCompanyById(1L)).thenReturn(c);
        when(vehicleDao.getVehicleById(2L)).thenReturn(v);
        when(employeeDao.getEmployeeById(3L)).thenReturn(e);
        when(customerDao.getCustomerById(4L)).thenReturn(cu);

        TransportDto dto = new TransportDto();
        dto.setStartPoint("S");
        dto.setEndPoint("E");
        dto.setDepartureDate(LocalDate.of(2025, 1, 1));
        dto.setArrivalDate(LocalDate.of(2025, 1, 2));
        dto.setCargoType(CargoType.ADR);
        dto.setQuantity(new BigDecimal("1.00"));
        dto.setInitPrice(new BigDecimal("2.00"));
        dto.setPaymentStatus(PaymentStatus.PAID);
        dto.setTotalPrice(new BigDecimal("2.00"));
        dto.setCompanyId(1L);
        dto.setVehicleId(2L);
        dto.setEmployeeId(3L);
        dto.setCustomerId(4L);

        Transport t = service.mapToEntity(dto);

        assertSame(c, t.getCompany());
        assertSame(v, t.getVehicle());
        assertSame(e, t.getEmployee());
        assertSame(cu, t.getCustomer());
    }

    @Test
    void mapToEntity_throws_whenCompanyMissing() {
        when(companyDao.getCompanyById(1L)).thenReturn(null);

        TransportDto dto = new TransportDto();
        dto.setStartPoint("S");
        dto.setEndPoint("E");
        dto.setDepartureDate(LocalDate.of(2025, 1, 1));
        dto.setArrivalDate(LocalDate.of(2025, 1, 2));
        dto.setCargoType(CargoType.GOODS);
        dto.setQuantity(BigDecimal.ONE);
        dto.setInitPrice(BigDecimal.ONE);
        dto.setPaymentStatus(PaymentStatus.NOT_PAID);
        dto.setTotalPrice(BigDecimal.ONE);
        dto.setCompanyId(1L);

        assertThrows(DAOException.class, () -> service.mapToEntity(dto));
    }

    @Test
    void mapToEntity_throws_whenVehicleMissing() {
        Company c = Company.builder().name("C").revenue(BigDecimal.ZERO).build();
        c.setId(1L);

        when(companyDao.getCompanyById(1L)).thenReturn(c);
        when(vehicleDao.getVehicleById(2L)).thenReturn(null);

        TransportDto dto = new TransportDto();
        dto.setStartPoint("S");
        dto.setEndPoint("E");
        dto.setDepartureDate(LocalDate.of(2025, 1, 1));
        dto.setArrivalDate(LocalDate.of(2025, 1, 2));
        dto.setCargoType(CargoType.GOODS);
        dto.setQuantity(BigDecimal.ONE);
        dto.setInitPrice(BigDecimal.ONE);
        dto.setPaymentStatus(PaymentStatus.NOT_PAID);
        dto.setTotalPrice(BigDecimal.ONE);
        dto.setCompanyId(1L);
        dto.setVehicleId(2L);

        assertThrows(DAOException.class, () -> service.mapToEntity(dto));
    }

    @Test
    void mapToEntity_throws_whenEmployeeMissing() {
        Company c = Company.builder().name("C").revenue(BigDecimal.ZERO).build();
        c.setId(1L);

        Vehicle v = Vehicle.builder().vehicleType(null).distanceTraveled(BigDecimal.ZERO).build();
        v.setId(2L);

        when(companyDao.getCompanyById(1L)).thenReturn(c);
        when(vehicleDao.getVehicleById(2L)).thenReturn(v);
        when(employeeDao.getEmployeeById(3L)).thenReturn(null);

        TransportDto dto = new TransportDto();
        dto.setStartPoint("S");
        dto.setEndPoint("E");
        dto.setDepartureDate(LocalDate.of(2025, 1, 1));
        dto.setArrivalDate(LocalDate.of(2025, 1, 2));
        dto.setCargoType(CargoType.GOODS);
        dto.setQuantity(BigDecimal.ONE);
        dto.setInitPrice(BigDecimal.ONE);
        dto.setPaymentStatus(PaymentStatus.NOT_PAID);
        dto.setTotalPrice(BigDecimal.ONE);
        dto.setCompanyId(1L);
        dto.setVehicleId(2L);
        dto.setEmployeeId(3L);

        assertThrows(DAOException.class, () -> service.mapToEntity(dto));
    }

    @Test
    void mapToEntity_throws_whenCustomerMissing() {
        Company c = Company.builder().name("C").revenue(BigDecimal.ZERO).build();
        c.setId(1L);

        Vehicle v = Vehicle.builder().vehicleType(null).distanceTraveled(BigDecimal.ZERO).build();
        v.setId(2L);

        Employee e = new Employee();
        e.setId(3L);

        when(companyDao.getCompanyById(1L)).thenReturn(c);
        when(vehicleDao.getVehicleById(2L)).thenReturn(v);
        when(employeeDao.getEmployeeById(3L)).thenReturn(e);
        when(customerDao.getCustomerById(4L)).thenReturn(null);

        TransportDto dto = new TransportDto();
        dto.setStartPoint("S");
        dto.setEndPoint("E");
        dto.setDepartureDate(LocalDate.of(2025, 1, 1));
        dto.setArrivalDate(LocalDate.of(2025, 1, 2));
        dto.setCargoType(CargoType.GOODS);
        dto.setQuantity(BigDecimal.ONE);
        dto.setInitPrice(BigDecimal.ONE);
        dto.setPaymentStatus(PaymentStatus.NOT_PAID);
        dto.setTotalPrice(BigDecimal.ONE);
        dto.setCompanyId(1L);
        dto.setVehicleId(2L);
        dto.setEmployeeId(3L);
        dto.setCustomerId(4L);

        assertThrows(DAOException.class, () -> service.mapToEntity(dto));
    }

    @Test
    void mapToDto_mapsFields_andSetsRelationIds() {
        Company c = Company.builder().name("C").revenue(BigDecimal.ZERO).build();
        c.setId(1L);

        Vehicle v = Vehicle.builder().vehicleType(null).distanceTraveled(BigDecimal.ZERO).build();
        v.setId(2L);

        Employee e = new Employee();
        e.setId(3L);

        Customer cu = new Customer();
        cu.setId(4L);

        Transport t = new Transport();
        t.setId(10L);
        t.setStartPoint("S");
        t.setEndPoint("E");
        t.setDepartureDate(LocalDate.of(2025, 1, 1));
        t.setArrivalDate(LocalDate.of(2025, 1, 2));
        t.setCargoType(CargoType.PASSENGERS);
        t.setQuantity(new BigDecimal("7.00"));
        t.setInitPrice(new BigDecimal("3.00"));
        t.setPaymentStatus(PaymentStatus.CANCELED);
        t.setTotalPrice(new BigDecimal("21.00"));
        t.setCompany(c);
        t.setVehicle(v);
        t.setEmployee(e);
        t.setCustomer(cu);

        TransportDto dto = service.mapToDto(t);

        assertEquals(10L, dto.getId());
        assertEquals("S", dto.getStartPoint());
        assertEquals("E", dto.getEndPoint());
        assertEquals(LocalDate.of(2025, 1, 1), dto.getDepartureDate());
        assertEquals(LocalDate.of(2025, 1, 2), dto.getArrivalDate());
        assertEquals(CargoType.PASSENGERS, dto.getCargoType());
        assertEquals(new BigDecimal("7.00"), dto.getQuantity());
        assertEquals(new BigDecimal("3.00"), dto.getInitPrice());
        assertEquals(PaymentStatus.CANCELED, dto.getPaymentStatus());
        assertEquals(new BigDecimal("21.00"), dto.getTotalPrice());

        assertEquals(1L, dto.getCompanyId());
        assertEquals(2L, dto.getVehicleId());
        assertEquals(3L, dto.getEmployeeId());
        assertEquals(4L, dto.getCustomerId());
    }

    @Test
    void createTransport_shouldCreate() throws DAOException {
        Company c = Company.builder().name("C").revenue(BigDecimal.ZERO).build();
        c.setId(1L);

        Vehicle v = Vehicle.builder().vehicleType(null).distanceTraveled(BigDecimal.ZERO).build();
        v.setId(2L);

        Employee e = new Employee();
        e.setId(3L);

        Customer cu = new Customer();
        cu.setId(4L);

        when(companyDao.getCompanyById(1L)).thenReturn(c);
        when(vehicleDao.getVehicleById(2L)).thenReturn(v);
        when(employeeDao.getEmployeeById(3L)).thenReturn(e);
        when(customerDao.getCustomerById(4L)).thenReturn(cu);

        TransportDto dto = new TransportDto();
        dto.setStartPoint("S");
        dto.setEndPoint("E");
        dto.setDepartureDate(LocalDate.of(2025, 1, 1));
        dto.setArrivalDate(LocalDate.of(2025, 1, 2));
        dto.setCargoType(CargoType.GOODS);
        dto.setQuantity(new BigDecimal("2.00"));
        dto.setInitPrice(new BigDecimal("10.00"));
        dto.setPaymentStatus(PaymentStatus.NOT_PAID);
        dto.setTotalPrice(new BigDecimal("20.00"));
        dto.setCompanyId(1L);
        dto.setVehicleId(2L);
        dto.setEmployeeId(3L);
        dto.setCustomerId(4L);

        TransportDto result = service.createTransport(dto);

        ArgumentCaptor<Transport> captor = ArgumentCaptor.forClass(Transport.class);
        verify(transportDao).createTransport(captor.capture());

        Transport created = captor.getValue();
        assertEquals("S", created.getStartPoint());
        assertEquals("E", created.getEndPoint());
        assertSame(c, created.getCompany());
        assertSame(v, created.getVehicle());
        assertSame(e, created.getEmployee());
        assertSame(cu, created.getCustomer());

        assertEquals("S", result.getStartPoint());
        assertEquals("E", result.getEndPoint());
        assertEquals(1L, result.getCompanyId());
        assertEquals(2L, result.getVehicleId());
        assertEquals(3L, result.getEmployeeId());
        assertEquals(4L, result.getCustomerId());
    }

    @Test
    void getTransportById_shouldThrow_whenMissing() {
        when(transportDao.getTransportById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.getTransportById(1L));
    }

    @Test
    void getTransportById_shouldReturnDto() throws DAOException {
        Transport t = new Transport();
        t.setId(1L);
        t.setStartPoint("S");
        t.setEndPoint("E");
        t.setDepartureDate(LocalDate.of(2025, 1, 1));
        t.setArrivalDate(LocalDate.of(2025, 1, 2));
        t.setCargoType(CargoType.GOODS);
        t.setQuantity(BigDecimal.ONE);
        t.setInitPrice(BigDecimal.ONE);
        t.setPaymentStatus(PaymentStatus.NOT_PAID);
        t.setTotalPrice(BigDecimal.ONE);

        when(transportDao.getTransportById(1L)).thenReturn(t);

        TransportDto dto = service.getTransportById(1L);

        assertEquals(1L, dto.getId());
        assertEquals("S", dto.getStartPoint());
        assertEquals("E", dto.getEndPoint());
    }

    @Test
    void getAllTransports() {
        Transport t1 = new Transport();
        t1.setId(1L);
        t1.setStartPoint("A");
        t1.setEndPoint("B");
        t1.setDepartureDate(LocalDate.of(2025, 1, 1));
        t1.setArrivalDate(LocalDate.of(2025, 1, 2));
        t1.setCargoType(CargoType.GOODS);
        t1.setQuantity(BigDecimal.ONE);
        t1.setInitPrice(BigDecimal.ONE);
        t1.setPaymentStatus(PaymentStatus.NOT_PAID);
        t1.setTotalPrice(BigDecimal.ONE);

        Transport t2 = new Transport();
        t2.setId(2L);
        t2.setStartPoint("C");
        t2.setEndPoint("D");
        t2.setDepartureDate(LocalDate.of(2025, 2, 1));
        t2.setArrivalDate(LocalDate.of(2025, 2, 2));
        t2.setCargoType(CargoType.ADR);
        t2.setQuantity(BigDecimal.ONE);
        t2.setInitPrice(BigDecimal.ONE);
        t2.setPaymentStatus(PaymentStatus.PAID);
        t2.setTotalPrice(BigDecimal.ONE);

        when(transportDao.getAllTransports()).thenReturn(List.of(t1, t2));

        Set<TransportDto> result = service.getAllTransports();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(d -> d.getId().equals(1L) && d.getStartPoint().equals("A")));
        assertTrue(result.stream().anyMatch(d -> d.getId().equals(2L) && d.getStartPoint().equals("C")));
    }

    @Test
    void updateTransport_shouldThrow_whenMissing() {
        when(transportDao.getTransportById(1L)).thenReturn(null);

        TransportDto dto = new TransportDto();
        dto.setStartPoint("S");
        dto.setEndPoint("E");
        dto.setDepartureDate(LocalDate.of(2025, 1, 1));
        dto.setArrivalDate(LocalDate.of(2025, 1, 2));
        dto.setCargoType(CargoType.GOODS);
        dto.setQuantity(BigDecimal.ONE);
        dto.setInitPrice(BigDecimal.ONE);
        dto.setPaymentStatus(PaymentStatus.NOT_PAID);
        dto.setTotalPrice(BigDecimal.ONE);

        assertThrows(DAOException.class, () -> service.updateTransport(1L, dto));
        verify(transportDao, never()).updateTransport(anyLong(), any());
    }

    @Test
    void updateTransport_shouldDetachRelations_whenDtoIdsNull() throws DAOException {
        Company c = Company.builder().name("C").revenue(BigDecimal.ZERO).build();
        c.setId(1L);

        Vehicle v = Vehicle.builder().vehicleType(null).distanceTraveled(BigDecimal.ZERO).build();
        v.setId(2L);

        Employee e = new Employee();
        e.setId(3L);

        Customer cu = new Customer();
        cu.setId(4L);

        Transport existing = new Transport();
        existing.setId(10L);
        existing.setCompany(c);
        existing.setVehicle(v);
        existing.setEmployee(e);
        existing.setCustomer(cu);

        when(transportDao.getTransportById(10L)).thenReturn(existing);

        TransportDto dto = new TransportDto();
        dto.setStartPoint("S");
        dto.setEndPoint("E");
        dto.setDepartureDate(LocalDate.of(2025, 1, 1));
        dto.setArrivalDate(LocalDate.of(2025, 1, 2));
        dto.setCargoType(CargoType.GOODS);
        dto.setQuantity(new BigDecimal("2.00"));
        dto.setInitPrice(new BigDecimal("3.00"));
        dto.setPaymentStatus(PaymentStatus.PAID);
        dto.setTotalPrice(new BigDecimal("6.00"));
        dto.setCompanyId(null);
        dto.setVehicleId(null);
        dto.setEmployeeId(null);
        dto.setCustomerId(null);

        TransportDto result = service.updateTransport(10L, dto);

        assertNull(existing.getCompany());
        assertNull(existing.getVehicle());
        assertNull(existing.getEmployee());
        assertNull(existing.getCustomer());

        assertEquals(10L, result.getId());
        assertNull(result.getCompanyId());
        assertNull(result.getVehicleId());
        assertNull(result.getEmployeeId());
        assertNull(result.getCustomerId());

        verify(transportDao).updateTransport(eq(10L), same(existing));
    }

    @Test
    void updateTransport_shouldSetRelations_whenDtoIdsProvided() throws DAOException {
        Transport existing = new Transport();
        existing.setId(10L);

        Company c = Company.builder().name("C").revenue(BigDecimal.ZERO).build();
        c.setId(1L);

        Vehicle v = Vehicle.builder().vehicleType(null).distanceTraveled(BigDecimal.ZERO).build();
        v.setId(2L);

        Employee e = new Employee();
        e.setId(3L);

        Customer cu = new Customer();
        cu.setId(4L);

        when(transportDao.getTransportById(10L)).thenReturn(existing);
        when(companyDao.getCompanyById(1L)).thenReturn(c);
        when(vehicleDao.getVehicleById(2L)).thenReturn(v);
        when(employeeDao.getEmployeeById(3L)).thenReturn(e);
        when(customerDao.getCustomerById(4L)).thenReturn(cu);

        TransportDto dto = new TransportDto();
        dto.setStartPoint("S");
        dto.setEndPoint("E");
        dto.setDepartureDate(LocalDate.of(2025, 1, 1));
        dto.setArrivalDate(LocalDate.of(2025, 1, 2));
        dto.setCargoType(CargoType.ADR);
        dto.setQuantity(new BigDecimal("1.00"));
        dto.setInitPrice(new BigDecimal("2.00"));
        dto.setPaymentStatus(PaymentStatus.NOT_PAID);
        dto.setTotalPrice(new BigDecimal("2.00"));
        dto.setCompanyId(1L);
        dto.setVehicleId(2L);
        dto.setEmployeeId(3L);
        dto.setCustomerId(4L);

        TransportDto result = service.updateTransport(10L, dto);

        assertSame(c, existing.getCompany());
        assertSame(v, existing.getVehicle());
        assertSame(e, existing.getEmployee());
        assertSame(cu, existing.getCustomer());

        assertEquals(1L, result.getCompanyId());
        assertEquals(2L, result.getVehicleId());
        assertEquals(3L, result.getEmployeeId());
        assertEquals(4L, result.getCustomerId());

        verify(transportDao).updateTransport(eq(10L), same(existing));
    }

    @Test
    void deleteTransport_shouldThrow_whenMissing() {
        when(transportDao.getTransportById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.deleteTransport(1L));
        verify(transportDao, never()).deleteTransport(anyLong());
    }

    @Test
    void deleteTransport_shouldDelete() throws DAOException {
        Transport existing = new Transport();
        existing.setId(1L);

        when(transportDao.getTransportById(1L)).thenReturn(existing);

        service.deleteTransport(1L);

        verify(transportDao).deleteTransport(1L);
    }

    @Test
    void getCompanyIdForTransport_shouldThrow_whenTransportMissing() {
        when(transportDao.getTransportById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.getCompanyIdForTransport(1L));
    }

    @Test
    void getCompanyIdForTransport_shouldThrow_whenNoCompany() {
        Transport t = new Transport();
        t.setId(1L);
        t.setCompany(null);

        when(transportDao.getTransportById(1L)).thenReturn(t);

        assertThrows(DAOException.class, () -> service.getCompanyIdForTransport(1L));
    }

    @Test
    void getCompanyIdForTransport_shouldReturnCompanyId() throws DAOException {
        Company c = Company.builder().name("C").revenue(BigDecimal.ZERO).build();
        c.setId(10L);

        Transport t = new Transport();
        t.setId(1L);
        t.setCompany(c);

        when(transportDao.getTransportById(1L)).thenReturn(t);

        assertEquals(10L, service.getCompanyIdForTransport(1L));
    }

    @Test
    void getEmployeeIdForTransport_shouldThrow_whenTransportMissing() {
        when(transportDao.getTransportById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.getEmployeeIdForTransport(1L));
    }

    @Test
    void getEmployeeIdForTransport_shouldThrow_whenNoEmployee() {
        Transport t = new Transport();
        t.setId(1L);
        t.setEmployee(null);

        when(transportDao.getTransportById(1L)).thenReturn(t);

        assertThrows(DAOException.class, () -> service.getEmployeeIdForTransport(1L));
    }

    @Test
    void getEmployeeIdForTransport_shouldReturnEmployeeId() throws DAOException {
        Employee e = new Employee();
        e.setId(10L);

        Transport t = new Transport();
        t.setId(1L);
        t.setEmployee(e);

        when(transportDao.getTransportById(1L)).thenReturn(t);

        assertEquals(10L, service.getEmployeeIdForTransport(1L));
    }

    @Test
    void getCustomerIdForTransport_shouldThrow_whenTransportMissing() {
        when(transportDao.getTransportById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.getCustomerIdForTransport(1L));
    }

    @Test
    void getCustomerIdForTransport_shouldThrow_whenNoCustomer() {
        Transport t = new Transport();
        t.setId(1L);
        t.setCustomer(null);

        when(transportDao.getTransportById(1L)).thenReturn(t);

        assertThrows(DAOException.class, () -> service.getCustomerIdForTransport(1L));
    }

    @Test
    void getCustomerIdForTransport_shouldReturnCustomerId() throws DAOException {
        Customer c = new Customer();
        c.setId(10L);

        Transport t = new Transport();
        t.setId(1L);
        t.setCustomer(c);

        when(transportDao.getTransportById(1L)).thenReturn(t);

        assertEquals(10L, service.getCustomerIdForTransport(1L));
    }

    @Test
    void getVehicleIdForTransport_shouldThrow_whenTransportMissing() {
        when(transportDao.getTransportById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.getVehicleIdForTransport(1L));
    }

    @Test
    void getVehicleIdForTransport_shouldThrow_whenNoVehicle() {
        Transport t = new Transport();
        t.setId(1L);
        t.setVehicle(null);

        when(transportDao.getTransportById(1L)).thenReturn(t);

        assertThrows(DAOException.class, () -> service.getVehicleIdForTransport(1L));
    }

    @Test
    void getVehicleIdForTransport_shouldReturnVehicleId() throws DAOException {
        Vehicle v = Vehicle.builder().vehicleType(null).distanceTraveled(BigDecimal.ZERO).build();
        v.setId(10L);

        Transport t = new Transport();
        t.setId(1L);
        t.setVehicle(v);

        when(transportDao.getTransportById(1L)).thenReturn(t);

        assertEquals(10L, service.getVehicleIdForTransport(1L));
    }
}
