package auth;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

	private String secretKey = "123456789012345678901234567890123456789012345678901234567890";

	private long expirationTime = 86400;

	// Genera un JWT
	public String generateToken(Map<String, Object> claims) {
		return Jwts.builder().setClaims(claims).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expirationTime))
				.signWith(SignatureAlgorithm.HS512, secretKey.getBytes()).compact();
	}

	// Extrae los claims de un token
	public Claims extractClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token).getBody();
	}

	// Valida un token
	public boolean validateToken(String token) {
		Claims claims = extractClaims(token);
		return claims.getExpiration().after(new Date());
	}
}
