package com.bridgelabz.fundoo_notes.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.bridgelabz.fundoo_notes.dto.LoginDto;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.awt.font.ShapeGraphicAttribute;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtToken {

    //    String clientSecret = "${clientSecret}"; // Or load from configuration
//    SecretKey sharedSecret = Keys.hmacShaKeyFor(clientSecret.getBytes(StandardCharsets.UTF_8));
    Instant now = Instant.now();
    static String TOKEN_SECRET = "javaDemo123";


    public String generateToken(LoginDto loginDto) {

        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        //payload
        return JWT.create()
                .withClaim("email", loginDto.getEmail())
                .sign(algorithm);

//        return Jwts.builder()
//                .claim("email", loginDto.getEmail())
//                .setSubject(loginDto.getEmail())
//                .setId(UUID.randomUUID().toString())
//                .setIssuedAt(Date.from(now))
//                .setExpiration(Date.from(now.plus(5l, ChronoUnit.MINUTES)))
//                .compact();

//        return Jwts.builder()
//                .setAudience("https://${yourOktaDomain}/oauth2/default/v1/token")
//                .setIssuedAt(Date.from(now))
//                .setExpiration(Date.from(now.plus(5L, ChronoUnit.MINUTES)))
//                .setIssuer(loginDto.getEmail())
//                .setSubject(loginDto.getEmail())
//                .setId(UUID.randomUUID().toString())
//                .signWith(sharedSecret)
//                .compact();
    }

    public String decodeToken(String token) {
        String email;
        //for verification algorithm
        Verification verification = JWT.require(Algorithm.HMAC256(TOKEN_SECRET));
        JWTVerifier jwtverifier = verification.build();
        //to decode token
        DecodedJWT decodedjwt = jwtverifier.verify(token);
        //retrive data
        Claim claim = decodedjwt.getClaim("email");
        email = claim.asString();
        return email;
    }
}
