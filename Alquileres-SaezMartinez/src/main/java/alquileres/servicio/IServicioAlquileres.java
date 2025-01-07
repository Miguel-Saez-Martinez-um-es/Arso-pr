package alquileres.servicio;

import java.io.IOException;

import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;

public interface IServicioAlquileres {

	void reservar(String idUsuario, String idBicicleta)  throws RepositorioException, EntidadNoEncontrada;

	void confirmarReserva(String idUsuario)  throws RepositorioException, EntidadNoEncontrada;

	void alquilar(String idUsuario, String idBicicleta)  throws RepositorioException, EntidadNoEncontrada;
	
	String historialUsuario(String idUsuario)  throws RepositorioException, EntidadNoEncontrada;

	void dejarBicicleta(String idUsuario, String idEstacion) throws RepositorioException, EntidadNoEncontrada, IOException;
	
	void liberarBloqueo(String idUsuario)  throws RepositorioException, EntidadNoEncontrada;
	
	void eliminarReserva(String idBicicleta)throws RepositorioException, EntidadNoEncontrada;
	
}
