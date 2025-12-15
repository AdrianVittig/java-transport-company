package org.university.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.university.configuration.SessionFactoryUtil;
import org.university.entity.DrivingLicense;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DrivingLicenseDaoTest {
    private static SessionFactory sessionFactory;
    private static DrivingLicenseDao drivingLicenseDao;

    @BeforeAll
    static void init(){
        sessionFactory = SessionFactoryUtil.getSessionFactory();
        drivingLicenseDao = new DrivingLicenseDao();
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
            session.createQuery("DELETE FROM DrivingLicense").executeUpdate();

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

    private DrivingLicense buildLicense(String number, LocalDate issueDate, LocalDate expiryDate) {
        DrivingLicense dl = new DrivingLicense();
        dl.setDrivingLicenseNumber(number);
        dl.setIssueDate(issueDate);
        dl.setExpiryDate(expiryDate);
        return dl;
    }

    @Test
    void createDrivingLicense() {
        DrivingLicense drivingLicense = buildLicense("123456789",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2030, 1, 1));

        drivingLicenseDao.createDrivingLicense(drivingLicense);

        List<DrivingLicense> all = drivingLicenseDao.getAllDrivingLicenses();
        assertEquals(1, all.size());

        DrivingLicense saved = all.get(0);
        assertNotNull(saved.getId());
        assertEquals("123456789", saved.getDrivingLicenseNumber());
        assertEquals(LocalDate.of(2020, 1, 1), saved.getIssueDate());
        assertEquals(LocalDate.of(2030, 1, 1), saved.getExpiryDate());
    }

    @Test
    void getDrivingLicenseById() {
        DrivingLicense dl = buildLicense("267982", LocalDate.of(2021, 2, 2), LocalDate.of(2031, 2, 2));
        drivingLicenseDao.createDrivingLicense(dl);

        DrivingLicense found = drivingLicenseDao.getDrivingLicenseById(dl.getId());

        assertNotNull(found);
        assertEquals(dl.getId(), found.getId());
        assertEquals("267982", found.getDrivingLicenseNumber());
        assertEquals(LocalDate.of(2021, 2, 2), found.getIssueDate());
        assertEquals(LocalDate.of(2031, 2, 2), found.getExpiryDate());
    }

    @Test
    void getAllDrivingLicenses() {
        drivingLicenseDao.createDrivingLicense(buildLicense("798128798", LocalDate.of(2020, 1, 1), LocalDate.of(2030, 1, 1)));
        drivingLicenseDao.createDrivingLicense(buildLicense("745818798", LocalDate.of(2022, 2, 2), LocalDate.of(2032, 2, 2)));

        List<DrivingLicense> all = drivingLicenseDao.getAllDrivingLicenses();
        assertEquals(2, all.size());

        List<String> numbers = all.stream().map(DrivingLicense::getDrivingLicenseNumber).toList();
        assertTrue(numbers.contains("798128798"));
        assertTrue(numbers.contains("745818798"));
    }

    @Test
    void updateDrivingLicense() {
        DrivingLicense drivingLicense = buildLicense("132465798", LocalDate.of(2019, 3, 3), LocalDate.of(2029, 3, 3));
        drivingLicenseDao.createDrivingLicense(drivingLicense);

        DrivingLicense updated = buildLicense("987821348", LocalDate.of(2019, 3, 3), LocalDate.of(2035, 3, 3));
        updated.setId(drivingLicense.getId());

        drivingLicenseDao.updateDrivingLicense(drivingLicense.getId(), updated);

        DrivingLicense after = drivingLicenseDao.getDrivingLicenseById(drivingLicense.getId());
        assertNotNull(after);
        assertEquals("987821348", after.getDrivingLicenseNumber());
        assertEquals(LocalDate.of(2019, 3, 3), after.getIssueDate());
        assertEquals(LocalDate.of(2035, 3, 3), after.getExpiryDate());
    }

    @Test
    void deleteDrivingLicense() {
        DrivingLicense drivingLicense = buildLicense("6465798", LocalDate.of(2018, 4, 4), LocalDate.of(2028, 4, 4));
        drivingLicenseDao.createDrivingLicense(drivingLicense);

        drivingLicenseDao.deleteDrivingLicense(drivingLicense.getId());

        assertTrue(drivingLicenseDao.getAllDrivingLicenses().isEmpty());
    }
}