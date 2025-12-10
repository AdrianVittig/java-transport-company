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
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(employee);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DAOException("Failed to create employee: " + e.getMessage());
        }
    }

    public Employee getEmployeeById(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Employee.class, id);
        }
    }

    public List<Employee> getAllEmployees() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT e FROM Employee e", Employee.class)
                    .getResultList();
        }
    }

    public void updateEmployee(long id, Employee updated) throws DAOException {
        Transaction transaction = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
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
        }
    }

    public void deleteEmployee(long id) throws DAOException {
        Transaction transaction = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
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
        }
    }
}
