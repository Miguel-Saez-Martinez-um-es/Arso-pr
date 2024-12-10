package Estaciones.repositorio;


import org.springframework.data.mongodb.repository.MongoRepository;

import Estaciones.modelo.Estacion;

public interface RepositorioEstacionMongoDB extends RepositorioEstacion, MongoRepository<Estacion, String> {

}
