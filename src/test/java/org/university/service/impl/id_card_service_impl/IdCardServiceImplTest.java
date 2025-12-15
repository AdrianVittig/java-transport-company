package org.university.service.impl.id_card_service_impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.university.dao.IdentificationCardDao;
import org.university.dao.PersonDao;
import org.university.dto.IdentificationCardDto;
import org.university.entity.IdentificationCard;
import org.university.entity.Person;
import org.university.exception.DAOException;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IdCardServiceImplTest {

    @Mock IdentificationCardDao identificationCardDao;
    @Mock PersonDao personDao;

    IdCardServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new IdCardServiceImpl(identificationCardDao, personDao);
    }

    @Test
    void mapToEntity() {
        IdentificationCardDto dto = new IdentificationCardDto();
        dto.setCardNumber("ID-123");
        dto.setIssueDate(LocalDate.of(2020, 1, 1));
        dto.setExpiryDate(LocalDate.of(2030, 1, 1));

        IdentificationCard card = service.mapToEntity(dto);

        assertEquals("ID-123", card.getCardNumber());
        assertEquals(LocalDate.of(2020, 1, 1), card.getIssueDate());
        assertEquals(LocalDate.of(2030, 1, 1), card.getExpiryDate());
    }

    @Test
    void mapToDto_withoutPerson() {
        IdentificationCard card = IdentificationCard.builder()
                .cardNumber("ID-123")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .build();
        card.setId(1L);
        card.setPerson(null);

        IdentificationCardDto dto = service.mapToDto(card);

        assertEquals(1L, dto.getId());
        assertEquals("ID-123", dto.getCardNumber());
        assertEquals(LocalDate.of(2020, 1, 1), dto.getIssueDate());
        assertEquals(LocalDate.of(2030, 1, 1), dto.getExpiryDate());
        assertNull(dto.getPersonId());
    }

    @Test
    void mapToDto_withPerson_setsPersonId() {
        Person p = new Person();
        p.setId(10L);

        IdentificationCard card = IdentificationCard.builder()
                .cardNumber("ID-123")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .build();
        card.setId(1L);
        card.setPerson(p);

        IdentificationCardDto dto = service.mapToDto(card);

        assertEquals(10L, dto.getPersonId());
    }

    @Test
    void createIdentificationCard_shouldCreate() throws DAOException {
        IdentificationCardDto dto = new IdentificationCardDto();
        dto.setCardNumber("ID-123");
        dto.setIssueDate(LocalDate.of(2020, 1, 1));
        dto.setExpiryDate(LocalDate.of(2030, 1, 1));

        doNothing().when(identificationCardDao).createIdentificationCard(any(IdentificationCard.class));

        IdentificationCardDto result = service.createIdentificationCard(dto);

        assertNull(result.getId());
        assertEquals("ID-123", result.getCardNumber());
        verify(identificationCardDao).createIdentificationCard(any(IdentificationCard.class));
    }

    @Test
    void getIdentificationCardById_shouldThrow_whenMissing() {
        when(identificationCardDao.getIdentificationCardById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.getIdentificationCardById(1L));
    }

    @Test
    void getIdentificationCardById_shouldReturnDto() throws DAOException {
        IdentificationCard card = IdentificationCard.builder()
                .cardNumber("ID-123")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .build();
        card.setId(1L);

        when(identificationCardDao.getIdentificationCardById(1L)).thenReturn(card);

        IdentificationCardDto dto = service.getIdentificationCardById(1L);

        assertEquals(1L, dto.getId());
        assertEquals("ID-123", dto.getCardNumber());
    }

    @Test
    void getAllIdentificationCards() {
        IdentificationCard c1 = IdentificationCard.builder()
                .cardNumber("ID-1")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .build();
        c1.setId(1L);

        IdentificationCard c2 = IdentificationCard.builder()
                .cardNumber("ID-2")
                .issueDate(LocalDate.of(2021, 1, 1))
                .expiryDate(LocalDate.of(2031, 1, 1))
                .build();
        c2.setId(2L);

        when(identificationCardDao.getAllIdentificationCards()).thenReturn(List.of(c1, c2));

        Set<IdentificationCardDto> result = service.getAllIdentificationCards();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(d -> d.getId().equals(1L) && d.getCardNumber().equals("ID-1")));
        assertTrue(result.stream().anyMatch(d -> d.getId().equals(2L) && d.getCardNumber().equals("ID-2")));
    }

    @Test
    void updateIdentificationCard_shouldThrow_whenMissing() {
        when(identificationCardDao.getIdentificationCardById(1L)).thenReturn(null);

        IdentificationCardDto dto = new IdentificationCardDto();
        dto.setCardNumber("NEW");
        dto.setIssueDate(LocalDate.of(2022, 1, 1));
        dto.setExpiryDate(LocalDate.of(2032, 1, 1));

        assertThrows(DAOException.class, () -> service.updateIdentificationCard(1L, dto));
        verify(identificationCardDao, never()).updateIdentificationCard(anyLong(), any());
    }

    @Test
    void updateIdentificationCard_shouldUpdate() throws DAOException {
        IdentificationCard existing = IdentificationCard.builder()
                .cardNumber("OLD")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .build();
        existing.setId(1L);

        when(identificationCardDao.getIdentificationCardById(1L)).thenReturn(existing);

        IdentificationCardDto dto = new IdentificationCardDto();
        dto.setCardNumber("NEW");
        dto.setIssueDate(LocalDate.of(2022, 1, 1));
        dto.setExpiryDate(LocalDate.of(2032, 1, 1));

        IdentificationCardDto result = service.updateIdentificationCard(1L, dto);

        assertEquals(1L, result.getId());
        assertEquals("NEW", result.getCardNumber());
        assertEquals(LocalDate.of(2022, 1, 1), result.getIssueDate());
        assertEquals(LocalDate.of(2032, 1, 1), result.getExpiryDate());

        verify(identificationCardDao).updateIdentificationCard(eq(1L), same(existing));
    }

    @Test
    void deleteIdentificationCard_shouldThrow_whenMissing() {
        when(identificationCardDao.getIdentificationCardById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.deleteIdentificationCard(1L));
        verify(identificationCardDao, never()).deleteIdentificationCard(anyLong());
    }

    @Test
    void deleteIdentificationCard_shouldDelete() throws DAOException {
        IdentificationCard existing = IdentificationCard.builder()
                .cardNumber("X")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .build();
        existing.setId(1L);

        when(identificationCardDao.getIdentificationCardById(1L)).thenReturn(existing);

        service.deleteIdentificationCard(1L);

        verify(identificationCardDao).deleteIdentificationCard(1L);
    }

    @Test
    void assignIdCardToPerson_shouldThrow_whenCardMissing() {
        when(identificationCardDao.getIdentificationCardById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.assignIdCardToPerson(1L, 10L));
        verifyNoInteractions(personDao);
    }

    @Test
    void assignIdCardToPerson_shouldThrow_whenPersonMissing() {
        IdentificationCard card = IdentificationCard.builder()
                .cardNumber("ID")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .build();
        card.setId(1L);

        when(identificationCardDao.getIdentificationCardById(1L)).thenReturn(card);
        when(personDao.getPersonById(10L)).thenReturn(null);

        assertThrows(DAOException.class, () -> service.assignIdCardToPerson(1L, 10L));
    }

    @Test
    void assignIdCardToPerson_shouldReturnEarly_whenAlreadyAssignedSameCard() throws DAOException {
        Person p = new Person();
        p.setId(10L);

        IdentificationCard card = IdentificationCard.builder()
                .cardNumber("ID")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .build();
        card.setId(1L);

        IdentificationCard already = IdentificationCard.builder()
                .cardNumber("ID")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .build();
        already.setId(1L);

        p.setIdentificationCard(already);

        when(identificationCardDao.getIdentificationCardById(1L)).thenReturn(card);
        when(personDao.getPersonById(10L)).thenReturn(p);

        service.assignIdCardToPerson(1L, 10L);

        verify(identificationCardDao, never()).updateIdentificationCard(anyLong(), any());
    }

    @Test
    void assignIdCardToPerson_shouldThrow_whenCardAssignedToAnotherPerson() {
        Person other = new Person();
        other.setId(99L);

        IdentificationCard card = IdentificationCard.builder()
                .cardNumber("ID")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .build();
        card.setId(1L);
        card.setPerson(other);

        Person target = new Person();
        target.setId(10L);

        when(identificationCardDao.getIdentificationCardById(1L)).thenReturn(card);
        when(personDao.getPersonById(10L)).thenReturn(target);

        assertThrows(DAOException.class, () -> service.assignIdCardToPerson(1L, 10L));
        verify(identificationCardDao, never()).updateIdentificationCard(eq(1L), any());
    }

    @Test
    void assignIdCardToPerson_shouldThrow_whenPersonHasAnotherCard() {
        Person p = new Person();
        p.setId(10L);

        IdentificationCard otherCard = IdentificationCard.builder()
                .cardNumber("OTHER")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .build();
        otherCard.setId(2L);

        p.setIdentificationCard(otherCard);

        IdentificationCard card = IdentificationCard.builder()
                .cardNumber("ID")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .build();
        card.setId(1L);

        when(identificationCardDao.getIdentificationCardById(1L)).thenReturn(card);
        when(personDao.getPersonById(10L)).thenReturn(p);

        assertThrows(DAOException.class, () -> service.assignIdCardToPerson(1L, 10L));
        verify(identificationCardDao, never()).updateIdentificationCard(anyLong(), any());
    }

    @Test
    void assignIdCardToPerson_shouldAssignAndUpdate() throws DAOException {
        Person p = new Person();
        p.setId(10L);
        p.setIdentificationCard(null);

        IdentificationCard card = IdentificationCard.builder()
                .cardNumber("ID")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .build();
        card.setId(1L);
        card.setPerson(null);

        when(identificationCardDao.getIdentificationCardById(1L)).thenReturn(card);
        when(personDao.getPersonById(10L)).thenReturn(p);

        service.assignIdCardToPerson(1L, 10L);

        assertEquals(p, card.getPerson());
        assertEquals(card, p.getIdentificationCard());

        verify(identificationCardDao).updateIdentificationCard(eq(1L), same(card));
    }

    @Test
    void removeIdCardFromPerson_shouldThrow_whenCardMissing() {
        when(identificationCardDao.getIdentificationCardById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.removeIdCardFromPerson(1L, 10L));
        verifyNoInteractions(personDao);
    }

    @Test
    void removeIdCardFromPerson_shouldThrow_whenPersonMissing() {
        IdentificationCard card = IdentificationCard.builder()
                .cardNumber("ID")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .build();
        card.setId(1L);

        when(identificationCardDao.getIdentificationCardById(1L)).thenReturn(card);
        when(personDao.getPersonById(10L)).thenReturn(null);

        assertThrows(DAOException.class, () -> service.removeIdCardFromPerson(1L, 10L));
    }

    @Test
    void removeIdCardFromPerson_shouldThrow_whenPersonNotAssignedThatCard() {
        Person p = new Person();
        p.setId(10L);
        p.setIdentificationCard(null);

        IdentificationCard card = IdentificationCard.builder()
                .cardNumber("ID")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .build();
        card.setId(1L);

        when(identificationCardDao.getIdentificationCardById(1L)).thenReturn(card);
        when(personDao.getPersonById(10L)).thenReturn(p);

        assertThrows(DAOException.class, () -> service.removeIdCardFromPerson(1L, 10L));
        verify(identificationCardDao, never()).updateIdentificationCard(anyLong(), any());
    }

    @Test
    void removeIdCardFromPerson_shouldUnassignAndUpdate() throws DAOException {
        Person p = new Person();
        p.setId(10L);

        IdentificationCard card = IdentificationCard.builder()
                .cardNumber("ID")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .build();
        card.setId(1L);

        p.setIdentificationCard(card);
        card.setPerson(p);

        when(identificationCardDao.getIdentificationCardById(1L)).thenReturn(card);
        when(personDao.getPersonById(10L)).thenReturn(p);

        service.removeIdCardFromPerson(1L, 10L);

        assertNull(card.getPerson());
        assertNull(p.getIdentificationCard());

        verify(identificationCardDao).updateIdentificationCard(eq(1L), same(card));
    }

    @Test
    void getPersonIdForIdCard_shouldThrow_whenCardMissing() {
        when(identificationCardDao.getIdentificationCardById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.getPersonIdForIdCard(1L));
    }

    @Test
    void getPersonIdForIdCard_shouldThrow_whenNotAssigned() {
        IdentificationCard card = IdentificationCard.builder()
                .cardNumber("ID")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .build();
        card.setId(1L);
        card.setPerson(null);

        when(identificationCardDao.getIdentificationCardById(1L)).thenReturn(card);

        assertThrows(DAOException.class, () -> service.getPersonIdForIdCard(1L));
    }

    @Test
    void getPersonIdForIdCard_shouldReturnPersonId() throws DAOException {
        Person p = new Person();
        p.setId(10L);

        IdentificationCard card = IdentificationCard.builder()
                .cardNumber("ID")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .build();
        card.setId(1L);
        card.setPerson(p);

        when(identificationCardDao.getIdentificationCardById(1L)).thenReturn(card);

        Long id = service.getPersonIdForIdCard(1L);

        assertEquals(10L, id);
    }
}
