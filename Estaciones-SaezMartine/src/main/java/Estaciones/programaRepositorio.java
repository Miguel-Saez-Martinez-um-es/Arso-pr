package Estaciones;

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
		
		System.out.println();
		System.out.println("Estaciones:");
		for(Estacion e : servicio.getEstaciones()) {
			int huecos = e.getCapacidad()-servicio.bicicletasEnEstacion(e.getId());
			System.out.println(e.getNombre() + " con id: " + e.getId() + " con " + servicio.bicicletasEnEstacion(e.getId()) + "/" + e.getCapacidad() + " bicicletas. Huecos: " + huecos);
		}
				
		System.out.println("Bicicletas:");
		for(Bicicleta b : servicio.getBicicletas()) {
			System.out.println(b.getModelo() + " en la estacion: " + b.getEstacion()  + " con codigo: " + b.getCodigo());
		}
		
		
		contexto.close();


	}

}
