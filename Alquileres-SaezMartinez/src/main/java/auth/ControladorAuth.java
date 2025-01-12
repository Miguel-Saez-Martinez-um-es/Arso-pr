package auth;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import alquileres.modelo.Usuario;
import alquileres.servicio.IServicioUsuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import servicio.FactoriaServicios;

@Path("auth")
public class ControladorAuth {

	private static final String KEY = "123456789012345678901234567890123456789012345678901234567890";
    private IServicioUsuario servicioUsuario = FactoriaServicios.getServicio(IServicioUsuario.class);

	
	@POST
	@Path("/login")
	@PermitAll
	public Response login(@FormParam("username") String username, @FormParam("password") String password) throws RepositorioException, EntidadNoEncontrada {
		
		Map<String, Object> claims = verificarCredenciales(username, password);
		if(claims != null) {
			Date caducidad = Date.from(Instant.now().plusSeconds(86400));
			String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, KEY)
					.setExpiration(caducidad).compact();
			return Response.ok(token).build();
		}
		
		return Response.status(Response.Status.UNAUTHORIZED).entity("Credenciales invalidas").build();
		
	}
	
	//Deberia ir a la base de datos, comprobar las credenciales, recuperar la informacion y colocarlo
	public Map<String, Object> verificarCredenciales(String username, String password) throws RepositorioException, EntidadNoEncontrada {
		
		
        Usuario usuario = servicioUsuario.autenticar(username, password);
        if (usuario != null) {
            // Crear claims con la información necesaria
            Map<String, Object> claims = new HashMap<>();
            claims.put("sub", usuario.getId());
            claims.put("roles", usuario.getRol());
            System.out.println(usuario.toString());
            return claims;
        }else {
        	// al ser null significa que ha falldo por tanto cancelamos
        	return null;
        }

	}
}
