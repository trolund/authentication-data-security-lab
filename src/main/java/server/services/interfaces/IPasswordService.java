package server.services.interfaces;

public interface IPasswordService {
    String hashPassword(String plainPassword);

    boolean correctPassword(String candidate_password, String storedHash);
}
