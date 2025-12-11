package org.university.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.university.configuration.SessionFactoryUtil;
import org.university.entity.Customer;
import org.university.exception.DAOException;

import java.util.List;

public class CustomerDao {
    public void createCustomer(Customer customer) throws DAOException {
        Session session = null;
        Transaction transaction = null;
        try {
            session = SessionFactoryUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(customer);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null){
                transaction.rollback();
            }
            throw new DAOException("Failed to create customer: " + e.getMessage());
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    public Customer getCustomerById(long id) {
        Session session = null;
        try{
            session = SessionFactoryUtil.getSessionFactory().openSession();
            return session.find(Customer.class, id);
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    public List<Customer> getAllCustomers() {
        Session session = null;
        try{
            session = SessionFactoryUtil.getSessionFactory().openSession();
            return session.createQuery("SELECT c FROM Customer c", Customer.class)
                    .getResultList();
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    public void updateCustomer(long id, Customer updated) throws DAOException {
        Transaction transaction = null;
        Session session = null;
        try {
            session = SessionFactoryUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            Customer customer = session.find(Customer.class, id);
            if (customer == null) {
                throw new DAOException("Customer with id " + id + " not found");
            }

            customer.setFirstName(updated.getFirstName());
            customer.setLastName(updated.getLastName());
            customer.setBirthDate(updated.getBirthDate());
            customer.setBudget(updated.getBudget());

            transaction.commit();
        } catch (DAOException e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new DAOException("Failed to update customer: " + e.getMessage());
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    public void deleteCustomer(long id) throws DAOException {
        Transaction transaction = null;
        Session session = null;
        try {
            session = SessionFactoryUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            Customer customer = session.find(Customer.class, id);
            if (customer == null) {
                throw new DAOException("Customer with id " + id + " not found");
            }

            session.remove(customer);

            transaction.commit();
        } catch (DAOException e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new DAOException("Failed to delete customer: " + e.getMessage());
        } finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }
}
