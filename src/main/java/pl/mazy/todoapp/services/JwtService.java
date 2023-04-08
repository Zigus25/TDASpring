package pl.mazy.todoapp.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import pl.mazy.todoapp.model.User;

import java.security.Key;
import java.util.*;
import java.util.function.Function;


@Service
public class JwtService {

    private static final  String secKey = "327235753778214125442A472D4B6150645367566B59703373367639792F423F";
    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken,Claims::getSubject);
    }

    public Integer extractID(HttpServletRequest request){
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        jwtToken = authHeader.substring(7);
        return Integer.parseInt(extractUsername(jwtToken));
    }

    public <T> T extractClaim(String token, Function<Claims,T>claimsResolve){
        final Claims claims = extractAllClaims(token);
        return claimsResolve.apply(claims);
    }

    public String generateToken(User userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }

    public String generateToken(Map<String, Objects> extractClaims, User userDetails){
        return Jwts
                .builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getId().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ 1000 * 60 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isValid(String token,User userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getId().toString())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    private Claims extractAllClaims(String token){
        return (Claims) Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parse(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
