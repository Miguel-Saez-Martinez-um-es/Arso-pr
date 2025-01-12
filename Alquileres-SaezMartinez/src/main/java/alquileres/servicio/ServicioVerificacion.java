package alquileres.servicio;

import java.util.HashMap;
import java.util.Map;


public class ServicioVerificacion implements IServicioVerificacion {

	// Almacenamiento estático de usuarios y roles en memoria
	private static final Map<String, String> credenciales = new HashMap<>();
	static {
		credenciales.put("user", "user");
		credenciales.put("admin", "admin");
	}

	@Override
	public Map<String, Object> verificarCredenciales(String username, String password) {
		// Verificación de credenciales para el rol de usuario
		if ("user".equals(username) && credenciales.get("user").equals(password)) {
			Map<String, Object> claims = new HashMap<>();
			claims.put("nombre", username);
			claims.put("rol", "USER");
			return claims;
		}
		return null; // Credenciales incorrectas
	}

	@Override
	public Map<String, Object> verificarCredencialesGestor(String username, String password) {
		// Verificación de credenciales para el rol de administrador
		if ("admin".equals(username) && credenciales.get("admin").equals(password)) {
			Map<String, Object> claims = new HashMap<>();
			claims.put("nombre", username);
			claims.put("rol", "ADMIN");
			return claims;
		}
		return null; // Credenciales incorrectas
	}
}
