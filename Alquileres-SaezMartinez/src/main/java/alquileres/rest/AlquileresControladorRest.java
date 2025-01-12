package alquileres.rest;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import alquileres.dto.AlquilerDTO;
import alquileres.dto.ReservaDTO;
import alquileres.dto.UsuarioDTO;
import alquileres.modelo.Alquiler;
import alquileres.modelo.Reserva;
import alquileres.modelo.Usuario;
import alquileres.servicio.IServicioAlquileres;
import alquileres.servicio.IServicioUsuario;
import io.jsonwebtoken.Claims;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import servicio.FactoriaServicios;
import utils.DateTimeUtils;

@Path("alquileres")
public class AlquileresControladorRest {

	private IServicioAlquileres servicio = FactoriaServicios.getServicio(IServicioAlquileres.class);
	private IServicioUsuario servicioUsuario = FactoriaServicios.getServicio(IServicioUsuario.class);

	@Context
	private HttpServletRequest servletRequest;

	// Obtener un usuario en concreto
	// curl -X GET http://localhost:8082/api/alquileres/Usuario/ -H "Authorization: Bearer tokenJwt"


	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({ "USUARIO", "GESTOR" })
	@PermitAll
	public Response getUsuario(@PathParam("id") String id) throws RepositorioException, EntidadNoEncontrada {
		/*
		String usuarioPeticion = null;
		String rol = null;
		try {
			if (this.servletRequest.getAttribute("claims") != null) {
				Claims claims = (Claims) this.servletRequest.getAttribute("claims");
				usuarioPeticion = claims.getSubject();
				rol = claims.get("roles").toString();

			}
		} catch (Exception e) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Token inválido").build();
		}

		if (!id.equals(usuarioPeticion) && !"GESTOR".equals(rol)) {
			return Response.status(Response.Status.FORBIDDEN).entity("Acceso denegado").build();
		}*/

		Usuario usuario = servicioUsuario.recuperar(id);
		UsuarioDTO usuarioDTO = convertirUsuarioADTO(usuario);
		return Response.status(Response.Status.OK).entity(usuarioDTO).build();
	}

	// Obtener todos los usuarios
	// curl -X GET http://localhost:8082/api/alquileres -H "Authorization: Bearer tokenJwt"


	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@RolesAllowed("GESTOR")
	@PermitAll
	public Response getAllUsuarios() throws RepositorioException, EntidadNoEncontrada {
		List<UsuarioDTO> usuarios = new LinkedList<UsuarioDTO>();
		for (Usuario u : servicioUsuario.recuperarUsuarios()) {
			usuarios.add(convertirUsuarioADTO(u));
		}
		return Response.status(Response.Status.OK).entity(usuarios).build();

	}

	// Reservar bicicleta
	// curl -X PUT
	// http://localhost:8082/api/alquileres/Usuario/reservar/Modelo1 -H "Authorization: Bearer tokenJwt"


