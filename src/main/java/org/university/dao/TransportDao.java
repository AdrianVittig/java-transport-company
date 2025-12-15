package org.university.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.university.configuration.SessionFactoryUtil;
import org.university.entity.Company;
import org.university.entity.Customer;
import org.university.entity.Employee;
import org.university.entity.Transport;
import org.university.entity.Vehicle;
import org.university.exception.DAOException;
import org.university.util.PaymentStatus;

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
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new DAOException("Failed to create transport: " + e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public Transport getTransportById(long id) {
        Session session = null;
        try {
            session = SessionFactoryUtil.getSessionFactory().openSession();
            return session.find(Transport.class, id);
        } finally {
            if (session != null && session.isOpen()) {
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
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void updateTransport(long id, Transport updated) throws DAOException {
        Transaction transaction = null;
        Session session = null;
        try {
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
            transport.setPaymentStatus(updated.getPaymentStatus());
            transport.setQuantity(updated.getQuantity());
            transport.setInitPrice(updated.getInitPrice());
            transport.setTotalPrice(updated.getTotalPrice());

            if (updated.getCompany() != null && updated.getCompany().getId() != null) {
                transport.setCompany(session.getReference(Company.class, updated.getCompany().getId()));
            } else {
                transport.setCompany(null);
            }

            if (updated.getEmployee() != null && updated.getEmployee().getId() != null) {
                transport.setEmployee(session.getReference(Employee.class, updated.getEmployee().getId()));
            } else {
                transport.setEmployee(null);
            }

            if (updated.getCustomer() != null && updated.getCustomer().getId() != null) {
                transport.setCustomer(session.getReference(Customer.class, updated.getCustomer().getId()));
            } else {
                transport.setCustomer(null);
            }

            if (updated.getVehicle() != null && updated.getVehicle().getId() != null) {
                transport.setVehicle(session.getReference(Vehicle.class, updated.getVehicle().getId()));
            } else {
                transport.setVehicle(null);
            }

            transaction.commit();
        } catch (DAOException e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw e;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw new DAOException("Failed to update transport: " + e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void updatePaymentStatus(long id, PaymentStatus status) throws DAOException {
        Transaction transaction = null;
        Session session = null;
        try {
            session = SessionFactoryUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            Transport transport = session.find(Transport.class, id);
            if (transport == null) {
                throw new DAOException("Transport with id " + id + " not found");
            }

            transport.setPaymentStatus(status);

            transaction.commit();
        } catch (DAOException e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw e;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw new DAOException("Failed to update payment status: " + e.getMessage());
        } finally {
            if (session != null && session.isOpen()) session.close();
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
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw e;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) transaction.rollback();
            throw new DAOException("Failed to delete transport: " + e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public List<Transport> sortByDestinationAscending(boolean asc){
        Session session = null;
        try{
            session = SessionFactoryUtil.getSessionFactory().openSession();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Transport> criteriaQuery = criteriaBuilder.createQuery(Transport.class);
            Root<Transport> root = criteriaQuery.from(Transport.class);

            criteriaQuery.select(root)
                    .orderBy(asc ? criteriaBuilder.asc(root.get("endPoint"))
                            : criteriaBuilder.desc(root.get("endPoint")));

            return session.createQuery(criteriaQuery).getResultList();
        }catch(DAOException e){
            throw e;
        }catch(Exception e){
            throw new DAOException("Failed to sort transports by destination: " + e.getMessage());
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    public List<Transport> filterByDestination(String destination){
        Session session = null;
        try{
            session = SessionFactoryUtil.getSessionFactory().openSession();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Transport> criteriaQuery = criteriaBuilder.createQuery(Transport.class);
            Root<Transport> root = criteriaQuery.from(Transport.class);

            Predicate destinationEqualsTo = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("endPoint")),
                    "%" + destination.toLowerCase() + "%"
            );

            criteriaQuery.select(root).where(destinationEqualsTo);
            return session.createQuery(criteriaQuery).getResultList();
        }catch(DAOException e){
            throw e;
        }catch(Exception e){
            throw new DAOException("Failed to filter transports by destination: " + e.getMessage());
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }
}
