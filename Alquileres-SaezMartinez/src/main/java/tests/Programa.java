package tests;

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
		String idEstacion = "Estacion 1";
		


		System.out.println();

		System.out.println(
				"----------------------------------Servicio Alquiler------------------------------------------------");

		System.out.println();
		Usuario u = servicioUsuario.recuperar(idUsuario);
		System.out.println("Usuario inicialmente");
		System.out.println("Usuario: " + u.toString());
		System.out.println();

		System.out.println("*******************Crear Reservas*********************");
		
		servicio.reservar(idUsuario, idBicicleta);
		System.out.println("\nCreamos una reserva y luego intentamos crear otra reserva (Aparece una nueva reserva y luego no cambia)");
		System.out.println("Usuario: "+u.toString());
		servicio.reservar(idUsuario, idBicicleta2);
		System.out.println("Usuario: "+u.toString());
		

		servicio.confirmarReserva(idUsuario);
		System.out.println("\nConfirmamos reserva (La reserva pasa a ser un alquiler)");
		System.out.println("Usuario: "+u.toString());
		
		
		System.out.println("\nDejamos la bicicleta (El alquiler pasa a tener fecha de fin)");
		servicio.dejarBicicleta(idUsuario, idEstacion);
		System.out.println("Usuario: "+u.toString());

		System.out.println("\nAlquilamos otra bicicleta (Aparece un nuevo alquiler)");
		servicio.alquilar(idUsuario, idBicicleta2);
		System.out.println("Usuario: "+u.toString());

		System.out.println("\nHistorial del usuario");
		System.out.println(servicio.historialUsuario(idUsuario));
		
		
		servicioUsuario.borrar(idUsuario);
		System.out.println(
				"-------------------------------------------------------------------------------------------------");

	}
}
