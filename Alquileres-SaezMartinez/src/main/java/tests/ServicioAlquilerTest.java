package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import alquileres.modelo.Alquiler;
import alquileres.modelo.Reserva;
import alquileres.modelo.Usuario;
import alquileres.servicio.IServicioAlquileres;
import alquileres.servicio.IServicioUsuario;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import servicio.FactoriaServicios;

public class ServicioAlquilerTest {

	String idUsuario;
	String idBicicleta;
	String idBicicleta2;
	String idEstacion;
	Usuario usuario;
	IServicioAlquileres servicio;
	IServicioUsuario servicioUsuario;

	@Before
	public void setUp() throws Exception {
		servicio = FactoriaServicios.getServicio(IServicioAlquileres.class);
		servicioUsuario = FactoriaServicios.getServicio(IServicioUsuario.class);
		idUsuario = "Usuario 1";
		idBicicleta = "Bicicleta 1";
		idBicicleta2 = "Bicicleta 2";
		idEstacion = "Estacion 1";
		usuario = new Usuario(idUsuario);
		servicioUsuario.borrar(idUsuario);
	}

	@Test
	public void testReservar() throws RepositorioException, EntidadNoEncontrada {

		// No hay ninguna reservaActiva en este momento
		assertTrue(usuario.getReservas().isEmpty());

		// Reservamos una bicicleta
		servicio.reservar(idUsuario, idBicicleta);

		// Comprobamos que la bicicleta en la reservaActiva es igual a la bicicleta de
		// la reserva hecha
		assertTrue(servicioUsuario.recuperar(idUsuario).reservaActiva().getIdBicicleta().equals(idBicicleta));
	}

	@Test
	public void testReservarConReservaActiva() throws RepositorioException, EntidadNoEncontrada {

		// No hay ninguna reservaActiva en este momento
		assertTrue(usuario.getReservas().isEmpty());

		// Reservamos una bicicleta y tratamos de reservar otra bicicleta
		servicio.reservar(idUsuario, idBicicleta);
		servicio.reservar(idUsuario, idBicicleta2);

		// Comprobamos que la bicicleta de la reservaActiva es la primera bicicleta
		assertTrue(servicioUsuario.recuperar(idUsuario).reservaActiva().getIdBicicleta().equals(idBicicleta));
	}

	@Test
	public void testReservarConAlquilerActivo() throws RepositorioException, EntidadNoEncontrada {

		// No hay ninguna reservaActiva o alquilerActivo en este momento
		assertTrue(usuario.getReservas().isEmpty());
		assertTrue(usuario.getAlquileres().isEmpty());

		// Alquilamos una bicicleta e intentamos reservar otra bicicleta
		servicio.alquilar(idUsuario, idBicicleta);
		servicio.reservar(idUsuario, idBicicleta2);

		// Comprobamos que el alquiler activo es sobre la bicicleta alquilada y que no
		// existe ninguna reserva
		assertTrue(servicioUsuario.recuperar(idUsuario).alquilerActivo().getIdBicicleta().equals(idBicicleta));
		assertTrue(servicioUsuario.recuperar(idUsuario).reservaActiva() == null);
	}

	@Test
	public void testReservarBloqueado() throws RepositorioException, EntidadNoEncontrada {

		Usuario usuario = servicioUsuario.recuperar(idUsuario);
		LocalDateTime creada = LocalDateTime.now().minusDays(2);
		LocalDateTime caducidad = LocalDateTime.now().minusDays(1);

		// No hay ninguna reserva en este momento
		assertTrue(usuario.getReservas().isEmpty());

		// Creamos y añadimos 3 reservas ya caducadas al usuario
		Reserva r1 = new Reserva(idBicicleta, creada, caducidad);
		Reserva r2 = new Reserva(idBicicleta, creada, caducidad);
		Reserva r3 = new Reserva(idBicicleta, creada, caducidad);

		usuario.addReserva(r1);
		usuario.addReserva(r2);
		usuario.addReserva(r3);

		// Tratamos de reservar una bicicleta
		servicio.reservar(idUsuario, idBicicleta);

		// Comprobamos que la bicicleta no se ha podido reservar
		assertTrue(servicioUsuario.recuperar(idUsuario).reservaActiva() == null);
	}

