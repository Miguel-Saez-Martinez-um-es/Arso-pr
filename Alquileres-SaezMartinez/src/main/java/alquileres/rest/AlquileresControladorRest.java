package alquileres.rest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import servicio.FactoriaServicios;

@Path("alquileres")
public class AlquileresControladorRest {

	private IServicioAlquileres servicio = FactoriaServicios.getServicio(IServicioAlquileres.class);
	private IServicioUsuario servicioUsuario = FactoriaServicios.getServicio(IServicioUsuario.class);

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActividad(@PathParam("id") String id) throws Exception {
		Usuario usuario = servicioUsuario.recuperar(id);

		UsuarioDTO usuarioDTO = convertirUsuarioADTO(usuario);
		return Response.status(Response.Status.OK).entity(usuarioDTO).build();
	}
	
	 @GET
	    @Produces({MediaType.APPLICATION_JSON})
	    public Response getAllUsuarios() throws Exception {
	        List<UsuarioDTO> usuarios = new LinkedList<UsuarioDTO>(); // Suponiendo que existe este método
	        for(Usuario u : servicioUsuario.recuperarUsuarios()) {
	        	usuarios.add(convertirUsuarioADTO(u));
	        }
	        return Response.status(Response.Status.OK).entity(usuarios).build();
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
			r.setCaducidad(formatLocalDateTime(reserva.getCaducidad()));
		} else {
			r.setCaducidad(null);
		}

		if (reserva.getCreada() != null) {
			r.setCreada(formatLocalDateTime(reserva.getCreada()));
		} else {
			r.setCreada(null);
		}

		return r;
	}

	private AlquilerDTO convertirAlquilerADTO(Alquiler alquiler) {
		AlquilerDTO a = new AlquilerDTO();

		a.setIdBicicleta(alquiler.getIdBicicleta());

		if (alquiler.getFin() != null) {
			a.setFin(formatLocalDateTime(alquiler.getFin()));

		} else {
			a.setFin(null);
		}

		if (alquiler.getInicio() != null) {
			a.setInicio(formatLocalDateTime(alquiler.getInicio()));
		} else {
			a.setInicio(null);
		}
		
		return a;
	}
	
	public static String formatLocalDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateTime.format(formatter);
    }
}
