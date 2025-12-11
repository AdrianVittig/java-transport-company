package org.university.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.university.configuration.SessionFactoryUtil;
import org.university.entity.Employee;
import org.university.exception.DAOException;

import java.util.List;

public class EmployeeDao {
    public void createEmployee(Employee employee) throws DAOException {
        Transaction transaction = null;
        Session session = null;
        try {
            session = SessionFactoryUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(employee);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DAOException("Failed to create employee: " + e.getMessage());
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    public Employee getEmployeeById(long id) {
        Session session = null;
        try{
            session = SessionFactoryUtil.getSessionFactory().openSession();
            return session.find(Employee.class, id);
        }finally {
                if(session != null && session.isOpen()){
                    session.close();
                }
            }
    }

    public List<Employee> getAllEmployees() {
        Session session = null;
        try{
            session = SessionFactoryUtil.getSessionFactory().openSession();
            return session.createQuery("SELECT e FROM Employee e", Employee.class)
                    .getResultList();
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    public void updateEmployee(long id, Employee updated) throws DAOException {
        Transaction transaction = null;
        Session session = null;
        try {
            session = SessionFactoryUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            Employee employee = session.find(Employee.class, id);
            if (employee == null) {
                throw new DAOException("Employee with id " + id + " not found");
            }

            employee.setFirstName(updated.getFirstName());
            employee.setLastName(updated.getLastName());
            employee.setBirthDate(updated.getBirthDate());
            employee.setDriverQualifications(updated.getDriverQualifications());
            employee.setDrivingLicense(updated.getDrivingLicense());
            employee.setCompany(updated.getCompany());
            employee.setSalary(updated.getSalary());

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
            throw new DAOException("Failed to update employee: " + e.getMessage());
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    public void deleteEmployee(long id) throws DAOException {
        Transaction transaction = null;
        Session session = null;
        try {
            session = SessionFactoryUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            Employee employee = session.find(Employee.class, id);
            if (employee == null) {
                throw new DAOException("Employee with id " + id + " not found");
            }

            session.remove(employee);
            transaction.commit();
        } catch (DAOException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } catch (Exception e) {
            if (transaction != null){
                transaction.rollback();
            }
            throw new DAOException("Failed to delete employee: " + e.getMessage());
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    public int countEmployeesByCompanyId(Long companyId){
        Session session = null;
        try{
            session = SessionFactoryUtil.getSessionFactory().openSession();
            Long count = session.createQuery(
                    "SELECT COUNT(e) FROM Employee e WHERE e.company.id = :companyId",
                    Long.class)
                    .setParameter("companyId", companyId)
                    .uniqueResult();
            return count != null ? count.intValue() : 0;
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }
}
