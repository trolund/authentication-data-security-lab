package server.services;

import org.springframework.security.crypto.bcrypt.BCrypt;
import server.services.interfaces.IPasswordService;

public class PasswordService implements IPasswordService {

    @Override
    public String hashPassword(String plainPassword){
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    @Override
    public boolean correctPassword(String candidate_password, String storedHash){
        return BCrypt.checkpw(candidate_password, storedHash);
    }
}
