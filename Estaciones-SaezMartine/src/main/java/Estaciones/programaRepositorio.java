package Estaciones;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import Estaciones.modelo.Bicicleta;
import Estaciones.modelo.Estacion;
import Estaciones.servicio.IServicioEstacion;
import repositorio.RepositorioException;

public class programaRepositorio {

	public static void main(String[] args) throws RepositorioException {
		ConfigurableApplicationContext contexto = SpringApplication.run(EstacionesApp.class, args);

		IServicioEstacion servicio = contexto.getBean(IServicioEstacion.class);

		for (Bicicleta b : servicio.getBicicletas()) {
			servicio.deleteBicicleta(b.getModelo());
		}
		for (Estacion e : servicio.getEstaciones()) {
			servicio.deleteEstacion(e.getNombre());
		}

		// Estaciones

		Estacion estacion = new Estacion();
		estacion.setNombre("Estacion1");
		estacion.setCapacidad(1);
		estacion.setFechaAlta(LocalDate.now());
		estacion.setDireccion("Calle San Basilio");
		estacion.setLatitud(37.99633205165018);
		estacion.setLongitud(-1.1449311025769682);

		String idEstacion = servicio.addEstacion(estacion);
		
		String idEstacion2 = servicio.altaEstacion("Estacion2", 15, "Calle Mayor", 37.564824972468024,
				-1.2588498598537197);

		String idEstacion3 = servicio.altaEstacion("Estacion3", 5, "Avenida Tierno Galvan", 37.560160502584075,
				-1.2699767938007211);
		servicio.altaEstacion("Estacion4", 10, "Avenida Doctor Meca", 37.572165662442714, -1.266359944888294);

		// Bicicletas

		Bicicleta bici = new Bicicleta();
		bici.setCodigo(UUID.randomUUID().toString());
		bici.setFechaAlta(LocalDate.now());
		bici.setModelo("Modelo1");
		bici.setEstacion(idEstacion);

		Bicicleta bici2 = new Bicicleta();
		bici2.setCodigo(UUID.randomUUID().toString());
		bici2.setFechaAlta(LocalDate.now());
		bici2.setModelo("Modelo2");
		bici2.setEstacion(idEstacion2);

		servicio.addBicicleta(bici);
		servicio.addBicicleta(bici2);
		String bici3 = servicio.altaBicicleta("Modelo3", idEstacion3);
		servicio.altaBicicleta("Modelo4", idEstacion3);
		
		
		System.out.println();
		
		System.out.println("Bicicletas:");
		for (Bicicleta b : servicio.getBicicletas()) {
			System.out.println(b.getModelo() + " en la estacion: " + b.getEstacion());
		}
		
		System.out.println();
		
		System.out.println("Estaciones:");
		for (Estacion e : servicio.getEstaciones()) {
			int huecos = e.getCapacidad() - servicio.bicicletasEnEstacion(e.getNombre());
			System.out.println(
					e.getNombre() + " con id: " + e.getId() + " con " + servicio.bicicletasEnEstacion(e.getNombre()) + "/"
							+ e.getCapacidad() + " bicicletas. Huecos: " + huecos);
		}
		
		//Prueba del envio del evento
		
		servicio.bajaBicicleta(bici3);
		System.out.println();
		Bicicleta b = servicio.getBicicleta(bici3);
		System.out.println(b.getModelo() + " en la estacion: " + b.getEstacion());


		contexto.close();

	}

}
