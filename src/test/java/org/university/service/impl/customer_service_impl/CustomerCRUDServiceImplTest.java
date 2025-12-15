package org.university.service.impl.customer_service_impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.university.dao.CustomerDao;
import org.university.dao.TransportDao;
import org.university.dto.CustomerDto;
import org.university.entity.Customer;
import org.university.entity.Transport;
import org.university.exception.DAOException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerCRUDServiceImplTest {

    @Mock CustomerDao customerDao;
    @Mock TransportDao transportDao;

    CustomerCRUDServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CustomerCRUDServiceImpl(customerDao, transportDao);
    }

    @Test
    void mapToEntity() {
        CustomerDto dto = new CustomerDto();
        dto.setFirstName("Ivan");
        dto.setLastName("Ivanov");
        dto.setBirthDate(LocalDate.of(2000, 1, 1));
        dto.setBudget(new BigDecimal("100.00"));

        Customer c = service.mapToEntity(dto);

        assertEquals("Ivan", c.getFirstName());
        assertEquals("Ivanov", c.getLastName());
        assertEquals(LocalDate.of(2000, 1, 1), c.getBirthDate());
        assertEquals(new BigDecimal("100.00"), c.getBudget());
    }

    @Test
    void mapToDto() {
        Customer c = new Customer();
        c.setId(7L);
        c.setFirstName("Ivan");
        c.setLastName("Ivanov");
        c.setBirthDate(LocalDate.of(2000, 1, 1));
        c.setBudget(new BigDecimal("100.00"));

        CustomerDto dto = service.mapToDto(c);

        assertEquals(7L, dto.getId());
        assertEquals("Ivan", dto.getFirstName());
        assertEquals("Ivanov", dto.getLastName());
        assertEquals(LocalDate.of(2000, 1, 1), dto.getBirthDate());
        assertEquals(new BigDecimal("100.00"), dto.getBudget());
    }

    @Test
    void createCustomer_shouldCreate() throws DAOException {
        CustomerDto dto = new CustomerDto();
        dto.setFirstName("Ivan");
        dto.setLastName("Ivanov");
        dto.setBirthDate(LocalDate.of(2000, 1, 1));
        dto.setBudget(new BigDecimal("100.00"));

        doAnswer(inv -> {
            Customer created = inv.getArgument(0);
            created.setId(1L);
            return null;
        }).when(customerDao).createCustomer(any(Customer.class));

        CustomerDto result = service.createCustomer(dto);

        assertEquals(1L, result.getId());
        assertEquals("Ivan", result.getFirstName());
        assertEquals("Ivanov", result.getLastName());
        assertEquals(LocalDate.of(2000, 1, 1), result.getBirthDate());
        assertEquals(new BigDecimal("100.00"), result.getBudget());
        verify(customerDao).createCustomer(any(Customer.class));
    }

    @Test
    void getCustomerById_shouldThrow_whenMissing() {
        when(customerDao.getCustomerById(5L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.getCustomerById(5L));
    }

    @Test
    void getCustomerById_shouldReturnDto() throws DAOException {
        Customer c = new Customer();
        c.setId(5L);
        c.setFirstName("Ivan");
        c.setLastName("Ivanov");
        c.setBirthDate(LocalDate.of(2000, 1, 1));
        c.setBudget(new BigDecimal("100.00"));

        when(customerDao.getCustomerById(5L)).thenReturn(c);

        CustomerDto dto = service.getCustomerById(5L);

        assertEquals(5L, dto.getId());
        assertEquals("Ivan", dto.getFirstName());
        assertEquals("Ivanov", dto.getLastName());
        assertEquals(LocalDate.of(2000, 1, 1), dto.getBirthDate());
        assertEquals(new BigDecimal("100.00"), dto.getBudget());
    }

    @Test
    void getAllCustomers() {
        Customer c1 = new Customer();
        c1.setId(1L);
        c1.setFirstName("A");
        c1.setLastName("A");
        c1.setBirthDate(LocalDate.of(2000, 1, 1));
        c1.setBudget(new BigDecimal("10.00"));

        Customer c2 = new Customer();
        c2.setId(2L);
        c2.setFirstName("B");
        c2.setLastName("B");
        c2.setBirthDate(LocalDate.of(2000, 1, 1));
        c2.setBudget(new BigDecimal("20.00"));

        when(customerDao.getAllCustomers()).thenReturn(List.of(c1, c2));

        Set<CustomerDto> result = service.getAllCustomers();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(d -> d.getId().equals(1L) && d.getFirstName().equals("A")));
        assertTrue(result.stream().anyMatch(d -> d.getId().equals(2L) && d.getFirstName().equals("B")));
    }

    @Test
    void updateCustomer_shouldThrow_whenMissing() {
        when(customerDao.getCustomerById(1L)).thenReturn(null);

        CustomerDto dto = new CustomerDto();
        dto.setFirstName("New");
        dto.setLastName("Name");
        dto.setBirthDate(LocalDate.of(2001, 1, 1));
        dto.setBudget(new BigDecimal("999.00"));

        assertThrows(DAOException.class, () -> service.updateCustomer(1L, dto));
        verify(customerDao, never()).updateCustomer(anyLong(), any());
    }

    @Test
    void updateCustomer_shouldUpdate() throws DAOException {
        Customer existing = new Customer();
        existing.setId(1L);
        existing.setFirstName("Old");
        existing.setLastName("Old");
        existing.setBirthDate(LocalDate.of(2000, 1, 1));
        existing.setBudget(new BigDecimal("10.00"));

        when(customerDao.getCustomerById(1L)).thenReturn(existing);

        CustomerDto dto = new CustomerDto();
        dto.setFirstName("New");
        dto.setLastName("Name");
        dto.setBirthDate(LocalDate.of(2001, 1, 1));
        dto.setBudget(new BigDecimal("999.00"));

        CustomerDto result = service.updateCustomer(1L, dto);

        assertEquals(1L, result.getId());
        assertEquals("New", result.getFirstName());
        assertEquals("Name", result.getLastName());
        assertEquals(LocalDate.of(2001, 1, 1), result.getBirthDate());
        assertEquals(new BigDecimal("999.00"), result.getBudget());

        verify(customerDao).updateCustomer(eq(1L), same(existing));
    }

    @Test
    void deleteCustomer_shouldThrow_whenMissing() {
        when(customerDao.getCustomerById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.deleteCustomer(1L));
        verify(customerDao, never()).deleteCustomer(anyLong());
    }

    @Test
    void deleteCustomer_shouldDelete() throws DAOException {
        Customer existing = new Customer();
        existing.setId(1L);

        when(customerDao.getCustomerById(1L)).thenReturn(existing);

        service.deleteCustomer(1L);

        verify(customerDao).deleteCustomer(1L);
    }

    @Test
    void addTransportToCustomer_shouldThrow_whenCustomerMissing() {
        when(customerDao.getCustomerById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.addTransportToCustomer(1L, 10L));
        verifyNoInteractions(transportDao);
    }

    @Test
    void addTransportToCustomer_shouldThrow_whenTransportMissing() {
        Customer customer = new Customer();
        customer.setId(1L);
        when(customerDao.getCustomerById(1L)).thenReturn(customer);

        when(transportDao.getTransportById(10L)).thenReturn(null);

        assertThrows(DAOException.class, () -> service.addTransportToCustomer(1L, 10L));
        verify(transportDao, never()).updateTransport(anyLong(), any());
    }

    @Test
    void addTransportToCustomer_shouldThrow_whenTransportAssignedToAnotherCustomer() {
        Customer c1 = new Customer();
        c1.setId(1L);

        Customer c2 = new Customer();
        c2.setId(2L);

        Transport t = new Transport();
        t.setId(10L);
        t.setCustomer(c2);

        when(customerDao.getCustomerById(1L)).thenReturn(c1);
        when(transportDao.getTransportById(10L)).thenReturn(t);

        assertThrows(DAOException.class, () -> service.addTransportToCustomer(1L, 10L));
        verify(transportDao, never()).updateTransport(anyLong(), any());
    }

    @Test
    void addTransportToCustomer_shouldAssignAndUpdate() throws DAOException {
        Customer customer = new Customer();
        customer.setId(1L);

        Transport t = new Transport();
        t.setId(10L);
        t.setCustomer(null);

        when(customerDao.getCustomerById(1L)).thenReturn(customer);
        when(transportDao.getTransportById(10L)).thenReturn(t);

        service.addTransportToCustomer(1L, 10L);

        assertNotNull(t.getCustomer());
        assertEquals(1L, t.getCustomer().getId());
        assertTrue(customer.getTransportSet().contains(t));

        verify(transportDao).updateTransport(eq(10L), same(t));
    }

    @Test
    void removeTransportFromCustomer_shouldThrow_whenCustomerMissing() {
        when(customerDao.getCustomerById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.removeTransportFromCustomer(1L, 10L));
        verifyNoInteractions(transportDao);
    }

    @Test
    void removeTransportFromCustomer_shouldThrow_whenTransportMissing() {
        Customer customer = new Customer();
        customer.setId(1L);
        when(customerDao.getCustomerById(1L)).thenReturn(customer);

        when(transportDao.getTransportById(10L)).thenReturn(null);

        assertThrows(DAOException.class, () -> service.removeTransportFromCustomer(1L, 10L));
        verify(transportDao, never()).updateTransport(anyLong(), any());
    }

    @Test
    void removeTransportFromCustomer_shouldThrow_whenNotAssignedToCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);

        Customer other = new Customer();
        other.setId(2L);

        Transport t = new Transport();
        t.setId(10L);
        t.setCustomer(other);

        when(customerDao.getCustomerById(1L)).thenReturn(customer);
        when(transportDao.getTransportById(10L)).thenReturn(t);

        assertThrows(DAOException.class, () -> service.removeTransportFromCustomer(1L, 10L));
        verify(transportDao, never()).updateTransport(anyLong(), any());
    }

    @Test
    void removeTransportFromCustomer_shouldUnassignAndUpdate() throws DAOException {
        Customer customer = new Customer();
        customer.setId(1L);

        Transport t = new Transport();
        t.setId(10L);
        t.setCustomer(customer);

        customer.getTransportSet().add(t);

        when(customerDao.getCustomerById(1L)).thenReturn(customer);
        when(transportDao.getTransportById(10L)).thenReturn(t);

        service.removeTransportFromCustomer(1L, 10L);

        assertNull(t.getCustomer());
        assertFalse(customer.getTransportSet().contains(t));

        verify(transportDao).updateTransport(eq(10L), same(t));
    }

    @Test
    void getAllTransportIdsForCustomer_shouldReturnMatchingIds() throws DAOException {
        Customer customer = new Customer();
        customer.setId(1L);

        Transport t1 = new Transport();
        t1.setId(10L);
        t1.setCustomer(customer);

        Transport t2 = new Transport();
        t2.setId(11L);
        t2.setCustomer(null);

        Customer other = new Customer();
        other.setId(2L);

        Transport t3 = new Transport();
        t3.setId(12L);
        t3.setCustomer(other);

        when(transportDao.getAllTransports()).thenReturn(List.of(t1, t2, t3));

        Set<Long> ids = service.getAllTransportIdsForCustomer(1L);

        assertEquals(Set.of(10L), ids);
    }
}
