package com.salat.briene.config;

import com.salat.briene.entities.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private final static String jwtSecret = "brieneSecretKey"; // should be more complex
    private final static Integer jwtExpirationMs = 999999999;

    public String generateJwtToken(Authentication authentication) {

        User userPrincipal = (User) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error(JWTError.INVALID_SIGNATURE.getMessage(), e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error(JWTError.INVALID_TOKEN.getMessage(), e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error(JWTError.TOKEN_IS_EXPIRED.getMessage(), e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error(JWTError.TOKEN_IS_UNSUPPORTED.getMessage(), e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error(JWTError.TOKEN_IS_EMPTY.getMessage(), e.getMessage());
        }

        return false;
    }
}