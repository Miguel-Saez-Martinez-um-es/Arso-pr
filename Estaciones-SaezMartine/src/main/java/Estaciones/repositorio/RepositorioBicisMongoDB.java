package Estaciones.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;

import Estaciones.modelo.Bicicleta;

public interface RepositorioBicisMongoDB extends RepositorioBicis, MongoRepository<Bicicleta, String>  {

}
