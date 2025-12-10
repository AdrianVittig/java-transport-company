package org.university.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.university.configuration.SessionFactoryUtil;
import org.university.entity.Transport;
import org.university.exception.DAOException;

import java.util.List;

public class TransportDao {
    public void createTransport(Transport transport) throws DAOException {
        Transaction transaction = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(transport);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DAOException("Failed to create transport: " + e.getMessage());
        }
    }

    public Transport getTransportById(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Transport.class, id);
        }
    }

    public List<Transport> getAllTransports() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT t FROM Transport t", Transport.class)
                    .getResultList();
        }
    }

    public void updateTransport(long id, Transport updated) throws DAOException {
        Transaction transaction = null;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Transport t = session.find(Transport.class, id);
            if (t == null) {
                throw new DAOException("Transport with id " + id + " not found");
            }

            t.setStartPoint(updated.getStartPoint());
            t.setEndPoint(updated.getEndPoint());
            t.setDepartureDate(updated.getDepartureDate());
            t.setArrivalDate(updated.getArrivalDate());
            t.setCargoType(updated.getCargoType());
            t.setCompany(updated.getCompany());
            t.setEmployee(updated.getEmployee());
            t.setCustomer(updated.getCustomer());
            t.setPaid(updated.isPaid());
            t.setQuantity(updated.getQuantity());
            t.setInitPrice(updated.getInitPrice());
            t.setTotalPrice(updated.getTotalPrice());
            t.setVehicle(updated.getVehicle());

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
            throw new DAOException("Failed to update transport: " + e.getMessage());
        }
    }

    public void deleteTransport(long id) throws DAOException {
        Transaction transaction = null;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Transport t = session.find(Transport.class, id);
            if (t == null) {
                throw new DAOException("Transport with id " + id + " not found");
            }

            session.remove(t);

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
            throw new DAOException("Failed to delete transport: " + e.getMessage());
        }
    }
}
