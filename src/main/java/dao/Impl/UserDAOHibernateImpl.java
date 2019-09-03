package dao.Impl;

import dao.UserDAO;
import model.User;
import org.hibernate.*;
import java.util.ArrayList;
import java.util.Collection;

public class UserDAOHibernateImpl implements UserDAO {

    private SessionFactory sessionFactory;

    public UserDAOHibernateImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Collection<User> getAllUsers() {
        Session session = sessionFactory.openSession();
        Collection<User> users = session.createQuery("FROM User").list();
        if (users == null) {
            session.close();
            return new ArrayList<User>();
        }
        session.close();
        return users;
    }

    @Override
    public void addUser(User user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(user);
        transaction.commit();
        session.close();
    }

    @Override
    public void updateUser(User user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.update(user);
        transaction.commit();
        session.close();
    }

    @Override
    public void deleteUserFromID(Long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            User user = getUserById(id);
            session.delete(user);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        transaction.commit();
        session.close();
    }

    @Override
    public void deleteUser(User user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.delete(user);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        transaction.commit();
        session.close();
    }

    @Override
    public boolean isUserExist(User user) {
        Session session = sessionFactory.openSession();
        User userParam = null;
        Query query = session.createQuery("SELECT o FROM User o WHERE name=:name AND password=:password AND birthday=:date");
        query.setParameter("name", user.getName());
        query.setParameter("password", user.getPassword());
        query.setParameter("date", user.getBirthday());
        try {
            userParam = (User) query.uniqueResult();
        } catch (NonUniqueResultException e){
            session.close();
            return true;
        }
        if (userParam != null) {
            session.close();
            return true;
        } else {
            session.close();
            return false;
        }
    }

    @Override
    public User getUserByName(String name) {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("SELECT o FROM User o WHERE name=:name");
        query.setParameter("name", name);
        User user = (User) query.uniqueResult();
        session.close();
        return user;
    }

    @Override
    public User getUserById(Long id) {
        Session session = sessionFactory.openSession();
        User user = (User) session.get(User.class, id);
        session.close();
        return user;
    }
}