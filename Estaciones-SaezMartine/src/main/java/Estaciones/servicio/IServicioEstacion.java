package Estaciones.servicio;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import Estaciones.DTO.BicicletaDTO;
import Estaciones.DTO.EstacionDTO;
import Estaciones.modelo.Bicicleta;
import Estaciones.modelo.Estacion;

public interface IServicioEstacion {

	String addEstacion(Estacion estacion);

	void deleteEstacion(String id);

	Estacion getEstacion(String id);

	List<Estacion> getEstaciones();

	String addBicicleta(Bicicleta bicicleta);

	void updateBicicleta(Bicicleta bicicleta);

	Bicicleta getBicicleta(String idBicicleta);

	void deleteBicicleta(String idBicicleta);

	List<Bicicleta> getBicicletas();

	String altaEstacion(String nombre, int capacidad, String direccion, double latitud, double longitud);

	String altaBicicleta(String modelo, String idEstacion);

	void estacionarBicicleta(String idBicicleta, String idEstacion);

	void retirarBicicleta(String idBicicleta);

	boolean isCompleta(String idEstacion);

	int bicicletasEnEstacion(String idEstacion);

	Page<EstacionDTO> getListadoPaginadoEstaciones(Pageable pageable);

	Page<BicicletaDTO> getListadoPaginadoBicicletas(Pageable pageable, String id);

	Page<BicicletaDTO> getListadoPaginadoBicicletas2(Pageable pageable, String id);
}
