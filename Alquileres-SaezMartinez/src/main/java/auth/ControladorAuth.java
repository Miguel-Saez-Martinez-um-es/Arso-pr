package auth;

import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import alquileres.servicio.IServicioVerificacion;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import servicio.FactoriaServicios;


@Path("auth")
public class ControladorAuth {

	private IServicioVerificacion servicioVerificacion = FactoriaServicios.getServicio(IServicioVerificacion.class);


	
	@POST
	@Path("/login")
	@PermitAll
	public Response login(@FormParam("username") String username, @FormParam("password") String password) throws RepositorioException, EntidadNoEncontrada {
		
		Map<String, Object> claims = servicioVerificacion.verificarCredenciales(username, password);
		if(claims != null) {
			String token = JwtUtil.generateToken(claims); // Usar JwtUtil para generar el token
			return Response.ok(token).build();
		}
		
		return Response.status(Response.Status.UNAUTHORIZED).entity("Credenciales inválidas. Deberian ser:" + claims).build();
		
		
	}
	
	@POST
	@Path("/loginGestor")
	@PermitAll
	public Response loginGestor(@FormParam("username") String username, @FormParam("password") String password) {
		Map<String, Object> claims = servicioVerificacion.verificarCredencialesGestor(username, password);
		if (claims != null) {
			String token = JwtUtil.generateToken(claims); // Usar JwtUtil para generar el token
			return Response.ok(token).build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Credenciales inválidas. Deberian ser:" + claims).build();
		}
	}
}
