package org.university.service.impl.person_service_impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.university.dao.IdentificationCardDao;
import org.university.dao.PersonDao;
import org.university.dto.PersonDto;
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
class PersonServiceImplTest {

    @Mock PersonDao personDao;
    @Mock IdentificationCardDao identificationCardDao;

    PersonServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new PersonServiceImpl(personDao, identificationCardDao);
    }

    @Test
    void mapToEntity() {
        PersonDto dto = new PersonDto();
        dto.setFirstName("Ivan");
        dto.setLastName("Ivanov");
        dto.setBirthDate(LocalDate.of(2000, 1, 1));

        Person p = service.mapToEntity(dto);

        assertEquals("Ivan", p.getFirstName());
        assertEquals("Ivanov", p.getLastName());
        assertEquals(LocalDate.of(2000, 1, 1), p.getBirthDate());
    }

    @Test
    void mapToDto_withoutCard() {
        Person p = new Person();
        p.setId(1L);
        p.setFirstName("Ivan");
        p.setLastName("Ivanov");
        p.setBirthDate(LocalDate.of(2000, 1, 1));
        p.setIdentificationCard(null);

        PersonDto dto = service.mapToDto(p);

        assertEquals(1L, dto.getId());
        assertEquals("Ivan", dto.getFirstName());
        assertEquals("Ivanov", dto.getLastName());
        assertEquals(LocalDate.of(2000, 1, 1), dto.getBirthDate());
        assertNull(dto.getIdentificationCardId());
    }

    @Test
    void mapToDto_withCard_setsCardId() {
        IdentificationCard card = IdentificationCard.builder()
                .cardNumber("ID-1")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .build();
        card.setId(10L);

        Person p = new Person();
        p.setId(1L);
        p.setFirstName("Ivan");
        p.setLastName("Ivanov");
        p.setBirthDate(LocalDate.of(2000, 1, 1));
        p.setIdentificationCard(card);

        PersonDto dto = service.mapToDto(p);

        assertEquals(10L, dto.getIdentificationCardId());
    }

    @Test
    void createPerson_shouldCreate() throws DAOException {
        PersonDto dto = new PersonDto();
        dto.setFirstName("Ivan");
        dto.setLastName("Ivanov");
        dto.setBirthDate(LocalDate.of(2000, 1, 1));

        doNothing().when(personDao).createPerson(any(Person.class));

        PersonDto result = service.createPerson(dto);

        assertNull(result.getId());
        assertEquals("Ivan", result.getFirstName());
        verify(personDao).createPerson(any(Person.class));
    }

    @Test
    void getPersonById_shouldThrow_whenMissing() {
        when(personDao.getPersonById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.getPersonById(1L));
    }

    @Test
    void getPersonById_shouldReturnDto() throws DAOException {
        Person p = new Person();
        p.setId(1L);
        p.setFirstName("Ivan");
        p.setLastName("Ivanov");
        p.setBirthDate(LocalDate.of(2000, 1, 1));

        when(personDao.getPersonById(1L)).thenReturn(p);

        PersonDto dto = service.getPersonById(1L);

        assertEquals(1L, dto.getId());
        assertEquals("Ivan", dto.getFirstName());
    }

    @Test
    void getAllPeople() {
        Person p1 = new Person();
        p1.setId(1L);
        p1.setFirstName("A");
        p1.setLastName("A");
        p1.setBirthDate(LocalDate.of(2000, 1, 1));

        Person p2 = new Person();
        p2.setId(2L);
        p2.setFirstName("B");
        p2.setLastName("B");
        p2.setBirthDate(LocalDate.of(2000, 1, 1));

        when(personDao.getAllPeople()).thenReturn(List.of(p1, p2));

        Set<PersonDto> result = service.getAllPeople();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(d -> d.getId().equals(1L) && d.getFirstName().equals("A")));
        assertTrue(result.stream().anyMatch(d -> d.getId().equals(2L) && d.getFirstName().equals("B")));
    }

    @Test
    void updatePerson_shouldThrow_whenPersonMissing() {
        when(personDao.getPersonById(1L)).thenReturn(null);

        PersonDto dto = new PersonDto();
        dto.setFirstName("New");
        dto.setLastName("Name");
        dto.setBirthDate(LocalDate.of(2001, 1, 1));

        assertThrows(DAOException.class, () -> service.updatePerson(1L, dto));
        verify(personDao, never()).updatePerson(anyLong(), any());
    }

    @Test
    void updatePerson_shouldDetachOldCard_whenNewCardIdIsNull() throws DAOException {
        IdentificationCard oldCard = IdentificationCard.builder()
                .cardNumber("OLD")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .build();
        oldCard.setId(10L);

        Person p = new Person();
        p.setId(1L);
        p.setFirstName("Ivan");
        p.setLastName("Ivanov");
        p.setBirthDate(LocalDate.of(2000, 1, 1));
        p.setIdentificationCard(oldCard);
        oldCard.setPerson(p);

        when(personDao.getPersonById(1L)).thenReturn(p);

        PersonDto dto = new PersonDto();
        dto.setFirstName("New");
        dto.setLastName("Name");
        dto.setBirthDate(LocalDate.of(2001, 1, 1));
        dto.setIdentificationCardId(null);

        PersonDto result = service.updatePerson(1L, dto);

        assertEquals("New", result.getFirstName());
        assertNull(p.getIdentificationCard());
        assertNull(oldCard.getPerson());

        verify(identificationCardDao).updateIdentificationCard(eq(10L), same(oldCard));
        verify(personDao).updatePerson(eq(1L), same(p));
    }

    @Test
    void updatePerson_shouldThrow_whenNewCardMissing() {
        Person p = new Person();
        p.setId(1L);
        p.setFirstName("Ivan");
        p.setLastName("Ivanov");
        p.setBirthDate(LocalDate.of(2000, 1, 1));
        p.setIdentificationCard(null);

        when(personDao.getPersonById(1L)).thenReturn(p);
        when(identificationCardDao.getIdentificationCardById(10L)).thenReturn(null);

        PersonDto dto = new PersonDto();
        dto.setFirstName("New");
        dto.setLastName("Name");
        dto.setBirthDate(LocalDate.of(2001, 1, 1));
        dto.setIdentificationCardId(10L);

        assertThrows(DAOException.class, () -> service.updatePerson(1L, dto));
        verify(personDao, never()).updatePerson(eq(1L), any());
    }

    @Test
    void updatePerson_shouldThrow_whenNewCardAssignedToAnotherPerson() {
        Person p = new Person();
        p.setId(1L);
        p.setFirstName("Ivan");
        p.setLastName("Ivanov");
        p.setBirthDate(LocalDate.of(2000, 1, 1));
        p.setIdentificationCard(null);

        Person other = new Person();
        other.setId(99L);

        IdentificationCard newCard = IdentificationCard.builder()
                .cardNumber("NEW")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .build();
        newCard.setId(10L);
        newCard.setPerson(other);

        when(personDao.getPersonById(1L)).thenReturn(p);
        when(identificationCardDao.getIdentificationCardById(10L)).thenReturn(newCard);

        PersonDto dto = new PersonDto();
        dto.setFirstName("New");
        dto.setLastName("Name");
        dto.setBirthDate(LocalDate.of(2001, 1, 1));
        dto.setIdentificationCardId(10L);

        assertThrows(DAOException.class, () -> service.updatePerson(1L, dto));
        verify(personDao, never()).updatePerson(eq(1L), any());
    }

    @Test
    void updatePerson_shouldDetachOldCardAndAssignNewCard() throws DAOException {
        IdentificationCard oldCard = IdentificationCard.builder()
                .cardNumber("OLD")
                .issueDate(LocalDate.of(2019, 1, 1))
                .expiryDate(LocalDate.of(2029, 1, 1))
                .build();
        oldCard.setId(9L);

        Person p = new Person();
        p.setId(1L);
        p.setFirstName("Ivan");
        p.setLastName("Ivanov");
        p.setBirthDate(LocalDate.of(2000, 1, 1));
        p.setIdentificationCard(oldCard);
        oldCard.setPerson(p);

        IdentificationCard newCard = IdentificationCard.builder()
                .cardNumber("NEW")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .build();
        newCard.setId(10L);
        newCard.setPerson(null);

        when(personDao.getPersonById(1L)).thenReturn(p);
        when(identificationCardDao.getIdentificationCardById(10L)).thenReturn(newCard);

        PersonDto dto = new PersonDto();
        dto.setFirstName("NewFirst");
        dto.setLastName("NewLast");
        dto.setBirthDate(LocalDate.of(2001, 1, 1));
        dto.setIdentificationCardId(10L);

        PersonDto result = service.updatePerson(1L, dto);

        assertEquals("NewFirst", result.getFirstName());
        assertEquals(10L, result.getIdentificationCardId());
        assertNull(oldCard.getPerson());
        assertEquals(newCard, p.getIdentificationCard());
        assertEquals(p, newCard.getPerson());

        verify(identificationCardDao).updateIdentificationCard(eq(9L), same(oldCard));
        verify(identificationCardDao).updateIdentificationCard(eq(10L), same(newCard));
        verify(personDao).updatePerson(eq(1L), same(p));
    }

    @Test
    void deletePerson_shouldThrow_whenMissing() {
        when(personDao.getPersonById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.deletePerson(1L));
        verify(personDao, never()).deletePerson(anyLong());
    }

    @Test
    void deletePerson_shouldDetachCardAndDelete() throws DAOException {
        IdentificationCard card = IdentificationCard.builder()
                .cardNumber("ID")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .build();
        card.setId(10L);

        Person p = new Person();
        p.setId(1L);
        p.setIdentificationCard(card);
        card.setPerson(p);

        when(personDao.getPersonById(1L)).thenReturn(p);

        service.deletePerson(1L);

        assertNull(p.getIdentificationCard());
        assertNull(card.getPerson());

        verify(identificationCardDao).updateIdentificationCard(eq(10L), same(card));
        verify(personDao).deletePerson(1L);
    }

    @Test
    void getIdentificationCardIdForPerson_shouldThrow_whenPersonMissing() {
        when(personDao.getPersonById(1L)).thenReturn(null);
        assertThrows(DAOException.class, () -> service.getIdentificationCardIdForPerson(1L));
    }

    @Test
    void getIdentificationCardIdForPerson_shouldThrow_whenNoCard() {
        Person p = new Person();
        p.setId(1L);
        p.setIdentificationCard(null);

        when(personDao.getPersonById(1L)).thenReturn(p);

        assertThrows(DAOException.class, () -> service.getIdentificationCardIdForPerson(1L));
    }

    @Test
    void getIdentificationCardIdForPerson_shouldReturnCardId() throws DAOException {
        IdentificationCard card = IdentificationCard.builder()
                .cardNumber("ID")
                .issueDate(LocalDate.of(2020, 1, 1))
                .expiryDate(LocalDate.of(2030, 1, 1))
                .build();
        card.setId(10L);

        Person p = new Person();
        p.setId(1L);
        p.setIdentificationCard(card);

        when(personDao.getPersonById(1L)).thenReturn(p);

        Long id = service.getIdentificationCardIdForPerson(1L);

        assertEquals(10L, id);
    }
}
