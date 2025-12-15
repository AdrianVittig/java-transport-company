package org.university.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.university.configuration.SessionFactoryUtil;
import org.university.entity.Customer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDaoTest {

    private static SessionFactory sessionFactory;
    private static CustomerDao customerDao;

    @BeforeAll
    static void init() {
        sessionFactory = SessionFactoryUtil.getSessionFactory();
        customerDao = new CustomerDao();
    }

    @BeforeEach
    void dropDataFromTables() {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            session.createQuery("DELETE FROM Transport").executeUpdate();
            session.createQuery("DELETE FROM Vehicle").executeUpdate();
            session.createQuery("DELETE FROM Employee").executeUpdate();
            session.createQuery("DELETE FROM Customer").executeUpdate();
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

    private Customer buildCustomer(String firstName, String lastName, LocalDate dateOfBirth, BigDecimal budget) {
        Customer c = new Customer();
        c.setFirstName(firstName);
        c.setLastName(lastName);
        c.setBirthDate(dateOfBirth);
        c.setBudget(budget);
        c.setTransportSet(new HashSet<>());
        return c;
    }

    @Test
    void createCustomer() {
        Customer customer = buildCustomer("Ivan", "Ivanov", LocalDate.of(2000, 1, 1), BigDecimal.valueOf(1000));

        customerDao.createCustomer(customer);

        List<Customer> all = customerDao.getAllCustomers();
        assertEquals(1, all.size());

        Customer saved = all.get(0);
        assertNotNull(saved.getId());
        assertEquals("Ivan", saved.getFirstName());
        assertEquals("Ivanov", saved.getLastName());
        assertEquals(LocalDate.of(2000, 1, 1), saved.getBirthDate());
        assertEquals(0, BigDecimal.valueOf(1000).compareTo(saved.getBudget()));
    }

    @Test
    void getCustomerById() {
        Customer customer = buildCustomer("Maria", "Petrova", LocalDate.of(1999, 5, 10), BigDecimal.valueOf(500));

        customerDao.createCustomer(customer);

        Customer found = customerDao.getCustomerById(customer.getId());
        assertNotNull(found);
        assertEquals(customer.getId(), found.getId());
        assertEquals("Maria", found.getFirstName());
        assertEquals("Petrova", found.getLastName());
        assertEquals(LocalDate.of(1999, 5, 10), found.getBirthDate());
        assertEquals(0, BigDecimal.valueOf(500).compareTo(found.getBudget()));
    }

    @Test
    void getAllCustomers() {
        customerDao.createCustomer(buildCustomer("Nikola", "Georgiev", LocalDate.of(2001, 1, 1), BigDecimal.valueOf(10)));
        customerDao.createCustomer(buildCustomer("Ivan", "Petrov", LocalDate.of(2002, 2, 2), BigDecimal.valueOf(20)));

        List<Customer> all = customerDao.getAllCustomers();
        assertEquals(2, all.size());

        List<String> firstNames = all.stream().map(Customer::getFirstName).toList();
        assertTrue(firstNames.contains("Nikola"));
        assertTrue(firstNames.contains("Ivan"));
    }

    @Test
    void updateCustomer() {
        Customer customer = buildCustomer("Georgi", "Georgiev", LocalDate.of(1998, 3, 3), BigDecimal.valueOf(100));
        customerDao.createCustomer(customer);

        Customer updated = buildCustomer("Georgi", "Updated", LocalDate.of(1998, 3, 3), BigDecimal.valueOf(999));
        updated.setId(customer.getId());

        customerDao.updateCustomer(customer.getId(), updated);

        Customer after = customerDao.getCustomerById(customer.getId());
        assertNotNull(after);
        assertEquals("Georgi", after.getFirstName());
        assertEquals("Updated", after.getLastName());
        assertEquals(LocalDate.of(1998, 3, 3), after.getBirthDate());
        assertEquals(0, BigDecimal.valueOf(999).compareTo(after.getBudget()));
    }

    @Test
    void deleteCustomer() {
        Customer customer = buildCustomer("Maria", "Petkova", LocalDate.of(2000, 1, 1), BigDecimal.valueOf(123));
        customerDao.createCustomer(customer);

        customerDao.deleteCustomer(customer.getId());

        assertTrue(customerDao.getAllCustomers().isEmpty());
    }
}
