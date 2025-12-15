package org.university.service.impl.driving_license_service_impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.university.dao.DrivingLicenseDao;
import org.university.dao.EmployeeDao;
import org.university.dto.DrivingLicenseDto;
import org.university.entity.DrivingLicense;
import org.university.entity.Employee;
import org.university.exception.DAOException;
import org.university.util.DrivingLicenseCategories;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DrivingLicenseServiceImplTest {

    @Mock DrivingLicenseDao drivingLicenseDao;
    @Mock EmployeeDao employeeDao;

    DrivingLicenseServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new DrivingLicenseServiceImpl(drivingLicenseDao, employeeDao);
    }

    @Test
    void mapToEntity() {
        DrivingLicenseDto dto = new DrivingLicenseDto();
        dto.setDrivingLicenseNumber("DL-123");
        dto.setIssueDate(LocalDate.of(2020, 1, 1));
        dto.setExpiryDate(LocalDate.of(2030, 1, 1));
        dto.setDrivingLicenseCategories(Set.of(DrivingLicenseCategories.B));

        DrivingLicense dl = service.mapToEntity(dto);

        assertEquals("DL-123", dl.getDrivingLicenseNumber());
        assertEquals(LocalDate.of(2020, 1, 1), dl.getIssueDate());
        assertEquals(LocalDate.of(2030, 1, 1), dl.getExpiryDate());
        assertEquals(Set.of(DrivingLicenseCategories.B), dl.getDrivingLicenseCategories());
    }

    @Test
    void mapToDto_withoutEmployee() {
        DrivingLicense dl = DrivingLicense.builder()
                .drivingLicenseNumber("DL-123")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .drivingLicenseCategories(Set.of(DrivingLicenseCategories.B))
                .build();
        dl.setId(1L);
        dl.setEmployee(null);

        DrivingLicenseDto dto = service.mapToDto(dl);

        assertEquals(1L, dto.getId());
        assertEquals("DL-123", dto.getDrivingLicenseNumber());
        assertEquals(LocalDate.of(2020, 1, 1), dto.getIssueDate());
        assertEquals(LocalDate.of(2030, 1, 1), dto.getExpiryDate());
        assertEquals(Set.of(DrivingLicenseCategories.B), dto.getDrivingLicenseCategories());
        assertNull(dto.getEmployeeId());
    }

    @Test
    void mapToDto_withEmployee_setsEmployeeId() {
        Employee e = new Employee();
        e.setId(10L);

        DrivingLicense dl = DrivingLicense.builder()
                .drivingLicenseNumber("DL-123")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .drivingLicenseCategories(Set.of(DrivingLicenseCategories.B))
                .build();
        dl.setId(1L);
        dl.setEmployee(e);

        DrivingLicenseDto dto = service.mapToDto(dl);

        assertEquals(10L, dto.getEmployeeId());
    }

    @Test
    void createDrivingLicense_shouldCreate() throws DAOException {
        DrivingLicenseDto dto = new DrivingLicenseDto();
        dto.setDrivingLicenseNumber("DL-123");
        dto.setIssueDate(LocalDate.of(2020, 1, 1));
        dto.setExpiryDate(LocalDate.of(2030, 1, 1));
        dto.setDrivingLicenseCategories(Set.of(DrivingLicenseCategories.B));

        doNothing().when(drivingLicenseDao).createDrivingLicense(any(DrivingLicense.class));

        DrivingLicenseDto result = service.createDrivingLicense(dto);

        assertNull(result.getId());
        assertEquals("DL-123", result.getDrivingLicenseNumber());
        verify(drivingLicenseDao).createDrivingLicense(any(DrivingLicense.class));
    }

    @Test
    void getDrivingLicenseById_shouldThrow_whenMissing() {
        when(drivingLicenseDao.getDrivingLicenseById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.getDrivingLicenseById(1L));
    }

    @Test
    void getDrivingLicenseById_shouldReturnDto() throws DAOException {
        DrivingLicense dl = DrivingLicense.builder()
                .drivingLicenseNumber("DL-123")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .drivingLicenseCategories(Set.of(DrivingLicenseCategories.B))
                .build();
        dl.setId(1L);

        when(drivingLicenseDao.getDrivingLicenseById(1L)).thenReturn(dl);

        DrivingLicenseDto dto = service.getDrivingLicenseById(1L);

        assertEquals(1L, dto.getId());
        assertEquals("DL-123", dto.getDrivingLicenseNumber());
    }

    @Test
    void getAllDrivingLicenses() {
        DrivingLicense dl1 = DrivingLicense.builder()
                .drivingLicenseNumber("DL-1")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .drivingLicenseCategories(Set.of(DrivingLicenseCategories.B))
                .build();
        dl1.setId(1L);

        DrivingLicense dl2 = DrivingLicense.builder()
                .drivingLicenseNumber("DL-2")
                .issueDate(LocalDate.of(2021, 1, 1))
                .expiryDate(LocalDate.of(2031, 1, 1))
                .drivingLicenseCategories(Set.of(DrivingLicenseCategories.C)) // <-- NOTE: fix below
                .build();
        dl2.setId(2L);

        when(drivingLicenseDao.getAllDrivingLicenses()).thenReturn(List.of(dl1, dl2));

        Set<DrivingLicenseDto> result = service.getAllDrivingLicenses();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(d -> d.getId().equals(1L) && d.getDrivingLicenseNumber().equals("DL-1")));
        assertTrue(result.stream().anyMatch(d -> d.getId().equals(2L) && d.getDrivingLicenseNumber().equals("DL-2")));
    }

    @Test
    void updateDrivingLicense_shouldThrow_whenMissing() {
        when(drivingLicenseDao.getDrivingLicenseById(1L)).thenReturn(null);

        DrivingLicenseDto dto = new DrivingLicenseDto();
        dto.setDrivingLicenseNumber("NEW");
        dto.setIssueDate(LocalDate.of(2022, 1, 1));
        dto.setExpiryDate(LocalDate.of(2032, 1, 1));
        dto.setDrivingLicenseCategories(Set.of(DrivingLicenseCategories.C));

        assertThrows(DAOException.class, () -> service.updateDrivingLicense(1L, dto));
        verify(drivingLicenseDao, never()).updateDrivingLicense(anyLong(), any());
    }

    @Test
    void updateDrivingLicense_shouldUpdate() throws DAOException {
        DrivingLicense existing = DrivingLicense.builder()
                .drivingLicenseNumber("OLD")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .drivingLicenseCategories(Set.of(DrivingLicenseCategories.B))
                .build();
        existing.setId(1L);

        when(drivingLicenseDao.getDrivingLicenseById(1L)).thenReturn(existing);

        DrivingLicenseDto dto = new DrivingLicenseDto();
        dto.setDrivingLicenseNumber("NEW");
        dto.setIssueDate(LocalDate.of(2022, 1, 1));
        dto.setExpiryDate(LocalDate.of(2032, 1, 1));
        dto.setDrivingLicenseCategories(Set.of(DrivingLicenseCategories.C));

        DrivingLicenseDto result = service.updateDrivingLicense(1L, dto);

        assertEquals(1L, result.getId());
        assertEquals("NEW", result.getDrivingLicenseNumber());
        assertEquals(Set.of(DrivingLicenseCategories.C), result.getDrivingLicenseCategories());

        verify(drivingLicenseDao).updateDrivingLicense(eq(1L), same(existing));
    }

    @Test
    void deleteDrivingLicense_shouldThrow_whenMissing() {
        when(drivingLicenseDao.getDrivingLicenseById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.deleteDrivingLicense(1L));
        verify(drivingLicenseDao, never()).deleteDrivingLicense(anyLong());
    }

    @Test
    void deleteDrivingLicense_shouldDelete() throws DAOException {
        DrivingLicense existing = DrivingLicense.builder()
                .drivingLicenseNumber("X")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .drivingLicenseCategories(Set.of(DrivingLicenseCategories.B))
                .build();
        existing.setId(1L);

        when(drivingLicenseDao.getDrivingLicenseById(1L)).thenReturn(existing);

        service.deleteDrivingLicense(1L);

        verify(drivingLicenseDao).deleteDrivingLicense(1L);
    }

    @Test
    void assignDrivingLicenseToEmployee_shouldThrow_whenLicenseMissing() {
        when(drivingLicenseDao.getDrivingLicenseById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.assignDrivingLicenseToEmployee(1L, 10L));
        verifyNoInteractions(employeeDao);
    }

    @Test
    void assignDrivingLicenseToEmployee_shouldThrow_whenEmployeeMissing() {
        DrivingLicense dl = DrivingLicense.builder()
                .drivingLicenseNumber("DL")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .drivingLicenseCategories(Set.of(DrivingLicenseCategories.B))
                .build();
        dl.setId(1L);

        when(drivingLicenseDao.getDrivingLicenseById(1L)).thenReturn(dl);
        when(employeeDao.getEmployeeById(10L)).thenReturn(null);

        assertThrows(DAOException.class, () -> service.assignDrivingLicenseToEmployee(1L, 10L));
    }

    @Test
    void assignDrivingLicenseToEmployee_shouldThrow_whenLicenseAssignedToAnotherEmployee() {
        Employee other = new Employee();
        other.setId(99L);

        DrivingLicense dl = DrivingLicense.builder()
                .drivingLicenseNumber("DL")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .drivingLicenseCategories(Set.of(DrivingLicenseCategories.B))
                .build();
        dl.setId(1L);
        dl.setEmployee(other);

        Employee target = new Employee();
        target.setId(10L);

        when(drivingLicenseDao.getDrivingLicenseById(1L)).thenReturn(dl);
        when(employeeDao.getEmployeeById(10L)).thenReturn(target);

        assertThrows(DAOException.class, () -> service.assignDrivingLicenseToEmployee(1L, 10L));
        verify(employeeDao, never()).updateEmployee(anyLong(), any());
        verify(drivingLicenseDao, never()).updateDrivingLicense(eq(1L), any());
    }

    @Test
    void assignDrivingLicenseToEmployee_shouldDetachOldEmployeeLicense_andAssignNew() throws DAOException {
        DrivingLicense newDl = DrivingLicense.builder()
                .drivingLicenseNumber("NEW")
                .issueDate(LocalDate.of(2022, 1, 1))
                .expiryDate(LocalDate.of(2032, 1, 1))
                .drivingLicenseCategories(Set.of(DrivingLicenseCategories.C))
                .build();
        newDl.setId(1L);

        DrivingLicense oldDl = DrivingLicense.builder()
                .drivingLicenseNumber("OLD")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .drivingLicenseCategories(Set.of(DrivingLicenseCategories.B))
                .build();
        oldDl.setId(2L);

        Employee employee = new Employee();
        employee.setId(10L);
        employee.setDrivingLicense(oldDl);
        oldDl.setEmployee(employee);

        when(drivingLicenseDao.getDrivingLicenseById(1L)).thenReturn(newDl);
        when(employeeDao.getEmployeeById(10L)).thenReturn(employee);

        service.assignDrivingLicenseToEmployee(1L, 10L);

        assertNull(oldDl.getEmployee());
        assertEquals(employee, newDl.getEmployee());
        assertEquals(newDl, employee.getDrivingLicense());

        verify(drivingLicenseDao).updateDrivingLicense(eq(2L), same(oldDl));
        verify(employeeDao).updateEmployee(eq(10L), same(employee));
        verify(drivingLicenseDao).updateDrivingLicense(eq(1L), same(newDl));
    }

    @Test
    void removeDrivingLicenseFromEmployee_shouldThrow_whenLicenseMissing() {
        when(drivingLicenseDao.getDrivingLicenseById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.removeDrivingLicenseFromEmployee(1L, 10L));
        verifyNoInteractions(employeeDao);
    }

    @Test
    void removeDrivingLicenseFromEmployee_shouldThrow_whenEmployeeMissing() {
        DrivingLicense dl = DrivingLicense.builder()
                .drivingLicenseNumber("DL")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .drivingLicenseCategories(Set.of(DrivingLicenseCategories.B))
                .build();
        dl.setId(1L);

        when(drivingLicenseDao.getDrivingLicenseById(1L)).thenReturn(dl);
        when(employeeDao.getEmployeeById(10L)).thenReturn(null);

        assertThrows(DAOException.class, () -> service.removeDrivingLicenseFromEmployee(1L, 10L));
    }

    @Test
    void removeDrivingLicenseFromEmployee_shouldThrow_whenEmployeeNotAssignedThatLicense() {
        DrivingLicense dl = DrivingLicense.builder()
                .drivingLicenseNumber("DL")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .drivingLicenseCategories(Set.of(DrivingLicenseCategories.B))
                .build();
        dl.setId(1L);

        Employee employee = new Employee();
        employee.setId(10L);
        employee.setDrivingLicense(null);

        when(drivingLicenseDao.getDrivingLicenseById(1L)).thenReturn(dl);
        when(employeeDao.getEmployeeById(10L)).thenReturn(employee);

        assertThrows(DAOException.class, () -> service.removeDrivingLicenseFromEmployee(1L, 10L));
        verify(employeeDao, never()).updateEmployee(anyLong(), any());
        verify(drivingLicenseDao, never()).updateDrivingLicense(eq(1L), any());
    }

    @Test
    void removeDrivingLicenseFromEmployee_shouldUnassignAndUpdate() throws DAOException {
        DrivingLicense dl = DrivingLicense.builder()
                .drivingLicenseNumber("DL")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .drivingLicenseCategories(Set.of(DrivingLicenseCategories.B))
                .build();
        dl.setId(1L);

        Employee employee = new Employee();
        employee.setId(10L);
        employee.setDrivingLicense(dl);
        dl.setEmployee(employee);

        when(drivingLicenseDao.getDrivingLicenseById(1L)).thenReturn(dl);
        when(employeeDao.getEmployeeById(10L)).thenReturn(employee);

        service.removeDrivingLicenseFromEmployee(1L, 10L);

        assertNull(dl.getEmployee());
        assertNull(employee.getDrivingLicense());

        verify(employeeDao).updateEmployee(eq(10L), same(employee));
        verify(drivingLicenseDao).updateDrivingLicense(eq(1L), same(dl));
    }

    @Test
    void getEmployeeIdForDrivingLicense_shouldThrow_whenLicenseMissing() {
        when(drivingLicenseDao.getDrivingLicenseById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.getEmployeeIdForDrivingLicense(1L));
        verifyNoInteractions(employeeDao);
    }

    @Test
    void getEmployeeIdForDrivingLicense_shouldReturnEmployeeId_whenAssigned() throws DAOException {
        DrivingLicense dl = DrivingLicense.builder()
                .drivingLicenseNumber("DL")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .drivingLicenseCategories(Set.of(DrivingLicenseCategories.B))
                .build();
        dl.setId(1L);

        Employee e1 = new Employee();
        e1.setId(10L);
        e1.setDrivingLicense(dl);

        when(drivingLicenseDao.getDrivingLicenseById(1L)).thenReturn(dl);
        when(employeeDao.getAllEmployees()).thenReturn(List.of(e1));

        Long id = service.getEmployeeIdForDrivingLicense(1L);

        assertEquals(10L, id);
    }

    @Test
    void getEmployeeIdForDrivingLicense_shouldThrow_whenNotAssignedToAnyEmployee() {
        DrivingLicense dl = DrivingLicense.builder()
                .drivingLicenseNumber("DL")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .drivingLicenseCategories(Set.of(DrivingLicenseCategories.B))
                .build();
        dl.setId(1L);

        Employee e1 = new Employee();
        e1.setId(10L);
        e1.setDrivingLicense(null);

        when(drivingLicenseDao.getDrivingLicenseById(1L)).thenReturn(dl);
        when(employeeDao.getAllEmployees()).thenReturn(List.of(e1));

        assertThrows(DAOException.class, () -> service.getEmployeeIdForDrivingLicense(1L));
    }
}
