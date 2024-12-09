package Estaciones.servicio;

import java.util.List;  

import Estaciones.modelo.Bicicleta;
import Estaciones.modelo.Estacion;
import Estaciones.modelo.Historico;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;

public interface IServicioEstacion {

	String crear(Estacion estacion) throws RepositorioException;

	void actualizar(Estacion estacion) throws RepositorioException, EntidadNoEncontrada;

	void borrar(String id) throws RepositorioException, EntidadNoEncontrada;

	Estacion getEstacion(String id) throws RepositorioException, EntidadNoEncontrada;

	List<Estacion> getEstaciones() throws RepositorioException, EntidadNoEncontrada;

	String crearBicicleta(Bicicleta bicicleta) throws RepositorioException;

	void actualizarBicicleta(Bicicleta bicicleta) throws RepositorioException, EntidadNoEncontrada;

	Bicicleta recuperarBicicleta(String idBicicleta) throws RepositorioException, EntidadNoEncontrada;

	void borrarBicicleta(String idBicicleta) throws RepositorioException, EntidadNoEncontrada;
	
	List<Bicicleta> getBicicletas() throws RepositorioException, EntidadNoEncontrada;
	
	String crearHistorico(String idBicicleta, String idEstacion) throws RepositorioException;

	void actualizarHistorico(Historico historico) throws RepositorioException, EntidadNoEncontrada;

	Historico recuperarHistorico(String idBicicleta, String idEstacion) throws RepositorioException, EntidadNoEncontrada;

	void borrarHistorico(String idBicicleta, String idEstacion) throws RepositorioException, EntidadNoEncontrada;
	
	void borrarHistorico(Historico historico) throws RepositorioException, EntidadNoEncontrada;

	List<Historico> getHistoricos() throws RepositorioException, EntidadNoEncontrada;
	
	String altaEstacion(String nombre, int capacidad, String direccion, double latitud, double longitud)
			throws RepositorioException;

	String altaBicicleta(String modelo, String idEstacion) throws RepositorioException, EntidadNoEncontrada;

	void estacionarBicicleta(String idBicicleta, String idEstacion) throws RepositorioException, EntidadNoEncontrada;

	void estacionarBicicleta(String idBicicleta) throws RepositorioException, EntidadNoEncontrada;

	void retirarBicicleta(String idBicicleta) throws RepositorioException, EntidadNoEncontrada;

	List<Bicicleta> recuperarBicicletasCercanasPosicion(double latitud, double longitud)
			throws RepositorioException, EntidadNoEncontrada;
	
	double calcularDistancia(double lat1, double lon1, double lat2, double lon2);
	
	boolean isCompleta(String idEstacion) throws RepositorioException, EntidadNoEncontrada;
	
	int bicicletasEnEstacion(String idEstacion) throws RepositorioException, EntidadNoEncontrada;
}
