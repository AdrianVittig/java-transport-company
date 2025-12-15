package org.university.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.university.configuration.SessionFactoryUtil;
import org.university.entity.Transport;
import org.university.util.CargoType;
import org.university.util.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransportDaoTest {

    private static SessionFactory sessionFactory;
    private static TransportDao transportDao;

    @BeforeAll
    static void init(){
        sessionFactory = SessionFactoryUtil.getSessionFactory();
        transportDao = new TransportDao();
    }

    @BeforeEach
    void clearDatabase() {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            session.createQuery("DELETE FROM Transport").executeUpdate();

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

    private Transport buildTransport(String startPoint, String endPoint, BigDecimal price, LocalDate date, PaymentStatus status) {
        Transport transport = new Transport();
        transport.setStartPoint(startPoint);
        transport.setEndPoint(endPoint);
        transport.setInitPrice(price);
        transport.setDepartureDate(date);
        transport.setArrivalDate(date.plusDays(1));
        transport.setPaymentStatus(status);
        transport.setQuantity(BigDecimal.ONE);
        transport.setCargoType(CargoType.ADR);
        return transport;
    }


    @Test
    void createTransport() {
        Transport transport = buildTransport("Sofia", "Varna", BigDecimal.valueOf(500),
                LocalDate.of(2025, 1, 10), PaymentStatus.NOT_PAID);

        transportDao.createTransport(transport);

        assertEquals(1, transportDao.getAllTransports().size());
        Transport saved = transportDao.getAllTransports().get(0);
        assertNotNull(saved.getId());
        assertEquals("Sofia", saved.getStartPoint());
        assertEquals("Varna", saved.getEndPoint());
        assertEquals(0, BigDecimal.valueOf(500).compareTo(saved.getInitPrice()));
        assertEquals(LocalDate.of(2025, 1, 11), saved.getArrivalDate());
        assertEquals(PaymentStatus.NOT_PAID, saved.getPaymentStatus());
    }

    @Test
    void getTransportById() {
        Transport transport = buildTransport("Plovdiv", "Burgas", BigDecimal.valueOf(300), LocalDate.of(2025, 2, 2), PaymentStatus.NOT_PAID);
        transportDao.createTransport(transport);

        Long id = transportDao.getAllTransports().get(0).getId();
        Transport found = transportDao.getTransportById(id);

        assertNotNull(found);
        assertEquals(id, found.getId());
        assertEquals("Plovdiv", found.getStartPoint());
        assertEquals("Burgas", found.getEndPoint());
    }

    @Test
    void getAllTransports() {
        transportDao.createTransport(buildTransport("Sofia", "Varna", BigDecimal.valueOf(500), LocalDate.of(2025, 1, 10), PaymentStatus.NOT_PAID));
        transportDao.createTransport(buildTransport("Sofia", "Burgas", BigDecimal.valueOf(450), LocalDate.of(2025, 1, 11), PaymentStatus.NOT_PAID));

        assertEquals(2, transportDao.getAllTransports().size());
    }

    @Test
    void updateTransport() {
        transportDao.createTransport(buildTransport("Sofia", "Varna", BigDecimal.valueOf(500), LocalDate.of(2025, 1, 10), PaymentStatus.NOT_PAID));
        Transport saved = transportDao.getAllTransports().get(0);

        Transport updated = buildTransport("Sofia", "Ruse", BigDecimal.valueOf(650), LocalDate.of(2025, 3, 3), PaymentStatus.NOT_PAID);
        updated.setId(saved.getId());

        transportDao.updateTransport(saved.getId(), updated);

        Transport after = transportDao.getTransportById(saved.getId());
        assertNotNull(after);
        assertEquals("Ruse", after.getEndPoint());
        assertEquals(0, BigDecimal.valueOf(650).compareTo(after.getInitPrice()));
        assertEquals(LocalDate.of(2025, 3, 3), after.getDepartureDate());
    }

    @Test
    void updatePaymentStatus() {
        transportDao.createTransport(buildTransport("Sofia", "Varna", BigDecimal.valueOf(500), LocalDate.of(2025, 1, 10), PaymentStatus.NOT_PAID));
        Long id = transportDao.getAllTransports().get(0).getId();

        transportDao.updatePaymentStatus(id, PaymentStatus.PAID);

        Transport after = transportDao.getTransportById(id);
        assertEquals(PaymentStatus.PAID, after.getPaymentStatus());
    }

    @Test
    void deleteTransport() {
        transportDao.createTransport(buildTransport("Sofia", "Varna", BigDecimal.valueOf(500), LocalDate.of(2025, 1, 10), PaymentStatus.NOT_PAID));
        Long id = transportDao.getAllTransports().get(0).getId();

        transportDao.deleteTransport(id);

        assertTrue(transportDao.getAllTransports().isEmpty());
    }

    @Test
    void sortByDestinationAscending() {
        transportDao.createTransport(buildTransport("Sofia", "Varna", BigDecimal.valueOf(500), LocalDate.of(2025, 1, 10), PaymentStatus.NOT_PAID));
        transportDao.createTransport(buildTransport("Sofia", "Burgas", BigDecimal.valueOf(450), LocalDate.of(2025, 1, 11), PaymentStatus.NOT_PAID));
        transportDao.createTransport(buildTransport("Sofia", "Sofia", BigDecimal.valueOf(100), LocalDate.of(2025, 1, 12), PaymentStatus.NOT_PAID));

        assertEquals(
                java.util.List.of("Burgas", "Sofia", "Varna"),
                transportDao.sortByDestinationAscending(true).stream().map(Transport::getEndPoint).toList()
        );
    }

    @Test
    void filterByDestination() {
        transportDao.createTransport(buildTransport("Sofia", "Varna", BigDecimal.valueOf(500), LocalDate.of(2025, 1, 10), PaymentStatus.NOT_PAID));
        transportDao.createTransport(buildTransport("Sofia", "Varna", BigDecimal.valueOf(520), LocalDate.of(2025, 1, 11), PaymentStatus.NOT_PAID));
        transportDao.createTransport(buildTransport("Sofia", "Burgas", BigDecimal.valueOf(450), LocalDate.of(2025, 1, 12), PaymentStatus.NOT_PAID));

        assertEquals(2, transportDao.filterByDestination("Varna").size());
        assertTrue(transportDao.filterByDestination("Varna").stream().allMatch(t -> "Varna".equals(t.getEndPoint())));
    }
}