package org.university.service.contract.id_card_service;

import org.university.dto.IdentificationCardDto;
import org.university.entity.IdentificationCard;
import org.university.exception.DAOException;

import java.util.Set;

public interface IdCardService {
    IdentificationCard mapToEntity(IdentificationCardDto cardDto);

    IdentificationCardDto mapToDto(IdentificationCard card);

    IdentificationCardDto createIdentificationCard(IdentificationCardDto cardDto) throws DAOException;

    IdentificationCardDto getIdentificationCardById(Long id) throws DAOException;

    Set<IdentificationCardDto> getAllIdentificationCards();

    IdentificationCardDto updateIdentificationCard(Long id, IdentificationCardDto cardDto) throws DAOException;

    void deleteIdentificationCard(Long id) throws DAOException;

    void assignIdCardToPerson(Long cardId, Long personId) throws DAOException;

    void removeIdCardFromPerson(Long cardId, Long personId) throws DAOException;

    Long getPersonIdForIdCard(Long cardId) throws DAOException;
}
