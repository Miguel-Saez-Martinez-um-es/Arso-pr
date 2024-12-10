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

	String addEstacion(Estacion estacion) throws RepositorioException;

	void updateEstacion(Estacion estacion) throws RepositorioException;

	void deleteEstacion(String id) throws RepositorioException;

	Estacion getEstacion(String id) throws RepositorioException;
	

	List<Estacion> getEstaciones() throws RepositorioException;

	String addBicicleta(Bicicleta bicicleta) throws RepositorioException;

	void updateBicicleta(Bicicleta bicicleta) throws RepositorioException;

	Bicicleta getBicicleta(String idBicicleta) throws RepositorioException;

	void deleteBicicleta(String idBicicleta) throws RepositorioException;
	
	List<Bicicleta> getBicicletas() throws RepositorioException;
	
	String altaEstacion(String nombre, int capacidad, String direccion, double latitud, double longitud)
			throws RepositorioException;

	String altaBicicleta(String modelo, String idEstacion) throws RepositorioException;

	void estacionarBicicleta(String idBicicleta, String idEstacion) throws RepositorioException;

	void estacionarBicicleta(String idBicicleta) throws RepositorioException;

	void retirarBicicleta(String idBicicleta) throws RepositorioException;
	
	boolean isCompleta(String idEstacion) throws RepositorioException;
	
	int bicicletasEnEstacion(String idEstacion) throws RepositorioException;
	
	Page<EstacionDTO> getListadoPaginadoEstaciones(Pageable pageable) throws Exception;
	
	Page<BicicletaDTO> getListadoPaginadoBicicletas(Pageable pageable, String id) throws Exception;
	
	Page<BicicletaDTO> getListadoPaginadoBicicletas2(Pageable pageable, String id) throws Exception;
}
