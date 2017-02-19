package serviceDatabase.service;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * Klasa umożliwiająca nawiązanie połączenia z bazą danych.
 */
public class DBUtil {

    /**
     * Pojedyńcza istancja klas typu SessionFactory
     */
    private static SessionFactory sessionFactory;

    /**
     * Metoda tworzy obiekt typu SessionFactory (singleton)
     * @return Zwraca obiekt typu Sessionfactory
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {

            Configuration configuration = new Configuration().configure();
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .configure().build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }
        return sessionFactory;
    }
}
