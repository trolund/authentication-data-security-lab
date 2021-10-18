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
            String hql = "SELECT U.email FROM User U WHERE U.email = '" + us.getEmail() + "'";
            Query query = session.createQuery(hql).setFetchSize(1);
            if(query.list().size() == 0){
                User u = new User(us.getEmail(), us.getFirstName(), us.getLastName(), us.getPassword());
                session.saveOrUpdate(u);
            }
        }
    }
}
