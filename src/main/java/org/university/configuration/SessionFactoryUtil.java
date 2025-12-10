package org.university.configuration;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.university.entity.*;

public class SessionFactoryUtil {
    private static SessionFactory sessionFactory;
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();
            configuration.addAnnotatedClass(Company.class);
            configuration.addAnnotatedClass(Customer.class);
            configuration.addAnnotatedClass(DrivingLicense.class);
            configuration.addAnnotatedClass(Employee.class);
            configuration.addAnnotatedClass(IdentificationCard.class);
            configuration.addAnnotatedClass(Person.class);
            configuration.addAnnotatedClass(Transport.class);
            configuration.addAnnotatedClass(Vehicle.class);
            ServiceRegistry serviceRegistry
                    = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }
        return sessionFactory;
    }
}
