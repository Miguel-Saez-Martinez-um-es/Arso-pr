package alquileres;

import java.time.LocalDateTime;
import alquileres.modelo.Alquiler;
import alquileres.modelo.Reserva;
import alquileres.modelo.Usuario;
import alquileres.servicio.IServicioAlquileres;
import alquileres.servicio.IServicioUsuario;
import servicio.FactoriaServicios;

public class Programa {

	public static void main(String[] args) throws Exception {

		IServicioAlquileres servicio = FactoriaServicios.getServicio(IServicioAlquileres.class);
		IServicioUsuario servicioUsuario = FactoriaServicios.getServicio(IServicioUsuario.class);

		String idUsuario = "Usuario 1";
		String idBicicleta = "Bicicleta 1";
		String idBicicleta2 = "Bicicleta 2";
		String idBicicleta3 = "Bicicleta 3";
		String idEstacion = "Estacion 1";
		


		System.out.println();

		System.out.println(
				"----------------------------------Servicio Alquiler------------------------------------------------");

		System.out.println();
		Usuario u;
		System.out.println("Usuario inicialmente");
		System.out.println("Usuario: " + servicioUsuario.recuperar(idUsuario).toString());
		System.out.println();

		System.out.println("*******************Crear Reservas*********************");
		
		servicio.reservar(idUsuario, idBicicleta);
		System.out.println("\nCreamos una reserva y luego intentamos crear otra reserva (Aparece una nueva reserva y luego no cambia)");
		System.out.println("Usuario: "+servicioUsuario.recuperar(idUsuario).toString());
		servicio.reservar(idUsuario, idBicicleta2);
		System.out.println("Usuario: "+servicioUsuario.recuperar(idUsuario).toString());
		

		servicio.confirmarReserva(idUsuario);
		System.out.println("\nConfirmamos reserva (La reserva pasa a ser un alquiler)");
		System.out.println("Usuario: "+servicioUsuario.recuperar(idUsuario).toString());
		
		
		System.out.println("\nDejamos la bicicleta (El alquiler pasa a tener fecha de fin)");
		servicio.dejarBicicleta(idUsuario, idEstacion);
		System.out.println("Usuario: "+servicioUsuario.recuperar(idUsuario).toString());

		LocalDateTime creada = LocalDateTime.now().minusDays(2);
		LocalDateTime caducidad = LocalDateTime.now().minusDays(1);


		// Creamos y añadimos 3 reservas ya caducadas al usuario
		Reserva r1 = new Reserva(idBicicleta, creada, caducidad);
		Reserva r2 = new Reserva(idBicicleta, creada, caducidad);
		Reserva r3 = new Reserva(idBicicleta, creada, caducidad);
		u = servicioUsuario.recuperar(idUsuario);
		u.addReserva(r1);
		u.addReserva(r2);
		u.addReserva(r3);
		servicioUsuario.actualizar(u);
		
		System.out.println("\nAñadimos tres reservas caducadas (aparecen 3 nuevas reservas y bloqueado: true)");
		System.out.println(servicio.historialUsuario(idUsuario));

		System.out.println("\nLiberamos el bloqueo del usuario (desaparecen todas las reservas y bloqueado: false)");
		servicio.liberarBloqueo(idUsuario);
		System.out.println(servicio.historialUsuario(idUsuario));

		

		
		/*
		System.out.println("\nAlquilamos otra bicicleta (Aparece un nuevo alquiler)");
		servicio.alquilar(idUsuario, idBicicleta2);
		System.out.println("Usuario: "+servicioUsuario.recuperar(idUsuario).toString());
		
		System.out.println("\nDejamos la bicicleta (El 2º alquiler pasa a tener fecha de fin)");
		servicio.dejarBicicleta(idUsuario, idEstacion);
		System.out.println("Usuario: "+servicioUsuario.recuperar(idUsuario).toString());
		*/
		

		System.out.println("\nAñadimos un alquiler que se pasa de tiempo (aparece un nuevo alquiler con fecha de ayer)");
		// Creamos y añadimos un alquiler que supere el tiempo de uso
		LocalDateTime inicio = LocalDateTime.now().minusDays(2);
		LocalDateTime fin = inicio.plusMinutes(180);
		Alquiler alquiler = new Alquiler(idBicicleta2);
		alquiler.setInicio(inicio);
		alquiler.setFin(fin);
		
		u = servicioUsuario.recuperar(idUsuario);
		u.addAlquiler(alquiler);
		servicioUsuario.actualizar(u);
		
		System.out.println("Usuario: "+servicioUsuario.recuperar(idUsuario).toString());

		// Tratamos de reservar una bicicleta
		servicio.alquilar(idUsuario, idBicicleta3);

		// Comprobamos que la bicicleta no se ha podido reservar
		System.out.println("\nTratamos de alquilar una bicicleta cuando nos pasamos de tiempo de uso (no cambia)");
		System.out.println("Usuario: "+servicioUsuario.recuperar(idUsuario).toString());
		
		servicioUsuario.borrar(idUsuario);
		System.out.println(
				"-------------------------------------------------------------------------------------------------");

	}
}
