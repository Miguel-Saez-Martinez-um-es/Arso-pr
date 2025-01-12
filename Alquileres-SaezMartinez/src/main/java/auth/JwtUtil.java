package auth;

import java.util.Date;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


public class JwtUtil {

	private static final String KEY = "123456789012345678901234567890123456789012345678901234567890";
	// 3600 = 1 hora | 86400 = 1 dia | 2.678.400 = 1 mes de 31 dias
	private static final long TIME = 86400;
	
	@SuppressWarnings("deprecation")
	public static String generateToken(Map<String, Object> claims) {
		return Jwts.builder().setClaims(claims).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + TIME))
				.signWith(SignatureAlgorithm.HS512, KEY.getBytes()).compact();
	}

	public static Claims extractClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(KEY.getBytes()).build().parseClaimsJws(token).getBody();
	}

	public static boolean validateToken(String token) {
		Claims claims = extractClaims(token);
		return claims.getExpiration().after(new Date());
	}
}
