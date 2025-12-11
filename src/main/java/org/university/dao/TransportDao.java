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
        Session session = null;
        try {
            session = SessionFactoryUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(transport);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DAOException("Failed to create transport: " + e.getMessage());
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    public Transport getTransportById(long id) {
        Session session = null;
        try {
            session = SessionFactoryUtil.getSessionFactory().openSession();
            return session.find(Transport.class, id);
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    public List<Transport> getAllTransports() {
        Session session = null;
        try {
            session = SessionFactoryUtil.getSessionFactory().openSession();
            return session.createQuery("SELECT t FROM Transport t", Transport.class)
                    .getResultList();
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    public void updateTransport(long id, Transport updated) throws DAOException {
        Transaction transaction = null;
        Session session = null;
        try{
            session = SessionFactoryUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            Transport transport = session.find(Transport.class, id);
            if (transport == null) {
                throw new DAOException("Transport with id " + id + " not found");
            }

            transport.setStartPoint(updated.getStartPoint());
            transport.setEndPoint(updated.getEndPoint());
            transport.setDepartureDate(updated.getDepartureDate());
            transport.setArrivalDate(updated.getArrivalDate());
            transport.setCargoType(updated.getCargoType());
            transport.setCompany(updated.getCompany());
            transport.setEmployee(updated.getEmployee());
            transport.setCustomer(updated.getCustomer());
            transport.setPaymentStatus(updated.getPaymentStatus());
            transport.setQuantity(updated.getQuantity());
            transport.setInitPrice(updated.getInitPrice());
            transport.setTotalPrice(updated.getTotalPrice());
            transport.setVehicle(updated.getVehicle());

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
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    public void deleteTransport(long id) throws DAOException {
        Transaction transaction = null;
        Session session = null;
        try {
            session = SessionFactoryUtil.getSessionFactory().openSession();
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
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }
}
