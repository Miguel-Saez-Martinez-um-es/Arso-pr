package Estaciones;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import Estaciones.modelo.Bicicleta;
import Estaciones.modelo.Estacion;
import Estaciones.modelo.Historico;
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
		String idEstacion = servicio.crear(estacion);
		String idEstacion2 = servicio.altaEstacion("Estacion 2", 15, "Calle Mayor", 37.564824972468024,
				-1.2588498598537197);
		System.out.println("Estacion 1: " + servicio.getEstacion(idEstacion));
		System.out.println("Estacion 2: " + servicio.getEstacion(idEstacion2));

		System.out.println();

		System.out.println();
		System.out.println("*******************Crear Bicicletas*********************");
		String idBici = servicio.crearBicicleta(bici);
		String idBici2 = servicio.altaBicicleta("Modelo 2", servicio.getEstacion(idEstacion2).getId());

		System.out.println("Modelo 1: " + servicio.recuperarBicicleta(idBici));
		System.out.println("Modelo 2: " + servicio.recuperarBicicleta(idBici2));

		System.out.println();
		System.out.println("**************Estacionamos/Retiramos Bicicletas*********");
		System.out.println("Hay plazas libres en " + servicio.getEstacion(idEstacion).getNombre() + ": "
				+ servicio.isCompleta(idEstacion) + ". Capacidad: " + servicio.bicicletasEnEstacion(idEstacion) + "/"
				+ servicio.getEstacion(idEstacion).getCapacidad());
		System.out.println("Hay plazas libres en " + servicio.getEstacion(idEstacion2).getNombre() + ": "
				+ servicio.isCompleta(idEstacion2) + ". Capacidad: " + servicio.bicicletasEnEstacion(idEstacion2) + "/"
				+ servicio.getEstacion(idEstacion2).getCapacidad());

		System.out.println();

		System.out.println("Estacionamos " + servicio.recuperarBicicleta(idBici).getModelo() + " en "
				+ servicio.getEstacion(idEstacion2).getNombre());
		servicio.estacionarBicicleta(idBici, idEstacion2);
		System.out.println("Hay plazas libres en " + servicio.getEstacion(idEstacion2).getNombre() + ": "
				+ servicio.isCompleta(idEstacion2) + ". Capacidad: " + servicio.bicicletasEnEstacion(idEstacion2) + "/"
				+ servicio.getEstacion(idEstacion2).getCapacidad());

		System.out.println();

		System.out.println("Retiramos " + servicio.recuperarBicicleta(idBici).getModelo());
		servicio.retirarBicicleta(idBici);
		System.out.println("Estacionamos " + servicio.recuperarBicicleta(idBici).getModelo() + " en "
				+ servicio.getEstacion(idEstacion).getNombre());
		servicio.estacionarBicicleta(idBici, idEstacion);

		System.out.println("Hay plazas libres en Estacion 1: " + servicio.isCompleta(idEstacion) + ". Capacidad: "
				+ servicio.bicicletasEnEstacion(idEstacion) + "/" + servicio.getEstacion(idEstacion).getCapacidad());
		System.out.println("Hay plazas libres en Estacion 2: " + servicio.isCompleta(idEstacion2) + ". Capacidad: "
				+ servicio.bicicletasEnEstacion(idEstacion2) + "/" + servicio.getEstacion(idEstacion2).getCapacidad());

		System.out.println();

		System.out.println("Creamos una nueva bicicleta en " + servicio.getEstacion(idEstacion2).getNombre());
		String idBici3 = servicio.altaBicicleta("Modelo 3", servicio.getEstacion(idEstacion2).getId());
		System.out.println("Modelo 3: " + servicio.recuperarBicicleta(idBici3));
		System.out.println("Hay plazas libres en " + servicio.getEstacion(idEstacion2).getNombre() + ": "
				+ servicio.isCompleta(idEstacion2) + ". Capacidad: " + servicio.bicicletasEnEstacion(idEstacion2) + "/"
				+ servicio.getEstacion(idEstacion2).getCapacidad());

		System.out.println();

		System.out.println("Retiramos " + servicio.recuperarBicicleta(idBici3).getModelo());
		servicio.retirarBicicleta(idBici3);
		System.out.println("Hay plazas libres en " + servicio.getEstacion(idEstacion2).getNombre() + ": "
				+ servicio.isCompleta(idEstacion2) + ". Capacidad: " + servicio.bicicletasEnEstacion(idEstacion2) + "/"
				+ servicio.getEstacion(idEstacion2).getCapacidad());

		System.out.println();

		System.out.println("Estacionamos por defecto " + servicio.recuperarBicicleta(idBici3).getModelo());
		servicio.estacionarBicicleta(idBici3);
		System.out.println("Hay plazas libres en Estacion 1: " + servicio.isCompleta(idEstacion) + ". Capacidad: "
				+ servicio.bicicletasEnEstacion(idEstacion) + "/" + servicio.getEstacion(idEstacion).getCapacidad());
		System.out.println("Hay plazas libres en Estacion 2: " + servicio.isCompleta(idEstacion2) + ". Capacidad: "
				+ servicio.bicicletasEnEstacion(idEstacion2) + "/" + servicio.getEstacion(idEstacion2).getCapacidad());

		System.out.println();

		System.out.println("*******************Historicos***************************");
		System.out.println("Historicos:");
		for (Historico h : servicio.getHistoricos()) {
			System.out.println(h.toString());
		}

		System.out.println();

		System.out.println("***************Comprobamos bicicletas cercanas**********");

		System.out.println(
				"Creamos 2 Estaciones mas que esten cerca de " + servicio.getEstacion(idEstacion2).getNombre());
		String idEstacion3 = servicio.altaEstacion("Estacion 3", 5, "Avenida Tierno Galvan", 37.560160502584075,
				-1.2699767938007211);
		String idEstacion4 = servicio.altaEstacion("Estacion 4", 10, "Avenida Doctor Meca", 37.572165662442714,
				-1.266359944888294);
		System.out.println("Estacion 3: " + servicio.getEstacion(idEstacion3));
		System.out.println("Estacion 4: " + servicio.getEstacion(idEstacion4));

		System.out.println();

		System.out.println("Bicicletas cercanas a : " + servicio.getEstacion(idEstacion2).getNombre());
		// Coordenadas de Estacion 2 = 37.564824972468024, -1.2588498598537197
		for (Bicicleta b : servicio.recuperarBicicletasCercanasPosicion(37.464824972468024, -1.1588498598537197)) {
			System.out.println(b.getModelo() + " en estacion " + servicio.getEstacion(b.getEstacion()).getNombre());
		}

		System.out.println();

		// Borrar datos
		for (Bicicleta b : servicio.getBicicletas()) {
			servicio.borrarBicicleta(b.getId());
		}
		for (Estacion e : servicio.getEstaciones()) {
			servicio.borrar(e.getId());
		}
		for (Historico h : servicio.getHistoricos()) {
			servicio.borrarHistorico(h);
		}
		System.out.println(
				"-------------------------------------------------------------------------------------------------");
		
		contexto.close();
	}
}
