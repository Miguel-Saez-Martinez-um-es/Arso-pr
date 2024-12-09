package Estaciones.repositorio;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import Estaciones.modelo.Historico;

@NoRepositoryBean
public interface RepositorioHistorico extends CrudRepository<Historico, String>{

}