	@Test
	public void testReservarSuperaTiempo() throws RepositorioException, EntidadNoEncontrada {

		Usuario usuario = servicioUsuario.recuperar(idUsuario);
		LocalDateTime inicio = LocalDateTime.now().minusDays(2);
		LocalDateTime fin = inicio.plusMinutes(180);

		// No hay ninguna reservaActiva o alquilerActivo en este momento
		assertTrue(usuario.getReservas().isEmpty());
		assertTrue(usuario.getAlquileres().isEmpty());

		// Creamos y añadimos un alquiler que supere el tiempo de uso
		Alquiler alquiler = new Alquiler(idBicicleta);
		alquiler.setInicio(inicio);
		alquiler.setFin(fin);

		usuario.addAlquiler(alquiler);

		// Tratamos de reservar una bicicleta
		servicio.reservar(idUsuario, idBicicleta);

		// Comprobamos que la bicicleta no se ha podido reservar
		assertTrue(servicioUsuario.recuperar(idUsuario).reservaActiva() == null);
	}

	@Test
	public void testConfirmarReserva() throws RepositorioException, EntidadNoEncontrada {

		// No hay ninguna reservaActiva o alquilerActivo en este momento
		assertTrue(usuario.getReservas().isEmpty());
		assertTrue(usuario.getAlquileres().isEmpty());

		// Reservamos una bicicleta
		servicio.reservar(idUsuario, idBicicleta);

		// Confirmamos la reserva
		servicio.confirmarReserva(idUsuario);

		// Comprobamos que la bicicleta en el alquilerActivo es igual a la bicicleta de
		// la reserva hecha
		assertTrue(servicioUsuario.recuperar(idUsuario).alquilerActivo().getIdBicicleta().equals(idBicicleta));
	}

	@Test
	public void testConfirmarReservaSinReservaActiva() throws RepositorioException, EntidadNoEncontrada {

		// No hay ninguna reservaActiva o alquilerActivo en este momento
		assertTrue(usuario.getReservas().isEmpty());
		assertTrue(usuario.getAlquileres().isEmpty());

		// Confirmamos la reserva
		servicio.confirmarReserva(idUsuario);

		// Comprobamos que la bicicleta en el alquilerActivo es igual a la bicicleta de
		// la reserva hecha
		assertTrue(servicioUsuario.recuperar(idUsuario).alquilerActivo() == null);
	}

	@Test
	public void testAlquilar() throws RepositorioException, EntidadNoEncontrada {

		// No hay ningun alquilerActivo en este momento
		assertTrue(usuario.getAlquileres().isEmpty());

		// Alquilamos una bicicleta
		servicio.alquilar(idUsuario, idBicicleta);

		// Comprobamos que la bicicleta en el alquilerActivo es igual a la bicicleta del
		// alquiler
		assertTrue(servicioUsuario.recuperar(idUsuario).alquilerActivo().getIdBicicleta().equals(idBicicleta));
	}

	@Test
	public void testAlquilarConReservaActiva() throws RepositorioException, EntidadNoEncontrada {

		// No hay ninguna reservaActiva o alquilerActivo en este momento
		assertTrue(usuario.getReservas().isEmpty());
		assertTrue(usuario.getAlquileres().isEmpty());

		// Reservamos una bicicleta
		servicio.reservar(idUsuario, idBicicleta);

		// Intentamos alquilar otra bicicleta
		servicio.alquilar(idUsuario, idBicicleta2);

		// Comprobamos que la bicicleta en la reservaActiva es igual a la bicicleta del
		// reservada y que no hay ningun alquiler activo
		assertTrue(servicioUsuario.recuperar(idUsuario).reservaActiva().getIdBicicleta().equals(idBicicleta));
		assertTrue(servicioUsuario.recuperar(idUsuario).alquilerActivo()==null);
	}
	
	@Test
	public void testAlquilarConAlquilerActivo() throws RepositorioException, EntidadNoEncontrada {

		// No hay ninguna reservaActiva o alquilerActivo en este momento
		assertTrue(usuario.getReservas().isEmpty());
		assertTrue(usuario.getAlquileres().isEmpty());

		// Alquilamos una bicicleta
		servicio.alquilar(idUsuario, idBicicleta);

		// Intentamos alquilar otra bicicleta
		servicio.alquilar(idUsuario, idBicicleta2);

		// Comprobamos que la bicicleta en la reservaActiva es igual a la bicicleta del
		// reservada y que no hay ningun alquiler activo
		assertTrue(servicioUsuario.recuperar(idUsuario).alquilerActivo().getIdBicicleta().equals(idBicicleta));
		assertFalse(servicioUsuario.recuperar(idUsuario).alquilerActivo().getIdBicicleta().equals(idBicicleta2));
	}
	
