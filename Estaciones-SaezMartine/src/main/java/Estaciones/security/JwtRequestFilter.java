package Estaciones.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;

//@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String authorizationHeader = request.getHeader("Authorization");

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			try {
				Claims claims = JwtUtil.extractClaims(token);
				if (JwtUtil.validateToken(token)) {
					String roles = claims.get("rol", String.class);
					Set<String> authorities = Arrays.stream(roles.split(",")).collect(Collectors.toSet());

					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
							claims.getSubject(), null,
							authorities.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role))
									.collect(Collectors.toList()));

					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			} catch (Exception e) {
				// Loguear el error y continuar con la cadena
				System.err.println("Error al validar el token JWT: " + e.getMessage());
			}
		}

		chain.doFilter(request, response);
	}
	
}
