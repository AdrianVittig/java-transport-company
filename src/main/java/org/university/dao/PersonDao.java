package org.university.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.university.configuration.SessionFactoryUtil;
import org.university.entity.Person;
import org.university.exception.DAOException;

import java.util.List;

public class PersonDao {
    public void createPerson(Person person) throws DAOException {
        Transaction transaction = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(person);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DAOException("Failed to create person: " + e.getMessage());
        }
    }

    public Person getPersonById(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Person.class, id);
        }
    }

    public List<Person> getAllPeople() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT p FROM Person p", Person.class)
                    .getResultList();
        }
    }

    public void updatePerson(long id, Person updated) throws DAOException {
        Transaction transaction = null;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Person person = session.find(Person.class, id);
            if (person == null) {
                throw new DAOException("Person with id " + id + " not found");
            }

            person.setFirstName(updated.getFirstName());
            person.setLastName(updated.getLastName());
            person.setBirthDate(updated.getBirthDate());

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
            throw new DAOException("Failed to update person: " + e.getMessage());
        }
    }

    public void deletePerson(long id) throws DAOException {
        Transaction transaction = null;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Person person = session.find(Person.class, id);
            if (person == null) {
                throw new DAOException("Person with id " + id + " not found");
            }

            session.remove(person);

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
            throw new DAOException("Failed to delete person: " + e.getMessage());
        }
    }
}
