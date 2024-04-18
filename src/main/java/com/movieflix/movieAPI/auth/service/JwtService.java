package com.movieflix.movieAPI.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {
    private final String SECRET_KEY= "dsdsdsffdfdfdfdfdsfdfdgfddfdfffffffffffffffffffffffffffffffffffffffffffffffdffdf";

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }
    public<T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims =extractAllClaims(token);
        System.out.println(claims);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token) //ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, SecurityException, IllegalArgumentException
                .getBody();
    }

    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(
            Map<String,Object> extraClaims,
            UserDetails userDetails
    ){

        return Jwts
                .builder()
//                .claim("authorities",populateAuthorities(userDetails.getAuthorities()))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() +1000*60))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }
    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities){
        Set<String> authoritiesSet=new HashSet<>();
        for(GrantedAuthority authority:authorities){
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",",authoritiesSet);
    }

}
