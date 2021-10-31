package server.services;

import org.hibernate.Session;
import org.hibernate.query.Query;
import server.data.utils.HibernateUtil;
import server.data.models.User;
import server.services.interfaces.ISeedingService;

import java.util.ArrayList;

public class SeedingService implements ISeedingService {

    private Session session;

    private ArrayList<User> users = new ArrayList<User>() {{
        add(new User("trolund@gmail.com", "Troels","Lund", "Password123"));
    }};

    public SeedingService() {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    public void createMockUsers(){

        for (User us: users) {
            String hql = "SELECT U.username FROM User U WHERE U.username = '" + us.getUsername() + "'";
            Query query = session.createQuery(hql).setFetchSize(1);
            if(query.list().size() == 0){
                User u = new User(us.getUsername(), us.getFirstName(), us.getLastName(), us.getHashedPassword());
                session.saveOrUpdate(u);
            }
        }
    }
}
