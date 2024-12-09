package Estaciones.repositorio;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import Estaciones.modelo.Incidencia;

@NoRepositoryBean
public interface RepositorioIncidencia extends CrudRepository<Incidencia, String>{

}
