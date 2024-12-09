package Estaciones.servicio;

import java.util.List;  

import Estaciones.modelo.Incidencia;
import repositorio.RepositorioException;

public interface IServicioIncidencias {
	
    String crear(String bicicleta, String descripcion) throws RepositorioException; 

    void actualizar(Incidencia incidencia) throws RepositorioException;
    
	void borrar(String id)  throws RepositorioException;

	Incidencia recuperar(String id)  throws RepositorioException;
	
	void cancelarIncidencia(String id, String motivo)throws RepositorioException;
	
	void asignarIncidencia(String id, String operario)throws RepositorioException;
	
	void resolverIncidencia(String id, String motivo, boolean reparada)throws RepositorioException;
	
	List<Incidencia> recuperarIncidenciasAbiertas() throws RepositorioException;
}
