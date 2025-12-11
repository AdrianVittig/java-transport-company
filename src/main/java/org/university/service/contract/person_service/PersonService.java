package org.university.service.contract.person_service;

import org.university.dto.PersonDto;
import org.university.entity.Person;
import org.university.exception.DAOException;

import java.util.Set;

public interface PersonService {
    Person mapToEntity(PersonDto personDto);

    PersonDto mapToDto(Person person);

    PersonDto createPerson(PersonDto personDto) throws DAOException;

    PersonDto getPersonById(Long id) throws DAOException;

    Set<PersonDto> getAllPeople();

    PersonDto updatePerson(Long id, PersonDto personDto) throws DAOException;

    void deletePerson(Long id) throws DAOException;

    Long getIdentificationCardIdForPerson(Long personId) throws DAOException;
}
