package com.sb.main.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils
{    //secretkey208T1A0560helloworldafreed
    private final String SECRET_KEY = "c2VjcmV0a2V5MjA4VDFBMDU2MGhlbGxvd29ybGRhZnJlZWQ=";

    // #1 Generate Token
    public String generateToken(Authentication authentication)
    {
        return Jwts.builder()
               .subject(authentication.getName())
               .issuedAt(new Date(System.currentTimeMillis()))
               .expiration(new Date((System.currentTimeMillis()+1000*60*60*24))) // 1day = 86,40,00,000
               .signWith(getSigninKey())
               .compact();
    }

    private SecretKey getSigninKey()
    {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token)
    {
        return Jwts.parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimResolver)
    {
        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

// #2 Extract Username
    public String extractUsername(String token)
    {
        return extractClaim(token,Claims::getSubject);
    }

    private Date extractExpiration(String token)
    {
        return extractClaim(token,Claims::getExpiration);
    }

    private boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }

 //  #3 Validate Token
    public boolean isValidToken(String token, UserDetails userDetails)
    {
      String username = extractUsername(token);
      return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
}
