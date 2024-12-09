package Estaciones.repositorio;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import Estaciones.modelo.Estacion;

public interface RepositorioEstacionMongoDB extends RepositorioEstacion, MongoRepository<Estacion, String> {

	// List<Encuesta> findByTitulo(String titulo);
	@Query("{'titulo': {$regex: ?0, $options: 'i'}}")
	List<Estacion> findByTituloContainingOrderByCierre(String titulo);
	
	@Query("{'apertura': {$lt: new Date()},'cierre': {$gt: new Date()}}")
	List<Estacion> findActivas();
}
