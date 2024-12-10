package Estaciones.security;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class SecuritySuccessHandler implements AuthenticationSuccessHandler {

	private static final String KEY = "123456789012345678901234567890123456789012345678901234567890";

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		DefaultOAuth2User usuario = (DefaultOAuth2User) authentication.getPrincipal();

		String login = usuario.getAttribute("login");
		System.out.println("login: " + login);

		Map<String, Object> claims = fetchUserInfo(usuario);

		Date caducidad = Date.from(Instant.now().plusSeconds(3600));

		String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, KEY).setExpiration(caducidad)
				.compact();

		response.getWriter().append(token);
	}

	private Map<String, Object> fetchUserInfo(DefaultOAuth2User usuario) {

		Map<String, Object> claims = new HashMap<>();
		String login; // recuperar el login de GitHub
		String rol;
		if (usuario.getAttribute("login") != null) {
			login = usuario.getAttribute("login");
		} else {
			login = "usuario-anonimo";
		}

		if ("Miguel-Saez-Martinez-um-es".equals(login)) {
			rol = "gestor";
		} else {
			rol = "usuario"; // Rol predeterminado
		}

		claims.put("sub", login);
		claims.put("rol", rol); // Rol predeterminado si no hay rol
		return claims;
	}

}
