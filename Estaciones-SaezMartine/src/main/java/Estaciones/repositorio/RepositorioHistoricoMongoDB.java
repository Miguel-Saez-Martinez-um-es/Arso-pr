package Estaciones.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;

import Estaciones.modelo.Historico;


public interface RepositorioHistoricoMongoDB  extends RepositorioHistorico, MongoRepository<Historico, String>  {
}
