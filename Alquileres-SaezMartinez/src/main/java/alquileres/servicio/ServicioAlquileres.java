package alquileres.servicio;

import java.time.LocalDateTime;
import java.util.Iterator;

import alquileres.modelo.Alquiler;
import alquileres.modelo.Reserva;
import alquileres.modelo.Usuario;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import servicio.FactoriaServicios;

public class ServicioAlquileres implements IServicioAlquileres {

	private IServicioEstaciones servicioEstaciones = FactoriaServicios.getServicio(IServicioEstaciones.class);
	private IServicioUsuario servicioUsuario = FactoriaServicios.getServicio(IServicioUsuario.class);

	@Override
	public void reservar(String idUsuario, String idBicicleta) throws RepositorioException, EntidadNoEncontrada {
		Usuario usuario = servicioUsuario.recuperar(idUsuario);
		if (usuario.reservaActiva() == null && usuario.alquilerActivo() == null && !usuario.bloqueado()
				&& !usuario.superaTiempo()) {
			Reserva reserva = new Reserva();
			reserva.setIdBicicleta(idBicicleta);
			reserva.setCreada(LocalDateTime.now());
			reserva.setCaducidad(LocalDateTime.now().plusMinutes(30));
			usuario.addReserva(reserva);
			servicioUsuario.actualizar(usuario);
		}
	}

	@Override
	public void confirmarReserva(String idUsuario) throws RepositorioException, EntidadNoEncontrada {
		Usuario usuario = servicioUsuario.recuperar(idUsuario);
		if (usuario.reservaActiva() != null) {
			Reserva reserva = usuario.reservaActiva();
			Alquiler alquiler = new Alquiler();
			alquiler.setInicio(LocalDateTime.now());
			alquiler.setIdBicicleta(reserva.getIdBicicleta());
			usuario.addAlquiler(alquiler);
			usuario.removeReserva(reserva);
			servicioUsuario.actualizar(usuario);
		}
	}

	@Override
	public void alquilar(String idUsuario, String idBicicleta) throws RepositorioException, EntidadNoEncontrada {
		Usuario usuario = servicioUsuario.recuperar(idUsuario);
		if (usuario.reservaActiva() == null && usuario.alquilerActivo() == null && !usuario.bloqueado()
				&& !usuario.superaTiempo()) {
			Alquiler alquiler = new Alquiler(idBicicleta);
			alquiler.setInicio(LocalDateTime.now());
			usuario.addAlquiler(alquiler);
			servicioUsuario.actualizar(usuario);
		}
	}

	@Override
	// historialUsuario(idUsuario): Usuario. Retorna la información con los
	// alquileres y reservas del usuario,
	// y el estado del servicio (bloqueado, tiempo de uso).
	// TODO tal vez crear otra clase que tenga esa informacion?
	public String historialUsuario(String idUsuario) throws RepositorioException, EntidadNoEncontrada {
		Usuario usuario = servicioUsuario.recuperar(idUsuario);
		return "[reservas=" + usuario.getReservas() + ", alquileres=" + usuario.getAlquileres() + ", bloqueado: "
				+ usuario.bloqueado() + ", Tiempo de uso: " + usuario.tiempoUsoSemana() + "]";
	}

	@Override
	public void dejarBicicleta(String idUsuario, String idEstacion) throws RepositorioException, EntidadNoEncontrada {
		Usuario usuario = servicioUsuario.recuperar(idUsuario);
		Alquiler alquiler = usuario.alquilerActivo();
		if (alquiler != null && servicioEstaciones.tieneHuecoDisponible(idEstacion)) {
			alquiler.setFin(LocalDateTime.now());
			// TODO No he actualizado la lista de alquileres del usuario
			servicioEstaciones.situarBicicleta(alquiler.getIdBicicleta(), idEstacion);
			servicioUsuario.actualizar(usuario);
		}
	}

	@Override
	public void liberarBloqueo(String idUsuario) throws RepositorioException, EntidadNoEncontrada {
		Usuario usuario = servicioUsuario.recuperar(idUsuario);
		//if (!usuario.getReservas().isEmpty()) {
			Iterator<Reserva> iterator = usuario.getReservas().iterator();
			while (iterator.hasNext()) {
	            Reserva r = iterator.next();
	            if (r.isCaducada()) {
	                iterator.remove();
	            }
	        }
		//}
		servicioUsuario.actualizar(usuario);
	}

}
