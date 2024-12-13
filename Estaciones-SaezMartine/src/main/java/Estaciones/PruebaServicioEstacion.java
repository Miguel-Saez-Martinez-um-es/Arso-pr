package Estaciones;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import Estaciones.modelo.Bicicleta;
import Estaciones.modelo.Estacion;
import Estaciones.servicio.IServicioEstacion;

public class PruebaServicioEstacion {
	public static void main(String[] args) throws Exception {

		ConfigurableApplicationContext contexto = SpringApplication.run(EstacionesApp.class, args);
		
		IServicioEstacion servicio = contexto.getBean(IServicioEstacion.class);
		
		

		// repositorio.initialize(new Historico());

		Bicicleta bici = new Bicicleta();
		bici.setCodigo(UUID.randomUUID().toString());
		bici.setFechaAlta(LocalDate.now());
		bici.setModelo("Modelo 1");

		Estacion estacion = new Estacion();
		estacion.setNombre("Estacion 1");
		estacion.setCapacidad(1);
		estacion.setFechaAlta(LocalDate.now());
		estacion.setDireccion("Calle San Basilio");
		estacion.setLatitud(37.99633205165018);
		estacion.setLongitud(-1.1449311025769682);

		System.out.println(
				"----------------------------------Servicio Estacion------------------------------------------------");

		System.out.println();

		System.out.println("*******************Crear Estaciones*********************");
		String idEstacion = servicio.addEstacion(estacion);
		String idEstacion2 = servicio.altaEstacion("Estacion 2", 15, "Calle Mayor", 37.564824972468024,
				-1.2588498598537197);
		System.out.println("Estacion 1: " + servicio.getEstacion(idEstacion));
		System.out.println("Estacion 2: " + servicio.getEstacion(idEstacion2));

		System.out.println();

		System.out.println();
		System.out.println("*******************Crear Bicicletas*********************");
		String idBici = servicio.addBicicleta(bici);
		String idBici2 = servicio.altaBicicleta("Modelo 2", servicio.getEstacion(idEstacion2).getId());

		System.out.println("Modelo 1: " + servicio.getBicicleta(idBici));
		System.out.println("Modelo 2: " + servicio.getBicicleta(idBici2));

		System.out.println();
		System.out.println("**************Estacionamos/Retiramos Bicicletas*********");
		System.out.println("Hay plazas libres en " + servicio.getEstacion(idEstacion).getNombre() + ": "
				+ servicio.isCompleta(idEstacion) + ". Capacidad: " + servicio.bicicletasEnEstacion(idEstacion) + "/"
				+ servicio.getEstacion(idEstacion).getCapacidad());
		System.out.println("Hay plazas libres en " + servicio.getEstacion(idEstacion2).getNombre() + ": "
				+ servicio.isCompleta(idEstacion2) + ". Capacidad: " + servicio.bicicletasEnEstacion(idEstacion2) + "/"
				+ servicio.getEstacion(idEstacion2).getCapacidad());

		System.out.println();

		System.out.println("Estacionamos " + servicio.getBicicleta(idBici).getModelo() + " en "
				+ servicio.getEstacion(idEstacion2).getNombre());
		servicio.estacionarBicicleta(idBici, idEstacion2);
		System.out.println("Hay plazas libres en " + servicio.getEstacion(idEstacion2).getNombre() + ": "
				+ servicio.isCompleta(idEstacion2) + ". Capacidad: " + servicio.bicicletasEnEstacion(idEstacion2) + "/"
				+ servicio.getEstacion(idEstacion2).getCapacidad());

		System.out.println();

		System.out.println("Retiramos " + servicio.getBicicleta(idBici).getModelo());
		servicio.retirarBicicleta(idBici);
		System.out.println("Estacionamos " + servicio.getBicicleta(idBici).getModelo() + " en "
				+ servicio.getEstacion(idEstacion).getNombre());
		servicio.estacionarBicicleta(idBici, idEstacion);

		System.out.println("Hay plazas libres en Estacion 1: " + servicio.isCompleta(idEstacion) + ". Capacidad: "
				+ servicio.bicicletasEnEstacion(idEstacion) + "/" + servicio.getEstacion(idEstacion).getCapacidad());
		System.out.println("Hay plazas libres en Estacion 2: " + servicio.isCompleta(idEstacion2) + ". Capacidad: "
				+ servicio.bicicletasEnEstacion(idEstacion2) + "/" + servicio.getEstacion(idEstacion2).getCapacidad());

		System.out.println();

		System.out.println("Creamos una nueva bicicleta en " + servicio.getEstacion(idEstacion2).getNombre());
		String idBici3 = servicio.altaBicicleta("Modelo 3", servicio.getEstacion(idEstacion2).getId());
		System.out.println("Modelo 3: " + servicio.getBicicleta(idBici3));
		System.out.println("Hay plazas libres en " + servicio.getEstacion(idEstacion2).getNombre() + ": "
				+ servicio.isCompleta(idEstacion2) + ". Capacidad: " + servicio.bicicletasEnEstacion(idEstacion2) + "/"
				+ servicio.getEstacion(idEstacion2).getCapacidad());

		System.out.println();

		System.out.println("Retiramos " + servicio.getBicicleta(idBici3).getModelo());
		servicio.retirarBicicleta(idBici3);
		System.out.println("Hay plazas libres en " + servicio.getEstacion(idEstacion2).getNombre() + ": "
				+ servicio.isCompleta(idEstacion2) + ". Capacidad: " + servicio.bicicletasEnEstacion(idEstacion2) + "/"
				+ servicio.getEstacion(idEstacion2).getCapacidad());

		System.out.println();

		System.out.println("Estacionamos por defecto " + servicio.getBicicleta(idBici3).getModelo());
		servicio.estacionarBicicleta(idBici3,idEstacion2);
		System.out.println("Hay plazas libres en Estacion 1: " + servicio.isCompleta(idEstacion) + ". Capacidad: "
				+ servicio.bicicletasEnEstacion(idEstacion) + "/" + servicio.getEstacion(idEstacion).getCapacidad());
		System.out.println("Hay plazas libres en Estacion 2: " + servicio.isCompleta(idEstacion2) + ". Capacidad: "
				+ servicio.bicicletasEnEstacion(idEstacion2) + "/" + servicio.getEstacion(idEstacion2).getCapacidad());

		System.out.println();

		System.out.println("Estaciones:");
		for(Estacion e : servicio.getEstaciones()) {
			System.out.println(e.getNombre() + " con id: " + e.getId());
		}
		
		System.out.println("Bicicletas:");
		for(Bicicleta b : servicio.getBicicletas()) {
			System.out.println(b.getModelo() + "en la estacion: " + b.getEstacion()  + " con codigo: " + b.getCodigo());
		}
		
		// Borrar datos
		for (Bicicleta b : servicio.getBicicletas()) {
			servicio.deleteBicicleta(b.getCodigo());
		}
		for (Estacion e : servicio.getEstaciones()) {
			servicio.deleteEstacion(e.getId());
		}
		System.out.println(
				"-------------------------------------------------------------------------------------------------");
		
		contexto.close();
	}
}
