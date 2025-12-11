package org.university.service.impl.id_card_service_impl;

import org.university.dao.IdentificationCardDao;
import org.university.dao.PersonDao;
import org.university.dto.IdentificationCardDto;
import org.university.entity.IdentificationCard;
import org.university.entity.Person;
import org.university.exception.DAOException;
import org.university.service.contract.id_card_service.IdCardService;

import java.util.Set;
import java.util.stream.Collectors;

public class IdCardServiceImpl implements IdCardService {
    private final IdentificationCardDao identificationCardDao;
    private final PersonDao personDao;

    public IdCardServiceImpl(IdentificationCardDao identificationCardDao, PersonDao personDao) {
        this.identificationCardDao = identificationCardDao;
        this.personDao = personDao;
    }

    @Override
    public IdentificationCard mapToEntity(IdentificationCardDto cardDto) {
        IdentificationCard identificationCard = new IdentificationCard();
        identificationCard.setCardNumber(cardDto.getCardNumber());
        identificationCard.setIssueDate(cardDto.getIssueDate());
        identificationCard.setExpiryDate(cardDto.getExpiryDate());
        return identificationCard;
    }

    @Override
    public IdentificationCardDto mapToDto(IdentificationCard card) {
        IdentificationCardDto cardDto = new IdentificationCardDto();
        cardDto.setId(card.getId());
        cardDto.setCardNumber(card.getCardNumber());
        cardDto.setIssueDate(card.getIssueDate());
        cardDto.setExpiryDate(card.getExpiryDate());
        if(card.getPerson() != null){
            cardDto.setPersonId(card.getPerson().getId());
        }
        return cardDto;
    }

    @Override
    public IdentificationCardDto createIdentificationCard(IdentificationCardDto cardDto) throws DAOException {
        IdentificationCard identificationCard = mapToEntity(cardDto);
        identificationCardDao.createIdentificationCard(identificationCard);
        return mapToDto(identificationCard);
    }

    @Override
    public IdentificationCardDto getIdentificationCardById(Long id) throws DAOException {
        IdentificationCard identificationCard = identificationCardDao.getIdentificationCardById(id);
        if(identificationCard == null) {
            throw new DAOException("Identification Card with id " + id + " does not exist");
        }
        return mapToDto(identificationCard);
    }

    @Override
    public Set<IdentificationCardDto> getAllIdentificationCards() {
        return identificationCardDao.getAllIdentificationCards()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toSet());
    }

    @Override
    public IdentificationCardDto updateIdentificationCard(Long id, IdentificationCardDto cardDto) throws DAOException {
        IdentificationCard identificationCard = identificationCardDao.getIdentificationCardById(id);
        if(identificationCard == null) {
            throw new DAOException("Identification Card with id " + id + " does not exist");
        }
        identificationCard.setCardNumber(cardDto.getCardNumber());
        identificationCard.setIssueDate(cardDto.getIssueDate());
        identificationCard.setExpiryDate(cardDto.getExpiryDate());
        identificationCardDao.updateIdentificationCard(id, identificationCard);
        return mapToDto(identificationCard);
    }

    @Override
    public void deleteIdentificationCard(Long id) throws DAOException {
        IdentificationCard identificationCard = identificationCardDao.getIdentificationCardById(id);
        if(identificationCard == null) {
            throw new DAOException("Identification Card with id " + id + " does not exist");
        }
        identificationCardDao.deleteIdentificationCard(id);
    }

    @Override
    public void assignIdCardToPerson(Long cardId, Long personId) throws DAOException {
        IdentificationCard identificationCard = identificationCardDao.getIdentificationCardById(cardId);
        if(identificationCard == null){
            throw new DAOException("Identification Card with id " + cardId + " does not exist");
        }

        Person person = personDao.getPersonById(personId);
        if(person == null){
            throw new DAOException("Person with id " + personId + " does not exist");
        }

        if(identificationCard.getPerson() != null
        && !identificationCard.getPerson().getId().equals(personId)){
            throw new DAOException("Identification Card with id " + cardId + " is already assigned to person with id " + identificationCard.getPerson().getId());
        }

        if(person.getIdentificationCard() != null
                && person.getIdentificationCard().getId().equals(cardId)){
            return;
        }

        if(person.getIdentificationCard() != null &&
                !person.getIdentificationCard().getId().equals(cardId)){
            throw new DAOException("Person with id " + personId + " is already assigned to another identification card");
        }

        identificationCard.setPerson(person);
        person.setIdentificationCard(identificationCard);

        identificationCardDao.updateIdentificationCard(cardId, identificationCard);
    }

    @Override
    public void removeIdCardFromPerson(Long cardId, Long personId) throws DAOException {
        IdentificationCard identificationCard = identificationCardDao.getIdentificationCardById(cardId);
        if(identificationCard == null){
            throw new DAOException("Identification Card with id " + cardId + " does not exist");
        }

        Person person = personDao.getPersonById(personId);
        if(person == null){
            throw new DAOException("Person with id " + personId + " does not exist");
        }

        if(person.getIdentificationCard() == null || !person.getIdentificationCard().getId().equals(cardId)){
            throw new DAOException("Person with id " + personId + " is not assigned to identification card with id " + cardId);
        }

        identificationCard.setPerson(null);
        person.setIdentificationCard(null);

        identificationCardDao.updateIdentificationCard(cardId, identificationCard);
    }

    @Override
    public Long getPersonIdForIdCard(Long cardId) throws DAOException {
        IdentificationCard identificationCard = identificationCardDao.getIdentificationCardById(cardId);
        if(identificationCard == null){
            throw new DAOException("Identification Card with id " + cardId + " does not exist");
        }

        Person person = identificationCard.getPerson();
        if(person == null){
            throw new DAOException("Identification card with id " + cardId +
                    " is not assigned to any person");
        }

        return person.getId();
    }
}
