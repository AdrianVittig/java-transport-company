package org.university.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.university.configuration.SessionFactoryUtil;
import org.university.dto.CompanyEmployeeDto;
import org.university.dto.CompanyEmployeesDto;
import org.university.dto.EmployeeDto;
import org.university.entity.Company;
import org.university.entity.Employee;
import org.university.entity.Transport;
import org.university.entity.Vehicle;
import org.university.exception.DAOException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CompanyDao {
    public void createCompany(Company company) throws DAOException {
        Transaction transaction = null;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.persist(company);
            transaction.commit();
        }catch(Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            throw new DAOException("Failed to create company: " + e.getMessage());
        }
    }

    public Company getCompanyById(long id){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            return session.find(Company.class,id);
        }
    }

    public List<Company> getAllCompanies(){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            return session.createQuery("SELECT c FROM Company c", Company.class).getResultList();
        }
    }

    public void updateCompany(long id, Company company) throws DAOException {
        Transaction transaction = null;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            Company companyToUpdate = session.find(Company.class,id);
            if(companyToUpdate == null){
                throw new DAOException("Company with id " + id + " not found");
            }
            companyToUpdate.setName(company.getName());
            companyToUpdate.setRevenue(company.getRevenue());
            transaction.commit();
        }catch(DAOException e){
            if(transaction != null){
                transaction.rollback();
            }
            throw e;
        }
        catch(Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            throw new DAOException("Failed to update company: " + e.getMessage());
        }
    }

    public void deleteCompany(long id) throws DAOException {
        Transaction transaction = null;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            Company companyToDelete = session.find(Company.class,id);
            if(companyToDelete == null){
                throw new DAOException("Company with id " + id + " not found");
            }
            session.remove(companyToDelete);
            transaction.commit();
        }catch(DAOException e){
            if(transaction != null){
                transaction.rollback();
            }
            throw e;
        }
        catch(Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            throw new DAOException("Failed to delete company: " + e.getMessage());
        }
    }

    public List<CompanyEmployeeDto> getCompanyWithEmployees(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT new org.university.dto.CompanyEmployeeDto(" +
                                    "c.id, c.name, e.id, e.firstName, e.lastName) " +
                                    "FROM Employee e " +
                                    "JOIN e.company c " +
                                    "WHERE c.id = :id",
                            CompanyEmployeeDto.class
                    )
                    .setParameter("id", id)
                    .getResultList();
        }
    }

    public CompanyEmployeesDto getCompanyWithEmployeesFetch(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Company company = session.createQuery(
                            "SELECT c FROM Company c " +
                                    "JOIN FETCH c.employeeSet " +
                                    "WHERE c.id = :id",
                            Company.class)
                    .setParameter("id", id)
                    .getSingleResult();

            Set<EmployeeDto> employeeDtos = company.getEmployeeSet()
                    .stream()
                    .map(e -> new EmployeeDto(
                            e.getId(),
                            e.getFirstName(),
                            e.getLastName(),
                            e.getBirthDate(),
                            e.getDrivingLicense() != null ? e.getDrivingLicense().getId() : null,
                            e.getDriverQualifications(),
                            e.getCompany() != null ? e.getCompany().getId() : null,
                            e.getSalary(),
                            e.getVehicleSet() != null
                                    ? e.getVehicleSet().stream()
                                    .map(Vehicle::getId)
                                    .collect(Collectors.toSet())
                                    : null,
                            e.getTransportSet() != null
                                    ? e.getTransportSet().stream()
                                    .map(Transport::getId)
                                    .collect(Collectors.toSet())
                                    : null
                    ))
                    .collect(Collectors.toSet());

            return new CompanyEmployeesDto(company.getId(), company.getName(), employeeDtos);
        }
    }

    public void createCompanyAndEmployee(Company company, Employee employee) throws DAOException {
        Transaction transaction = null;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();

            session.persist(company);

            employee.setCompany(company);
            session.persist(employee);

            company.getEmployeeSet().add(employee);

            transaction.commit();
        } catch(Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            throw new DAOException("Failed to create company and employee: " + e.getMessage());
        }
    }
}
