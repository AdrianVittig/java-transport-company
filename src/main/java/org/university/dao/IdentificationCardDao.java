package org.university.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.university.configuration.SessionFactoryUtil;
import org.university.entity.IdentificationCard;
import org.university.exception.DAOException;

import java.util.List;

public class IdentificationCardDao {
    public void createIdentificationCard(IdentificationCard card) throws DAOException {
        Transaction transaction = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(card);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DAOException("Failed to create identification card: " + e.getMessage());
        }
    }

    public IdentificationCard getIdentificationCardById(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(IdentificationCard.class, id);
        }
    }

    public List<IdentificationCard> getAllIdentificationCards() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT i FROM IdentificationCard i", IdentificationCard.class)
                    .getResultList();
        }
    }

    public void updateIdentificationCard(long id, IdentificationCard updated) throws DAOException {
        Transaction transaction = null;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            IdentificationCard card = session.find(IdentificationCard.class, id);
            if (card == null) {
                throw new DAOException("Identification card with id " + id + " not found");
            }

            card.setCardNumber(updated.getCardNumber());
            card.setIssueDate(updated.getIssueDate());
            card.setExpiryDate(updated.getExpiryDate());
            card.setPerson(updated.getPerson());

            transaction.commit();
        } catch (DAOException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DAOException("Failed to update identification card: " + e.getMessage());
        }
    }

    public void deleteIdentificationCard(long id) throws DAOException {
        Transaction transaction = null;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            IdentificationCard card = session.find(IdentificationCard.class, id);
            if (card == null) {
                throw new DAOException("Identification card with id " + id + " not found");
            }

            session.remove(card);

            transaction.commit();
        } catch (DAOException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DAOException("Failed to delete identification card: " + e.getMessage());
        }
    }
}
