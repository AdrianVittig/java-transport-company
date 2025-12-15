package org.university.dao;

import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.university.configuration.SessionFactoryUtil;
import org.university.entity.Company;
import org.university.entity.DrivingLicense;
import org.university.entity.Employee;
import org.university.exception.DAOException;
import org.university.util.DriverQualifications;

import java.math.BigDecimal;
import java.util.List;

public class EmployeeDao {
    public void createEmployee(Employee employee) throws DAOException {
        if (employee == null) {
            throw new DAOException("Employee cannot be null");
        }

        Session session = SessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            if (employee.getCompany() != null && employee.getCompany().getId() != null) {
                Company company = session.find(Company.class, employee.getCompany().getId());
                employee.setCompany(company);
            }

            if (employee.getDrivingLicense() != null && employee.getDrivingLicense().getId() != null) {
                DrivingLicense drivingLicense = session.find(DrivingLicense.class, employee.getDrivingLicense().getId());
                employee.setDrivingLicense(drivingLicense);
            }

            session.persist(employee);
            transaction.commit();
        } catch (Exception exception) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new DAOException("Failed to create employee: " + exception.getMessage());
        } finally {
            session.close();
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

    public List<Employee> sortEmployeesBySalary(boolean ascending){
        Session session = null;
        try{
            session = SessionFactoryUtil.getSessionFactory().openSession();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
            Root<Employee> root = criteriaQuery.from(Employee.class);

            criteriaQuery.select(root)
                    .orderBy(ascending ?
                            criteriaBuilder.asc(root.get("salary"))
                            : criteriaBuilder.desc(root.get("salary")));

            return session.createQuery(criteriaQuery).getResultList();
        }catch(DAOException e){
            throw e;
        }catch(Exception e){
            throw new DAOException("Failed to sort employees by salary: " + e.getMessage());
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    public List<Employee> sortEmployeesByQualification(boolean ascending){
        Session session = null;
        try{
            session = SessionFactoryUtil.getSessionFactory().openSession();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
            Root<Employee> root = criteriaQuery.from(Employee.class);

            Join<Employee, DriverQualifications> qualificationsJoin =
                    root.join("driverQualifications", JoinType.LEFT);

            criteriaQuery.select(root).distinct(true)
                    .orderBy(ascending ? criteriaBuilder.asc(qualificationsJoin)
                            : criteriaBuilder.desc(qualificationsJoin));

            return session.createQuery(criteriaQuery).getResultList();
        }catch(DAOException e){
            throw e;
        }catch(Exception e){
            throw new DAOException("Failed to sort employees by qualification: " + e.getMessage());
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    public List<Employee> filterByMinSalary(BigDecimal minSalary){
        Session session = null;
        try{
            session = SessionFactoryUtil.getSessionFactory().openSession();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
            Root<Employee> root = criteriaQuery.from(Employee.class);

            Predicate greaterThanSalary =
                    criteriaBuilder.greaterThanOrEqualTo(root.get("salary"), minSalary);

            criteriaQuery.select(root).where(greaterThanSalary);
            return session.createQuery(criteriaQuery).getResultList();
        }catch(DAOException e){
            throw e;
        }catch(Exception e){
            throw new DAOException("Failed to filter employees by salary: " + e.getMessage());
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    public List<Employee> filterByQualification(DriverQualifications qualification){
        Session session = null;
        try{
            session = SessionFactoryUtil.getSessionFactory().openSession();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
            Root<Employee> root = criteriaQuery.from(Employee.class);

            Join<Employee, DriverQualifications> qualificationsJoin=
                    root.join("driverQualifications");

            criteriaQuery.select(root)
                    .where(criteriaBuilder.equal(qualificationsJoin, qualification));


            return session.createQuery(criteriaQuery).getResultList();
        }catch(DAOException e){
            throw e;
        }catch(Exception e){
            throw new DAOException("Failed to filter employees by qualification: " + e.getMessage());
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }
}
