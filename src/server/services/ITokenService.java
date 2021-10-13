package server.services;

import com.auth0.jwt.exceptions.JWTVerificationException;
import shared.exceptions.Unauthorized;

public interface ITokenService {

    String createJWT(String email) throws JWTVerificationException;
    boolean verifyToken(String token);
    boolean verifyTokenThrowOnInvalid(String token) throws Unauthorized;
}