	@PUT
	@Path("{id}/reservar/{idBicicleta}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({ "USUARIO", "GESTOR" })
	@PermitAll
	public Response reservar(@PathParam("id") String id, @PathParam("idBicicleta") String idBicicleta)
			throws RepositorioException, EntidadNoEncontrada {

		try {
			servicio.reservar(id, idBicicleta);
			return Response.status(Response.Status.CREATED).entity("Reserva creada exitosamente.").build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Error al confirmar reserva: " + e.getMessage())
					.build();
		}
	}

	// Confirmar reserva
	// curl -X PUT http://localhost:8082/api/alquileres/Usuario/confirmar/ -H "Authorization: Bearer tokenJwt"


	@PUT
	@Path("{id}/confirmar")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({ "Usuario", "GESTOR" })
	@PermitAll
	public Response confirmarReserva(@PathParam("id") String id) {
		try {
			servicio.confirmarReserva(id);
			return Response.status(Response.Status.OK).entity("Reserva confirmada exitosamente.").build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Error al confirmar reserva: " + e.getMessage())
					.build();
		}
	}

	// Alquilar bicicleta
	// curl -X PUT
	// http://localhost:8082/api/alquileres/Usuario/alquilar/Modelo1 -H "Authorization: Bearer tokenJwt"


	@PUT
	@Path("{id}/alquilar/{idBicicleta}")
	@RolesAllowed({ "USUARIO", "GESTOR" })
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response alquilar(@PathParam("id") String id, @PathParam("idBicicleta") String idBicicleta) {
		try {
			servicio.alquilar(id, idBicicleta);
			return Response.status(Response.Status.CREATED).entity("Bicicleta alquilada exitosamente.").build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Error al alquilar bicicleta: " + e.getMessage())
					.build();
		}
	}

	// Devolver bicicleta
	// curl -X PUT
	// http://localhost:8082/api/alquileres/Usuario/devolver/Estacion2 -H "Authorization: Bearer tokenJwt"


	@PUT
	@Path("{id}/devolver/{idEstacion}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({ "USUARIO", "GESTOR" })
	@PermitAll
	public Response dejarBicicleta(@PathParam("id") String id, @PathParam("idEstacion") String idEstacion) {
	
		// Requisitos: El usuario tiene un alquilerActivo. La estación tiene un hueco disponible para el estacionamiento. 
		// Por tanto primero comprobar el usuario y segundo recuperar la estacion y ver si tiene huecos libres
		try {
			System.out.println("ControladorRest en dejarBicicleta");
			servicio.dejarBicicleta(id, idEstacion);
			return Response.status(Response.Status.OK).entity("Bicicleta devuelta exitosamente.").build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Error al devolver bicicleta: " + e.getMessage())
					.build();
		}
	}

	// Liberar bloqueos 
	// curl -X PUT http://localhost:8082/api/alquileres/liberar/Usuario -H "Authorization: Bearer tokenJwt"


	@PUT
	@Path("liberar/{idUsuario}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("GESTOR")
	@PermitAll
	public Response liberarBloqueo(@PathParam("idUsuario") String idUsuario) {
		try {
			servicio.liberarBloqueo(idUsuario);
			return Response.status(Response.Status.OK).entity("Bloqueos liberados exitosamente.").build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Error al liberar bloqueos: " + e.getMessage())
					.build();
		}
	}

	private UsuarioDTO convertirUsuarioADTO(Usuario usuario) {

		List<ReservaDTO> reservasDTO = new LinkedList<ReservaDTO>();

		for (Reserva r : usuario.getReservas()) {
			reservasDTO.add(convertirReservaADTO(r));
		}

		List<AlquilerDTO> alquileresDTO = new LinkedList<AlquilerDTO>();

		for (Alquiler a : usuario.getAlquileres()) {
			alquileresDTO.add(convertirAlquilerADTO(a));
		}
		
		
		return new UsuarioDTO(usuario.getId(), reservasDTO, alquileresDTO);
	}

	private ReservaDTO convertirReservaADTO(Reserva reserva) {
		ReservaDTO r = new ReservaDTO();

		r.setIdBicicleta(reserva.getIdBicicleta());

		if (reserva.getCaducidad() != null) {
			r.setCaducidad(DateTimeUtils.toString(reserva.getCaducidad()));
		} else {
			r.setCaducidad(null);
		}

		if (reserva.getCreada() != null) {
			r.setCreada(DateTimeUtils.toString(reserva.getCreada()));
		} else {
			r.setCreada(null);
		}

		return r;
	}

	private AlquilerDTO convertirAlquilerADTO(Alquiler alquiler) {
		AlquilerDTO a = new AlquilerDTO();

		a.setIdBicicleta(alquiler.getIdBicicleta());

		if (alquiler.getFin() != null) {
			a.setFin(DateTimeUtils.toString(alquiler.getFin()));

		} else {
			a.setFin(null);
		}

		if (alquiler.getInicio() != null) {
			a.setInicio(DateTimeUtils.toString(alquiler.getInicio()));
		} else {
			a.setInicio(null);
		}

		return a;
	}

}
