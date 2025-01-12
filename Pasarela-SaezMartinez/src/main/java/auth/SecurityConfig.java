package auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable() // Deshabilitar CSRF para simplificar las pruebas
				.authorizeRequests().antMatchers("/api/auth/login", "/api/auth/loginOAuth2").permitAll() // Permitir
																											// acceso
																											// público a
																											// estos
																											// endpoints
				.antMatchers("/api/usuarios/**").permitAll().antMatchers("/api/estaciones/**").permitAll()
				.antMatchers("/api/alquileres/**").permitAll().anyRequest().authenticated() // Requerir autenticación
																							// para el resto
				.and().httpBasic(); // Autenticación básica opcional para otros endpoints
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Configura usuarios en memoria o proveedores de autenticación adicionales si
		// es necesario
		auth.inMemoryAuthentication().withUser("user").password("{noop}password").roles("USER").and().withUser("admin")
				.password("{noop}admin").roles("ADMIN");
	}
}
