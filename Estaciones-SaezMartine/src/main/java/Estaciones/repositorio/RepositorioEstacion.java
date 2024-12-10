package Estaciones.repositorio;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import Estaciones.modelo.Estacion;

@NoRepositoryBean
public interface RepositorioEstacion extends PagingAndSortingRepository<Estacion, String>{
	
}
