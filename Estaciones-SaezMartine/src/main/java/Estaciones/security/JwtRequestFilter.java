package Estaciones.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private static final String KEY = "123456789012345678901234567890123456789012345678901234567890";

	
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		String authorization = request.getHeader("Authorization");
		
	    if (authorization != null) {
			String token = authorization.substring("Bearer ".length()).trim();
	    	System.out.println("token en doFilterInternal: " + token);
	    	
	    	Claims claims = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
	    	
			Date caducidad = claims.getExpiration();
			
			if (caducidad.before(new Date())) {
				response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED);
		        return;
			}
			
	    	String username = claims.getSubject();
	    	String rol = (String) claims.get("rol");
	    	ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	    	authorities.add(new SimpleGrantedAuthority(rol));
	    	
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null,
					authorities);
			System.out.println("nombre Usuario:"+ username + " con rol:" + rol);
			
			SecurityContextHolder.getContext().setAuthentication(auth);
		}

		chain.doFilter(request, response);
	}
}
