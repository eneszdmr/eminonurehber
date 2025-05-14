package com.istanbul.eminonurehber.Config;

import com.istanbul.eminonurehber.Entity.Company;
import com.istanbul.eminonurehber.Entity.Role;
import com.istanbul.eminonurehber.Entity.User;
import com.istanbul.eminonurehber.Repository.CompanyRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Autowired
    private CompanyRepository companyRepository;
    private static final String SECRET_KEY = "45790fbb230a9ba63368419009cefbdc175e888c8b155434ccf359f29c24182e";

    // Token oluşturma (Süre: 1 gün)
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())) // Kullanıcı rolünü ekliyoruz
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24*2)) // 1 gün
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token, String email) {
        // Email kontrolü ve token'ın süresinin dolup dolmadığına bakıyoruz
        return (email.equals(extractEmail(token)) && !isTokenExpired(token));
    }

    public String extractEmail(String token) {
        if (token == null || token.isBlank()) {
            return null; // Eğer token yoksa null döndür
        }
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        }catch (Exception e) {
            return null;
        }

    }

    // Token'ın süresi doldu mu?
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Token'ın sona erme süresi
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Genel claim çıkarma metodu
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }


    public Set<String> extractRoles(String token) {
        if (token == null || token.isBlank()) {
            return Collections.emptySet(); // Eğer token yoksa boş bir rol seti döndür
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // "role" claim'ini List<String> olarak al
            List<String> rolesList = claims.get("role", List.class);

            if (rolesList == null) {
                throw new RuntimeException("Role claim not found in token");
            }

            // Listeyi Set'e çevir
            return new HashSet<>(rolesList);
        } catch (Exception e) {
            throw new RuntimeException("Error extracting roles from token: " + e.getMessage(), e);
        }
    }

    public Long getCompanyIdByEmail(String email) {
        Optional<Company> company = companyRepository.findByEmail(email);
        return company.map(Company::getId).orElseThrow(() -> new RuntimeException("Firma bulunamadı!"));
    }

}
