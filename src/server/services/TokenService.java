package server.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import shared.exceptions.Unauthorized;

public class TokenService implements ITokenService {

    private String secret = "secret";
    private String Issuer = "print-server";

   public String createJWT(String email) throws JWTVerificationException {
       Algorithm algorithm = Algorithm.HMAC256(secret);
       String token = JWT.create()
                   .withIssuer(Issuer)
                   .withClaim("Email", email)
                   .sign(algorithm);

       System.out.println("Created token to user: " + email);

       return token;
    }

    public boolean verifyToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(Issuer)
                    .build();
            DecodedJWT jwt = verifier.verify(token);

            Claim emailClaim = jwt.getClaim("Email");

            System.out.println("Validated token by user: " + emailClaim.asString());

            return true;
        } catch (JWTVerificationException exception){
            System.err.println("Invalid Token");
            return false;
        }
    }

    public boolean verifyTokenThrowOnInvalid(String token) throws Unauthorized {
       if(verifyToken(token)){
           return true;
       }else{
           throw new Unauthorized("User was not authorised");
       }
    }

}
