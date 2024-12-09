package Estaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import Estaciones.repositorio.RepositorioBicis;
import Estaciones.repositorio.RepositorioEstacion;
import Estaciones.repositorio.RepositorioHistorico;

public class programaRepositorio {

	public static void main(String[] args) {
		ConfigurableApplicationContext contexto = SpringApplication.run(EstacionesApp.class, args);
		
		// ...
		RepositorioEstacion repositorio = contexto.getBean(RepositorioEstacion.class);
		RepositorioBicis repositorioBicis = contexto.getBean(RepositorioBicis.class);
		RepositorioHistorico repositorioH = contexto.getBean(RepositorioHistorico.class);

		
		System.out.println("Estaciones: " +repositorio.count());
		System.out.println("Bicis: " +repositorioBicis.count());
		System.out.println("Historicos: " +repositorioH.count());

		
		contexto.close();

	}

}
