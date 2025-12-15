package org.university.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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
import org.university.validators.ValidateNames;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
public class CompanyDao {
    public void createCompany(Company company) throws DAOException {
        Transaction transaction = null;
        Session session = null;
        try{
            session = SessionFactoryUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(company);
            transaction.commit();
        }catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new DAOException("Failed to create company: " + e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public Company getCompanyById(long id){
        Session session = null;
        try{
            session = SessionFactoryUtil.getSessionFactory().openSession();
            return session.find(Company.class,id);
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    public List<Company> getAllCompanies(){
        Session session = null;
        try{
            session = SessionFactoryUtil.getSessionFactory().openSession();
            return session.createQuery("SELECT c FROM Company c", Company.class).getResultList();
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }

    }

    public void updateCompany(long id, Company company) throws DAOException {
        Transaction transaction = null;
        Session session = null;
        try{
            session = SessionFactoryUtil.getSessionFactory().openSession();
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
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    public void deleteCompany(long id) throws DAOException {
        Transaction transaction = null;
        Session session = null;
        try{
            session = SessionFactoryUtil.getSessionFactory().openSession();
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
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    public List<CompanyEmployeeDto> getCompanyWithEmployees(long id) {
        Session session = null;
        try{
            session = SessionFactoryUtil.getSessionFactory().openSession();
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
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    public CompanyEmployeesDto getCompanyWithEmployeesFetch(long id) {
        Session session = null;
        try{
            session = SessionFactoryUtil.getSessionFactory().openSession();
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
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    public void createCompanyAndEmployee(Company company, Employee employee) throws DAOException {
        Session session = null;
        Transaction transaction = null;
        try {
        session = SessionFactoryUtil.getSessionFactory().openSession();
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
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    public List<Company> sortCompaniesByNameAscending(boolean ascending){
        Session session = null;
        try{
            session = SessionFactoryUtil.getSessionFactory().openSession();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Company> criteriaQuery = criteriaBuilder.createQuery(Company.class);
            Root<Company> root = criteriaQuery.from(Company.class);

            criteriaQuery.select(root).
                    orderBy(ascending ?
                            criteriaBuilder.asc(root.get("name")) : criteriaBuilder.desc(root.get("name")));

            return session.createQuery(criteriaQuery).getResultList();
        }catch(DAOException e){
            throw e;
        }catch(Exception e){
            throw new DAOException("Failed to sort companies by name: " + e.getMessage());
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    public List<Company> sortCompaniesByRevenue(boolean ascending){
        Session session = null;
        try{
            session = SessionFactoryUtil.getSessionFactory().openSession();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Company> criteriaQuery = criteriaBuilder.createQuery(Company.class);
            Root<Company> root = criteriaQuery.from(Company.class);

            criteriaQuery.select(root)
                    .orderBy(ascending ?
                            criteriaBuilder.asc(root.get("revenue")) : criteriaBuilder.desc(root.get("revenue")));

            return session.createQuery(criteriaQuery).getResultList();
        }catch(DAOException e){
            throw e;
        }catch(Exception e){
            throw new DAOException("Failed to sort companies by revenue: " + e.getMessage());
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    public List<Company> filterByName(String name){
        Session session = null;
        try{
            session = SessionFactoryUtil.getSessionFactory().openSession();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Company> criteriaQuery = criteriaBuilder.createQuery(Company.class);
            Root<Company> root = criteriaQuery.from(Company.class);

            Predicate nameEqualsTo = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"
            );

            criteriaQuery.select(root).where(nameEqualsTo);
            return session.createQuery(criteriaQuery).getResultList();
        }catch(DAOException e){
            throw e;
        }catch(Exception e){
            throw new DAOException("Failed to filter companies by name: " + e.getMessage());
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    public List<Company> filterByRevenue(BigDecimal minRevenue){
        Session session = null;
        try{
            session = SessionFactoryUtil.getSessionFactory().openSession();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Company> criteriaQuery = criteriaBuilder.createQuery(Company.class);
            Root<Company> root = criteriaQuery.from(Company.class);

            criteriaQuery.select(root).
                    where(criteriaBuilder.greaterThanOrEqualTo(root.get("revenue"), minRevenue));


            return session.createQuery(criteriaQuery).getResultList();
        }catch(DAOException e){
            throw e;
        }catch(Exception e){
            throw new DAOException("Failed to filter companies by revenue: " + e.getMessage());
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

}
