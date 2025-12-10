package org.university.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.university.configuration.SessionFactoryUtil;
import org.university.entity.Vehicle;
import org.university.exception.DAOException;

import java.util.List;

public class VehicleDao
{
    public void createVehicle(Vehicle vehicle) throws DAOException {
        Transaction transaction = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(vehicle);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DAOException("Failed to create vehicle: " + e.getMessage());
        }
    }

    public Vehicle getVehicleById(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Vehicle.class, id);
        }
    }

    public List<Vehicle> getAllVehicles() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT v FROM Vehicle v", Vehicle.class)
                    .getResultList();
        }
    }

    public void updateVehicle(long id, Vehicle updated) throws DAOException {
        Transaction transaction = null;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Vehicle v = session.find(Vehicle.class, id);
            if (v == null) {
                throw new DAOException("Vehicle with id " + id + " not found");
            }

            v.setVehicleType(updated.getVehicleType());
            v.setDistanceTraveled(updated.getDistanceTraveled());
            v.setEmployee(updated.getEmployee());
            v.setCompany(updated.getCompany());

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
            throw new DAOException("Failed to update vehicle: " + e.getMessage());
        }
    }

    public void deleteVehicle(long id) throws DAOException {
        Transaction transaction = null;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Vehicle vehicle = session.find(Vehicle.class, id);
            if (vehicle == null) {
                throw new DAOException("Vehicle with id " + id + " not found");
            }

            session.remove(vehicle);

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
            throw new DAOException("Failed to delete vehicle: " + e.getMessage());
        }
    }
}
