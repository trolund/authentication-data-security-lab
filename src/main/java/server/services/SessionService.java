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
import java.time.LocalDateTime;
import java.util.Date;

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

    public server.data.models.Session getValidSession(Integer sessionId){
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();

            String hql = "SELECT S FROM Session S " +
                         "WHERE S.sessionId = '" + sessionId + "' ";
  //                  "AND S.validTo > current_time()";

            Query<server.data.models.Session> query = session.createQuery(hql);
            server.data.models.Session s = query.getSingleResult();

            LocalDateTime now = LocalDateTime.now();
            Date date = java.sql.Timestamp.valueOf(now);

            return s.getValidTo().getTime() > date.getTime() ? s : null;
        }catch (NoResultException e){
            return null;
        }
    }

    public void endSession(Integer sessionId){
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();

            server.data.models.Session s = getValidSession(sessionId);

            LocalDateTime now = LocalDateTime.now();
            s.setValidTo(java.sql.Timestamp.valueOf(now));

            session.update(s);
            tx.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean isValidSession(Integer sessionId){
        return getValidSession(sessionId) != null;
    }

}
