package Estaciones.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;

import Estaciones.modelo.Incidencia;

public interface RepositorioIncidenciaMongoDB extends RepositorioIncidencia, MongoRepository<Incidencia, String>{

}
