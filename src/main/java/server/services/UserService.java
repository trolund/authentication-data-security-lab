package server.services;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import server.data.utils.HibernateUtil;
import server.data.models.User;
import server.services.interfaces.IUserService;

import javax.persistence.NoResultException;

public class UserService implements IUserService {

    private Session session;

    public UserService() {
    }

    @Override
    public User getUser(String email){
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "SELECT U FROM User U WHERE U.username = '" + email + "'";

            Query<User> query = session.createQuery(hql);
            User user = query.getSingleResult();

            return user;
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public int createUser(User u){
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Integer userID = null;

        if(!session.isOpen())
            return userID;

        try {
            tx = session.beginTransaction();
            userID = (Integer) session.save(u);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return userID;
    }

    public void deleteUser(String email){
        session = HibernateUtil.getSessionFactory().openSession();
        if(!session.isOpen())
            return;
        User u = getUser(email);
        if(u != null)
            session.delete(u);
    }
}
