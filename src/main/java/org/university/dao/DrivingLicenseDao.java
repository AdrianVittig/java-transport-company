package org.university.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.university.configuration.SessionFactoryUtil;
import org.university.entity.DrivingLicense;
import org.university.exception.DAOException;

import java.util.List;

public class DrivingLicenseDao {
    public void createDrivingLicense(DrivingLicense drivingLicense) throws DAOException {
        Transaction transaction = null;
        Session session = null;
        try {
        session = SessionFactoryUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(drivingLicense);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DAOException("Failed to create driving license: " + e.getMessage());
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    public DrivingLicense getDrivingLicenseById(long id) {
        Session session = null;
        try {
            session = SessionFactoryUtil.getSessionFactory().openSession();
            return session.find(DrivingLicense.class, id);
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    public List<DrivingLicense> getAllDrivingLicenses() {
        Session session = null;
        try {
            session = SessionFactoryUtil.getSessionFactory().openSession();
            return session.createQuery("SELECT d FROM DrivingLicense d", DrivingLicense.class)
                    .getResultList();
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    public void updateDrivingLicense(long id, DrivingLicense updated) throws DAOException {
        Transaction transaction = null;
        Session session = null;
        try {
            session = SessionFactoryUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            DrivingLicense drivingLicense = session.find(DrivingLicense.class, id);
            if (drivingLicense == null) {
                throw new DAOException("Driving license with id " + id + " not found");
            }

            drivingLicense.setDrivingLicenseNumber(updated.getDrivingLicenseNumber());
            drivingLicense.setIssueDate(updated.getIssueDate());
            drivingLicense.setExpiryDate(updated.getExpiryDate());
            drivingLicense.setDrivingLicenseCategories(updated.getDrivingLicenseCategories());

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
            throw new DAOException("Failed to update driving license: " + e.getMessage());
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }

    public void deleteDrivingLicense(long id) throws DAOException {
        Transaction transaction = null;
        Session session = null;
        try{
            session = SessionFactoryUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            DrivingLicense drivingLicense = session.find(DrivingLicense.class, id);
            if (drivingLicense == null) {
                throw new DAOException("Driving license with id " + id + " not found");
            }

            session.remove(drivingLicense);
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
            throw new DAOException("Failed to delete driving license: " + e.getMessage());
        }finally {
            if(session != null && session.isOpen()){
                session.close();
            }
        }
    }
}
