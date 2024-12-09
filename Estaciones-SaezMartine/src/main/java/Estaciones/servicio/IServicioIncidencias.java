package Estaciones.servicio;

import java.util.List; 

import Estaciones.modelo.Incidencia;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;

public interface IServicioIncidencias {
	
    String crear(String bicicleta, String descripcion) throws RepositorioException; 

    void actualizar(Incidencia incidencia) throws RepositorioException, EntidadNoEncontrada;
    
	void borrar(String id)  throws RepositorioException, EntidadNoEncontrada;

	Incidencia recuperar(String id)  throws RepositorioException, EntidadNoEncontrada;
	
	void cancelarIncidencia(String id, String motivo)throws RepositorioException, EntidadNoEncontrada;
	
	void asignarIncidencia(String id, String operario)throws RepositorioException, EntidadNoEncontrada;
	
	void resolverIncidencia(String id, String motivo, boolean reparada)throws RepositorioException, EntidadNoEncontrada;
	
	List<Incidencia> recuperarIncidenciasAbiertas() throws RepositorioException, EntidadNoEncontrada;
}
