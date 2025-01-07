package alquileres.servicio;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Iterator;

import alquileres.modelo.Alquiler;
import alquileres.modelo.Reserva;
import alquileres.modelo.Usuario;
import alquileres.rabbitmq.PublicadorEventosRabbitMQ;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import retrofit.IEstacionService;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import servicio.FactoriaServicios;

public class ServicioAlquileres implements IServicioAlquileres {

	public IEstacionService servicioEstaciones;
	private IServicioUsuario servicioUsuario = FactoriaServicios.getServicio(IServicioUsuario.class);
	private final PublicadorEventosRabbitMQ publicadorEventos = new PublicadorEventosRabbitMQ();

	public ServicioAlquileres() {

		// disponible hasta el 4/2/2025
		String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNaWd1ZWwtU2Flei1NYXJ0aW5lei11bS1lcyIsImV4cCI6MTczODY5NjQ5NSwicm9sIjoiZ2VzdG9yIn0.lm1i76Gwj5AaCrBT3biCH2LwnjKW-ViKkQBZV9g2QdU";

		OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
			Request originalRequest = chain.request();
			Request newRequest = originalRequest.newBuilder().header("Authorization", "Bearer " + jwtToken).build();
			return chain.proceed(newRequest);
		}).build();

		servicioEstaciones = new Retrofit.Builder().baseUrl("http://localhost:8080/")
				.addConverterFactory(JacksonConverterFactory.create()).client(client).build()
				.create(IEstacionService.class);
	}

	@Override
	public void reservar(String idUsuario, String idBicicleta) throws RepositorioException, EntidadNoEncontrada {
		if (idBicicleta == null) {
			throw new IllegalArgumentException("El ID de la bicicleta no puede ser null");
		}
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
			String idBicicleta = reserva.getIdBicicleta();
			Alquiler alquiler = new Alquiler();
			alquiler.setInicio(LocalDateTime.now());
			alquiler.setIdBicicleta(reserva.getIdBicicleta());
			usuario.addAlquiler(alquiler);
			usuario.removeReserva(reserva);
			servicioUsuario.actualizar(usuario);
			// publicamos la reserva como si fuera un alquiler
			publicadorEventos.publicarEvento("citybike.alquileres.bicicleta-alquilada", idBicicleta);
		}
	}

	@Override
	public void alquilar(String idUsuario, String idBicicleta) throws RepositorioException, EntidadNoEncontrada {
		if (idBicicleta == null) {
			throw new IllegalArgumentException("El ID de la bicicleta no puede ser null");
		}
		Usuario usuario = servicioUsuario.recuperar(idUsuario);
		if (usuario.reservaActiva() == null && usuario.alquilerActivo() == null && !usuario.bloqueado()
				&& !usuario.superaTiempo()) {
			Alquiler alquiler = new Alquiler(idBicicleta);
			alquiler.setInicio(LocalDateTime.now());
			usuario.addAlquiler(alquiler);
			servicioUsuario.actualizar(usuario);

			// TODO publicar evento del id de la bici alquilada ya no esta disponible
			publicadorEventos.publicarEvento("citybike.alquileres.bicicleta-alquilada", idBicicleta);
		}
	}

	@Override
	// historialUsuario(idUsuario): Usuario. Retorna la información con los
	// alquileres y reservas del usuario,
	// y el estado del servicio (bloqueado, tiempo de uso).
	public String historialUsuario(String idUsuario) throws RepositorioException, EntidadNoEncontrada {
		Usuario usuario = servicioUsuario.recuperar(idUsuario);
		return "[reservas=" + usuario.getReservas() + ", alquileres=" + usuario.getAlquileres() + ", bloqueado: "
				+ usuario.bloqueado() + ", Tiempo de uso: " + usuario.tiempoUsoSemana() + "]";
	}

	@Override
	public void dejarBicicleta(String idUsuario, String idEstacion)
			throws RepositorioException, EntidadNoEncontrada, IOException {
		if (idEstacion == null) {
			throw new IllegalArgumentException("El ID de la estacion no puede ser null");
		}
		Usuario usuario = servicioUsuario.recuperar(idUsuario);
		Alquiler alquiler = usuario.alquilerActivo();
		// deberia comprobar
		// servicioEstaciones.getEstacion(idEstacion).execute().body().getHuecos()
		if (alquiler != null && servicioEstaciones.getEstacion(idEstacion).execute().body().getHuecos() > 0) {
			alquiler.setFin(LocalDateTime.now());
			servicioEstaciones.estacionarBicicleta(alquiler.getIdBicicleta(), idEstacion);
			servicioUsuario.actualizar(usuario);

			String mensaje = "{ \"idBicicleta\": \"" + alquiler.getIdBicicleta() + "\", " + "\"idEstacion\": \""
					+ idEstacion + "\", " + "\"fechaFin\": \"" + alquiler.getFin() + "\" }";
			publicadorEventos.publicarEvento("citybike.alquileres.bicicleta-alquiler-concluido",
					mensaje);
		}
	}

	@Override
	public void liberarBloqueo(String idUsuario) throws RepositorioException, EntidadNoEncontrada {
		Usuario usuario = servicioUsuario.recuperar(idUsuario);
		Iterator<Reserva> iterator = usuario.getReservas().iterator();
		while (iterator.hasNext()) {
			Reserva r = iterator.next();
			if (r.isCaducada()) {
				iterator.remove();
			}
		}
		servicioUsuario.actualizar(usuario);
	}

	@Override
	public void eliminarReserva(String idBicicleta) throws RepositorioException, EntidadNoEncontrada {
		for (Usuario u : servicioUsuario.recuperarUsuarios()) {
			if (u.reservaActiva().getIdBicicleta().equals(idBicicleta)) {
				u.removeReserva(u.reservaActiva());
				return;
			}
		}
	}

}