	@Test
	public void testAlquilarBloqueado() throws RepositorioException, EntidadNoEncontrada {

		Usuario usuario = servicioUsuario.recuperar(idUsuario);
		LocalDateTime creada = LocalDateTime.now().minusDays(2);
		LocalDateTime caducidad = LocalDateTime.now().minusDays(1);

		// No hay ninguna reserva en este momento
		assertTrue(usuario.getReservas().isEmpty());

		// Creamos y añadimos 3 reservas ya caducadas al usuario
		Reserva r1 = new Reserva(idBicicleta, creada, caducidad);
		Reserva r2 = new Reserva(idBicicleta, creada, caducidad);
		Reserva r3 = new Reserva(idBicicleta, creada, caducidad);

		usuario.addReserva(r1);
		usuario.addReserva(r2);
		usuario.addReserva(r3);

		// Tratamos de reservar una bicicleta
		servicio.alquilar(idUsuario, idBicicleta);

		// Comprobamos que la bicicleta no se ha podido reservar
		assertTrue(servicioUsuario.recuperar(idUsuario).alquilerActivo() == null);
	}

	@Test
	public void testAlquilarSuperaTiempo() throws RepositorioException, EntidadNoEncontrada {

		Usuario usuario = servicioUsuario.recuperar(idUsuario);
		LocalDateTime inicio = LocalDateTime.now().minusDays(2);
		LocalDateTime fin = inicio.plusMinutes(180);

		// No hay ninguna reservaActiva o alquilerActivo en este momento
		assertTrue(usuario.getReservas().isEmpty());
		assertTrue(usuario.getAlquileres().isEmpty());

		// Creamos y añadimos un alquiler que supere el tiempo de uso
		Alquiler alquiler = new Alquiler(idBicicleta);
		alquiler.setInicio(inicio);
		alquiler.setFin(fin);

		usuario.addAlquiler(alquiler);

		// Tratamos de reservar una bicicleta
		servicio.alquilar(idUsuario, idBicicleta);

		// Comprobamos que la bicicleta no se ha podido reservar
		assertTrue(servicioUsuario.recuperar(idUsuario).alquilerActivo() == null);
	}
	
	@Test
	public void testLiberarBloqueoBloqueado() throws RepositorioException, EntidadNoEncontrada {

		Usuario usuario = servicioUsuario.recuperar(idUsuario);
		LocalDateTime creada = LocalDateTime.now().minusDays(2);
		LocalDateTime caducidad = LocalDateTime.now().minusDays(1);
		
		// Creamos y añadimos 3 reservas ya caducadas al usuario
		Reserva r1 = new Reserva(idBicicleta, creada, caducidad);
		Reserva r2 = new Reserva(idBicicleta, creada, caducidad);
		Reserva r3 = new Reserva(idBicicleta, creada, caducidad);

		usuario.addReserva(r1);
		usuario.addReserva(r2);
		usuario.addReserva(r3);
		
		// Comprobamos que el usuario esta bloqueado
		assertTrue(usuario.bloqueado());

		// Liberamos el bloqueo
		servicio.liberarBloqueo(idUsuario);
		
		// Comprobamos que el usuario ya no esta bloqueado
		assertFalse(usuario.bloqueado());
	}

	@Test
	public void testLiberarBloqueoNoBloqueado() throws RepositorioException, EntidadNoEncontrada {

		Usuario usuario = servicioUsuario.recuperar(idUsuario);
		
		// Comprobamos que el usuario no esta bloqueado
		assertFalse(usuario.bloqueado());

		// Liberamos el bloqueo
		servicio.liberarBloqueo(idUsuario);
		
		// Comprobamos que el usuario sigue sin estar bloqueado
		assertFalse(usuario.bloqueado());
	}
	
	@Test
	public void testDejarBicicleta() throws RepositorioException, EntidadNoEncontrada {
		
		// Alquilamos una bicicleta
		servicio.alquilar(idUsuario, idBicicleta);
	
		// Comprobamos que la bicicleta en el alquilerActivo es igual a la bicicleta del
		// alquiler
		assertTrue(servicioUsuario.recuperar(idUsuario).alquilerActivo().getIdBicicleta().equals(idBicicleta));
		
		// Dejamos la bicicleta
		servicio.dejarBicicleta(idUsuario, idEstacion);
		
		// Comprobamos que el usuario ya no tienen ningun alquilerActivo
		assertTrue(servicioUsuario.recuperar(idUsuario).alquilerActivo()==null);
	}

	
}
