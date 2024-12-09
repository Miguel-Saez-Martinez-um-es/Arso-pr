package Estaciones.repositorio;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import Estaciones.modelo.Estacion;

@NoRepositoryBean
public interface RepositorioEstacion extends CrudRepository<Estacion, String>{
	
}
