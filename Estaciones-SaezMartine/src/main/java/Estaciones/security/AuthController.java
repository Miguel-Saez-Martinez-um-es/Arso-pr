package Estaciones.security;

import java.util.Map; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Estaciones.servicio.IServicioVerificacion;
import io.swagger.v3.oas.annotations.Parameter;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private IServicioVerificacion servicioVerificacion;


	@PostMapping("/login")
	public ResponseEntity<String> login(@Parameter(description = "Nombre de usuario") @RequestParam String username,
			@Parameter(description = "Contraseña del usuario") @RequestParam String password) {
		Map<String, Object> claims = servicioVerificacion.verificarCredenciales(username, password);
		System.out.println(claims);
		if (claims != null) {
			String token = JwtUtil.generateToken(claims);
			return ResponseEntity.ok(token);
		} else {
			return ResponseEntity.status(401).body("Credenciales inválidas.");
		}
	}
	
	@PostMapping("/loginGestor")
	public ResponseEntity<String> loginGestor(
			@Parameter(description = "Nombre de usuario del gestor") @RequestParam String username,
			@Parameter(description = "Contraseña del gestor") @RequestParam String password) {
		Map<String, Object> claims = servicioVerificacion.verificarCredencialesGestor(username, password);
		if (claims != null) {
			String token = JwtUtil.generateToken(claims);
			return ResponseEntity.ok(token);
		} else {
			return ResponseEntity.status(401).body("Credenciales inválidas." );
		}
	}
}

