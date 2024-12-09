package Estaciones.repositorio;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import Estaciones.modelo.Bicicleta;

@NoRepositoryBean
public interface RepositorioBicis extends CrudRepository<Bicicleta, String>{

}
