package server.services;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import server.data.models.User;
import server.data.utils.HibernateUtil;
import server.services.interfaces.ISessionService;

import javax.persistence.NoResultException;
import java.io.Serializable;

public class SessionService implements ISessionService, Serializable {

    public int addSession(User user){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Integer SessionID = null;

        try {
            tx = session.beginTransaction();
            server.data.models.Session s = new server.data.models.Session(7, user);
            SessionID = (Integer) session.save(s);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return SessionID;
    }

    public server.data.models.Session getValidSession(int sessionId){
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();

            String hql = "SELECT S FROM Session S " +
                    "WHERE S.sessionId = '" + sessionId + "' " +
 //                   "AND S.user.email = '" + email + "'" + " " +
                    "AND S.validTo >= current_time()";

            Query<server.data.models.Session> query = session.createQuery(hql);
            server.data.models.Session s = query.getSingleResult();

            return s;
        }catch (NoResultException e){
            return null;
        }
    }

    public boolean isValidSession(int sessionId){
        return getValidSession(sessionId) != null;
    }

}
