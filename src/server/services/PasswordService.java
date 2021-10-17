package server.services;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordService {

    public String hashPassword(String plain_password){
        return BCrypt.hashpw(plain_password, BCrypt.gensalt());
    }

    public boolean correctPassword(String candidate_password, String stored_hash){
        return BCrypt.checkpw(candidate_password, stored_hash);
    }
}
