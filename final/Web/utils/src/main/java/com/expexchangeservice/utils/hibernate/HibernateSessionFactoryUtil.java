package com.expexchangeservice.utils.hibernate;

import com.expexchangeservice.model.entities.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

public class HibernateSessionFactoryUtil {

    @Autowired
    private static SessionFactory SESSION_FACTORY;
    private static Session SESSION;

    static {
        try {
            Configuration configuration = new Configuration().configure();
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Token.class);
            configuration.addAnnotatedClass(UserProfile.class);
            configuration.addAnnotatedClass(Theme.class);
            configuration.addAnnotatedClass(Section.class);
            configuration.addAnnotatedClass(Review.class);
            configuration.addAnnotatedClass(Lesson.class);
            configuration.addAnnotatedClass(Course.class);
            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
            SESSION_FACTORY = configuration.buildSessionFactory(builder.build());
        } catch (Exception e) {
            System.out.println("Исключение!" + e);
        }
    }

    private HibernateSessionFactoryUtil() {
    }

    public static Session openSession() {
        SESSION = SESSION_FACTORY.openSession();
        return SESSION;
    }

    public static Session getSession() {
        return SESSION;
    }

    public static void closeSession() {
        SESSION.close();
    }
}
