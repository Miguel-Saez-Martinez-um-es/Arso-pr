package Estaciones.servicio;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import Estaciones.DTO.BicicletaDTO;
import Estaciones.DTO.EstacionDTO;
import Estaciones.modelo.Bicicleta;
import Estaciones.modelo.Estacion;
import repositorio.RepositorioException;

public interface IServicioEstacion {

	String crear(Estacion estacion) throws RepositorioException;

	void actualizar(Estacion estacion) throws RepositorioException;

	void borrar(String id) throws RepositorioException;

	Estacion getEstacion(String id) throws RepositorioException;

	List<Estacion> getEstaciones() throws RepositorioException;

	String crearBicicleta(Bicicleta bicicleta) throws RepositorioException;

	void actualizarBicicleta(Bicicleta bicicleta) throws RepositorioException;

	Bicicleta recuperarBicicleta(String idBicicleta) throws RepositorioException;

	void borrarBicicleta(String idBicicleta) throws RepositorioException;
	
	List<Bicicleta> getBicicletas() throws RepositorioException;
	
	String altaEstacion(String nombre, int capacidad, String direccion, double latitud, double longitud)
			throws RepositorioException;

	String altaBicicleta(String modelo, String idEstacion) throws RepositorioException;

	void estacionarBicicleta(String idBicicleta, String idEstacion) throws RepositorioException;

	void estacionarBicicleta(String idBicicleta) throws RepositorioException;

	void retirarBicicleta(String idBicicleta) throws RepositorioException;

	List<Bicicleta> recuperarBicicletasCercanasPosicion(double latitud, double longitud)
			throws RepositorioException;
	
	double calcularDistancia(double lat1, double lon1, double lat2, double lon2);
	
	boolean isCompleta(String idEstacion) throws RepositorioException;
	
	int bicicletasEnEstacion(String idEstacion) throws RepositorioException;
	
	Page<EstacionDTO> getListadoPaginadoEstaciones(Pageable pageable) throws Exception;
	
	Page<BicicletaDTO> getListadoPaginadoBicicletas(Pageable pageable, String id) throws Exception;
	
	Page<BicicletaDTO> getListadoPaginadoBicicletas2(Pageable pageable, String id) throws Exception;
}
