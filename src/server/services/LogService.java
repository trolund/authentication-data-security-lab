package server.services;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import server.data.models.Log;
import server.data.utils.HibernateUtil;
import server.services.interfaces.ILogService;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

public class LogService implements ILogService {

    public int addToServerLog(int userID, String action){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Integer logID = null;

        try {
            tx = session.beginTransaction();
            server.data.models.Log s = new server.data.models.Log(userID, action);
            logID = (Integer) session.save(s);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
        return logID;
    }

    public List<Log> lastLogs(int take){
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();

            String hql = "SELECT L FROM Log L ";

            Query<server.data.models.Log> query = session.createQuery(hql);
            return query.getResultList().stream().limit(take).collect(Collectors.toList());
        }catch (NoResultException e){
            return null;
        }
    }

}
