package org.university.service.impl.person_service_impl;

import org.university.dao.IdentificationCardDao;
import org.university.dao.PersonDao;
import org.university.dto.PersonDto;
import org.university.entity.IdentificationCard;
import org.university.entity.Person;
import org.university.exception.DAOException;
import org.university.service.contract.person_service.PersonService;

import java.util.Set;
import java.util.stream.Collectors;

public class PersonServiceImpl implements PersonService {
    private final PersonDao personDao;
    private final IdentificationCardDao identificationCardDao;

    public PersonServiceImpl(PersonDao personDao, IdentificationCardDao identificationCardDao) {
        this.personDao = personDao;
        this.identificationCardDao = identificationCardDao;
    }

    @Override
    public Person mapToEntity(PersonDto personDto) {
        Person person = new Person();
        person.setFirstName(personDto.getFirstName());
        person.setLastName(personDto.getLastName());
        person.setBirthDate(personDto.getBirthDate());

        return person;
    }

    @Override
    public PersonDto mapToDto(Person person) {
        PersonDto personDto = new PersonDto();
        personDto.setId(person.getId());
        personDto.setFirstName(person.getFirstName());
        personDto.setLastName(person.getLastName());
        personDto.setBirthDate(person.getBirthDate());

        if(person.getIdentificationCard() != null){
            personDto.setIdentificationCardId(person.getIdentificationCard().getId());
        }
        return personDto;
    }

    @Override
    public PersonDto createPerson(PersonDto personDto) throws DAOException {
        Person person = mapToEntity(personDto);
        personDao.createPerson(person);
        return mapToDto(person);
    }

    @Override
    public PersonDto getPersonById(Long id) throws DAOException {
        Person person = personDao.getPersonById(id);
        if(person == null) {
            throw new DAOException("Person with id " + id + " does not exist");
        }
        return mapToDto(person);
    }

    @Override
    public Set<PersonDto> getAllPeople() {
        return personDao.getAllPeople()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toSet());
    }

    @Override
    public PersonDto updatePerson(Long id, PersonDto personDto) throws DAOException {
        Person person = personDao.getPersonById(id);
        if(person == null) {
            throw new DAOException("Person with id " + id + " does not exist");
        }
        person.setFirstName(personDto.getFirstName());
        person.setLastName(personDto.getLastName());
        person.setBirthDate(personDto.getBirthDate());

        if(personDto.getIdentificationCardId() != null){
            IdentificationCard identificationCard = identificationCardDao.getIdentificationCardById(personDto.getIdentificationCardId());
            if(identificationCard == null){
                throw new DAOException("Identification card with id " + personDto.getIdentificationCardId() + " does not exist");
            }

            if(identificationCard.getPerson() != null && !identificationCard.getPerson().getId().equals(id)){
                throw new DAOException("Identification card with id " + personDto.getIdentificationCardId() + " is already assigned to another person");
            }

            if(person.getIdentificationCard() != null){
                person.getIdentificationCard().setPerson(null);
            }
            person.setIdentificationCard(identificationCard);
            identificationCard.setPerson(person);
        }
        else{
            if(person.getIdentificationCard() != null){
                person.getIdentificationCard().setPerson(null);
                person.setIdentificationCard(null);
            }
        }
        personDao.updatePerson(id, person);
        return mapToDto(person);
    }

    @Override
    public void deletePerson(Long id) throws DAOException {
        Person person = personDao.getPersonById(id);
        if(person == null) {
            throw new DAOException("Person with id " + id + " does not exist");
        }

        if(person.getIdentificationCard() != null){
            IdentificationCard identificationCard = person.getIdentificationCard();
            identificationCard.setPerson(null);
            identificationCardDao.updateIdentificationCard(identificationCard.getId(), identificationCard);
            person.setIdentificationCard(null);
        }
        personDao.deletePerson(id);
    }

    @Override
    public Long getIdentificationCardIdForPerson(Long personId) throws DAOException {
        Person person = personDao.getPersonById(personId);
        if(person == null){
            throw new DAOException("Person with id " + personId + " does not exist");
        }
        IdentificationCard identificationCard = person.getIdentificationCard();
        if(identificationCard == null){
            throw new DAOException("Person with id " + personId + " does not have an identification card");
        }
        return identificationCard.getId();
    }
}
