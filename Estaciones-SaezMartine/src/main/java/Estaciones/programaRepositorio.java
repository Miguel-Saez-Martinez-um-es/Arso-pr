package Estaciones;

import org.springframework.boot.SpringApplication; 
import org.springframework.context.ConfigurableApplicationContext;

import Estaciones.modelo.Estacion;
import Estaciones.repositorio.RepositorioBicis;
import Estaciones.repositorio.RepositorioEstacion;

public class programaRepositorio {

	public static void main(String[] args) {
		ConfigurableApplicationContext contexto = SpringApplication.run(EstacionesApp.class, args);
		
		// ...
		RepositorioEstacion repositorio = contexto.getBean(RepositorioEstacion.class);
		RepositorioBicis repositorioBicis = contexto.getBean(RepositorioBicis.class);

		
		System.out.println("Estaciones: " +repositorio.count());
		System.out.println("Bicis: " +repositorioBicis.count());

		System.out.println();
		for(Estacion e : repositorio.findAll())
			System.out.println("Estacion: "+e.getNombre());
		
		contexto.close();

	}

}
