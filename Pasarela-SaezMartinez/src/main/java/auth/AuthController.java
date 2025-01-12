package auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pasarela.retrofit.IServicioVerificacion;
import retrofit2.Response;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


	@Autowired
	private IServicioVerificacion servicioVerificacion;

	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
		try {
			Response<List<Map<String, String>>> response = servicioVerificacion.verificarCredenciales(credentials).execute();

			if (response.isSuccessful() && response.body() != null) {
				List<Map<String, String>> claims = response.body();

				String userId = claims.stream()
						.filter(claim -> "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier"
								.equals(claim.get("type")))
						.findFirst().map(claim -> claim.get("value")).orElse(null);

				String fullName = claims.stream().filter(
						claim -> "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name".equals(claim.get("type")))
						.findFirst().map(claim -> claim.get("value")).orElse(null);

				String role = claims.stream()
						.filter(claim -> "http://schemas.microsoft.com/ws/2008/06/identity/claims/role"
								.equals(claim.get("type")))
						.findFirst().map(claim -> claim.get("value")).orElse(null);

				if (userId == null || fullName == null || role == null) {
					throw new IllegalArgumentException("Respuesta inválida de usuarios.");
				}

				Map<String, Object> jwtClaims = new HashMap<>();
				jwtClaims.put("nombre", fullName);
				jwtClaims.put("rol", role);

				String token = JwtUtil.generateToken(jwtClaims);

				Map<String, Object> result = new HashMap<>();
				result.put("token", token);
				result.put("identificador", userId);
				result.put("nombreCompleto", fullName);
				result.put("rol", role);

				return ResponseEntity.ok(result);
			} else {
				Map<String, Object> errorResponse = new HashMap<>();
				errorResponse.put("error", "Credenciales inválidas");
				return ResponseEntity.status(401).body(errorResponse);
			}
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("error", "Error interno del servidor");
			return ResponseEntity.status(500).body(errorResponse);
		}
	}

	@PostMapping("/loginOAuth2")
	public ResponseEntity<Map<String, Object>> loginOAuth2(@RequestBody Map<String, String> oauth2Request) {
		try {
			Response<List<Map<String, String>>> response = servicioVerificacion.verificarUsuarioOAuth2(oauth2Request)
					.execute();

			if (response.isSuccessful() && response.body() != null) {
				List<Map<String, String>> claims = response.body();

				// Extraer datos de claims
				String userId = claims.stream()
						.filter(claim -> "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier"
								.equals(claim.get("type")))
						.findFirst().map(claim -> claim.get("value")).orElse(null);

				String fullName = claims.stream().filter(
						claim -> "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name".equals(claim.get("type")))
						.findFirst().map(claim -> claim.get("value")).orElse(null);

				String role = claims.stream()
						.filter(claim -> "http://schemas.microsoft.com/ws/2008/06/identity/claims/role"
								.equals(claim.get("type")))
						.findFirst().map(claim -> claim.get("value")).orElse(null);

				if (userId == null || fullName == null || role == null) {
					throw new IllegalArgumentException("Respuesta inválida de usuarios.");
				}

				// Generar token JWT
				Map<String, Object> jwtClaims = new HashMap<>();
				jwtClaims.put("nombre", fullName);
				jwtClaims.put("rol", role);

				String token = JwtUtil.generateToken(jwtClaims);

				// Respuesta
				Map<String, Object> result = new HashMap<>();
				result.put("token", token);
				result.put("identificador", userId);
				result.put("nombreCompleto", fullName);
				result.put("rol", role);

				return ResponseEntity.ok(result);
			} else {
				Map<String, Object> errorResponse = new HashMap<>();
				errorResponse.put("error", "Usuario no encontrado");
				return ResponseEntity.status(404).body(errorResponse);
			}
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("error", "Error interno del servidor");
			return ResponseEntity.status(500).body(errorResponse);
		}
	}
}
