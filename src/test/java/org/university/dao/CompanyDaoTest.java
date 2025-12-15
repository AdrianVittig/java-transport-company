package org.university.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.university.configuration.SessionFactoryUtil;
import org.university.dto.CompanyEmployeeDto;
import org.university.dto.CompanyEmployeesDto;
import org.university.dto.EmployeeDto;
import org.university.entity.Company;
import org.university.entity.Employee;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CompanyDaoTest {
    private static SessionFactory sessionFactory;
    private static CompanyDao companyDao;

    @BeforeAll
    static void init(){
        sessionFactory = SessionFactoryUtil.getSessionFactory();
        companyDao = new CompanyDao();
    }

    @BeforeEach
    void dropDataFromTables() {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            session.createQuery("DELETE FROM Transport").executeUpdate();
            session.createQuery("DELETE FROM Vehicle").executeUpdate();
            session.createQuery("DELETE FROM Employee").executeUpdate();
            session.createQuery("DELETE FROM Company").executeUpdate();

            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    private Company buildCompany(String name, BigDecimal revenue) {
        return Company.builder()
                .name(name)
                .revenue(revenue)
                .employeeSet(new HashSet<>())
                .transportSet(new HashSet<>())
                .vehicleSet(new HashSet<>())
                .build();
    }

    private Employee buildEmployee(String firstName, String lastName, LocalDate birthDate, BigDecimal salary) {
        Employee e = new Employee();
        e.setFirstName(firstName);
        e.setLastName(lastName);
        e.setBirthDate(birthDate);
        e.setSalary(salary);
        return e;
    }

    @Test
    void createCompany() {
        Company company = Company.builder()
                .name("Test")
                .revenue(BigDecimal.TEN)
                .employeeSet(Set.of())
                .transportSet(Set.of())
                .vehicleSet(Set.of())
                .build();

        companyDao.createCompany(company);

        assertEquals(1, companyDao.getAllCompanies().size());
        assertEquals(company, companyDao.getAllCompanies().get(0));
    }

    @Test
    void getCompanyById() {
        Company company = Company.builder()
                .name("Nestle")
                .revenue(BigDecimal.TEN)
                .employeeSet(Set.of())
                .transportSet(Set.of())
                .vehicleSet(Set.of())
                .build();

        companyDao.createCompany(company);
        Company savedCompany = companyDao.getCompanyById(company.getId());

        assertNotNull(savedCompany);
        assertEquals(company, savedCompany);
    }

    @Test
    void getAllCompanies() {
        Company company1 = Company.builder()
                .name("Budweiser")
                .revenue(BigDecimal.TEN)
                .employeeSet(Set.of())
                .transportSet(Set.of())
                .vehicleSet(Set.of())
                .build();

        Company company2 = Company.builder()
                .name("Reitnzeichen")
                .revenue(BigDecimal.valueOf(1500))
                .employeeSet(Set.of())
                .transportSet(Set.of())
                .vehicleSet(Set.of())
                .build();

        companyDao.createCompany(company1);
        companyDao.createCompany(company2);

        List<Company> companies = companyDao.getAllCompanies();
        assertEquals(2, companies.size());
    }

    @Test
    void updateCompany() {
        Company company1 = Company.builder()
                .name("Oreo")
                .revenue(BigDecimal.TEN)
                .employeeSet(Set.of())
                .transportSet(Set.of())
                .vehicleSet(Set.of())
                .build();
        companyDao.createCompany(company1);

        company1.setName("Coca Cola");
        companyDao.updateCompany(company1.getId(), company1);

        Company newName = companyDao.getCompanyById(company1.getId());
        assertEquals("Coca Cola", newName.getName());
    }

    @Test
    void deleteCompany() {
        Company company = Company.builder()
                .name("Apple")
                .revenue(BigDecimal.TEN)
                .employeeSet(Set.of())
                .transportSet(Set.of())
                .vehicleSet(Set.of())
                .build();
        companyDao.createCompany(company);

        companyDao.deleteCompany(company.getId());
        assertTrue(companyDao.getAllCompanies().isEmpty());
        assertEquals(0, companyDao.getAllCompanies().size());
    }



    @Test
    void getCompanyWithEmployees() {
        Company company = Company.builder()
                .name("Milka")
                .revenue(BigDecimal.TEN)
                .employeeSet(new HashSet<>())
                .transportSet(new HashSet<>())
                .vehicleSet(new HashSet<>())
                .build();

        Employee e1 = new Employee();
        e1.setFirstName("Ivan");
        e1.setLastName("Ivanov");
        e1.setBirthDate(LocalDate.of(2000, 1, 1));
        e1.setSalary(BigDecimal.valueOf(1000));

        Employee e2 = new Employee();
        e2.setFirstName("Maria");
        e2.setLastName("Petrova");
        e2.setBirthDate(LocalDate.of(1999, 5, 10));
        e2.setSalary(BigDecimal.valueOf(1200));

        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            session.persist(company);

            e1.setCompany(company);
            e2.setCompany(company);

            session.persist(e1);
            session.persist(e2);

            company.getEmployeeSet().add(e1);
            company.getEmployeeSet().add(e2);

            session.getTransaction().commit();
        } finally {
            session.close();
        }

        List<CompanyEmployeeDto> dtos = companyDao.getCompanyWithEmployees(company.getId());

        assertNotNull(dtos);
        assertEquals(2, dtos.size());

        assertTrue(dtos.stream().allMatch(d -> d.getCompanyId() == company.getId()));
        assertTrue(dtos.stream().allMatch(d -> d.getName().equals("Milka")));

        List<String> firstNames = dtos.stream().map(CompanyEmployeeDto::getEmployeeFirstName).toList();
        assertTrue(firstNames.contains("Ivan"));
        assertTrue(firstNames.contains("Maria"));

        List<String> lastNames = dtos.stream().map(CompanyEmployeeDto::getEmployeeLastName).toList();
        assertTrue(lastNames.contains("Ivanov"));
        assertTrue(lastNames.contains("Petrova"));
    }

    @Test
    void getCompanyWithEmployeesFetch() {
        Company company = Company.builder()
                .name("Coca Cola")
                .revenue(BigDecimal.valueOf(500))
                .employeeSet(new HashSet<>())
                .transportSet(new HashSet<>())
                .vehicleSet(new HashSet<>())
                .build();

        Employee e1 = new Employee();
        e1.setFirstName("Georgi");
        e1.setLastName("Georgiev");
        e1.setBirthDate(LocalDate.of(1998, 3, 3));
        e1.setSalary(BigDecimal.valueOf(900));

        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            session.persist(company);

            e1.setCompany(company);
            session.persist(e1);

            company.getEmployeeSet().add(e1);

            session.getTransaction().commit();
        } finally {
            session.close();
        }

        CompanyEmployeesDto dto = companyDao.getCompanyWithEmployeesFetch(company.getId());

        assertNotNull(dto);
        assertEquals(company.getId(), dto.getCompanyId());
        assertEquals("Coca Cola", dto.getName());

        assertNotNull(dto.getEmployees());
        assertEquals(1, dto.getEmployees().size());

        EmployeeDto emp = dto.getEmployees().iterator().next();
        assertEquals("Georgi", emp.getFirstName());
        assertEquals("Georgiev", emp.getLastName());
    }

    @Test
    void createCompanyAndEmployee() {
        Company company = Company.builder()
                .name("Milka")
                .revenue(BigDecimal.valueOf(100))
                .employeeSet(new HashSet<>())
                .transportSet(new HashSet<>())
                .vehicleSet(new HashSet<>())
                .build();

        Employee employee = new Employee();
        employee.setFirstName("Stoyan");
        employee.setLastName("Stoyanov");
        employee.setBirthDate(LocalDate.of(2001, 2, 2));
        employee.setSalary(BigDecimal.valueOf(800));

        companyDao.createCompanyAndEmployee(company, employee);

        Session session = sessionFactory.openSession();
        try {
            Long companyCount = session.createQuery("SELECT COUNT(c) FROM Company c", Long.class).getSingleResult();
            Long employeeCount = session.createQuery("SELECT COUNT(e) FROM Employee e", Long.class).getSingleResult();

            assertEquals(1L, companyCount);
            assertEquals(1L, employeeCount);

            Employee savedEmployee = session.createQuery("SELECT e FROM Employee e", Employee.class).getSingleResult();
            assertNotNull(savedEmployee.getCompany());
            assertEquals(company.getId(), savedEmployee.getCompany().getId());
        } finally {
            session.close();
        }

        List<CompanyEmployeeDto> dtos = companyDao.getCompanyWithEmployees(company.getId());
        assertEquals(1, dtos.size());
        assertEquals("Stoyan", dtos.get(0).getEmployeeFirstName());
        assertEquals("Stoyanov", dtos.get(0).getEmployeeLastName());
    }

    @Test
    void sortCompaniesByNameAscending() {
        Company company1 = Company.builder()
                .name("Watch").revenue(BigDecimal.TEN)
                .vehicleSet(Set.of())
                .employeeSet(Set.of())
                .transportSet(Set.of())
                .build();

        Company company2 = Company.builder()
                .name("Laptop").revenue(BigDecimal.TEN)
                .vehicleSet(Set.of())
                .employeeSet(Set.of())
                .transportSet(Set.of())
                .build();

        companyDao.createCompany(company1);
        companyDao.createCompany(company2);

        List<Company> companies = companyDao.sortCompaniesByNameAscending(true);
        assertEquals("Laptop", companies.get(0).getName());
    }

    @Test
    void sortCompaniesByRevenue() {
        Company company1 = Company.builder()
                .name("Lowest").revenue(BigDecimal.TEN)
                .vehicleSet(Set.of())
                .employeeSet(Set.of())
                .transportSet(Set.of())
                .build();

        Company company2 = Company.builder()
                .name("Mid").revenue(BigDecimal.valueOf(1500))
                .vehicleSet(Set.of())
                .employeeSet(Set.of())
                .transportSet(Set.of())
                .build();

        Company company3 = Company.builder()
                .name("Highest").revenue(BigDecimal.valueOf(4000))
                .vehicleSet(Set.of())
                .employeeSet(Set.of())
                .transportSet(Set.of())
                .build();

        companyDao.createCompany(company1);
        companyDao.createCompany(company2);
        companyDao.createCompany(company3);

        List<Company> asc = companyDao.sortCompaniesByRevenue(true);
        assertEquals(List.of("Lowest", "Mid", "Highest"),
                asc.stream().map(Company::getName).toList());

        List<Company> desc = companyDao.sortCompaniesByRevenue(false);
        assertEquals(List.of("Highest", "Mid", "Lowest"),
                desc.stream().map(Company::getName).toList());
    }

    @Test
    void filterByName() {
        Company company1 = Company.builder()
                .name("Nestle").revenue(BigDecimal.TEN)
                .vehicleSet(Set.of())
                .employeeSet(Set.of())
                .transportSet(Set.of())
                .build();

        Company company2 = Company.builder()
                .name("Milka").revenue(BigDecimal.TEN)
                .vehicleSet(Set.of())
                .employeeSet(Set.of())
                .transportSet(Set.of())
                .build();

        Company company3 = Company.builder()
                .name("Ariel").revenue(BigDecimal.TEN)
                .vehicleSet(Set.of())
                .employeeSet(Set.of())
                .transportSet(Set.of())
                .build();

        companyDao.createCompany(company1);
        companyDao.createCompany(company2);
        companyDao.createCompany(company3);

        List<Company> result = companyDao.filterByName("Mil");

        assertEquals(1, result.size());

        List<String> names = result.stream().map(Company::getName).toList();
        assertTrue(names.contains("Milka"));
        assertFalse(names.contains("Nestle"));
        assertFalse(names.contains("Ariel"));
    }

    @Test
    void filterByRevenue() {
        Company company1 = Company.builder()
                .name("Nestle").revenue(BigDecimal.TEN)
                .vehicleSet(Set.of()).employeeSet(Set.of()).transportSet(Set.of())
                .build();

        Company company2 = Company.builder()
                .name("Milka").revenue(BigDecimal.valueOf(1500))
                .vehicleSet(Set.of()).employeeSet(Set.of()).transportSet(Set.of())
                .build();

        Company company3 = Company.builder()
                .name("Ariel").revenue(BigDecimal.valueOf(3500))
                .vehicleSet(Set.of()).employeeSet(Set.of()).transportSet(Set.of())
                .build();

        companyDao.createCompany(company1);
        companyDao.createCompany(company2);
        companyDao.createCompany(company3);

        List<Company> result = companyDao.filterByRevenue(BigDecimal.valueOf(1000));

        assertEquals(2, result.size());

        List<String> namesSorted = result.stream()
                .map(Company::getName)
                .sorted()
                .toList();

        assertEquals(List.of("Ariel", "Milka"), namesSorted);
    }
}
