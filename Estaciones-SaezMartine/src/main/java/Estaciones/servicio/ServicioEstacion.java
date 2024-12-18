package Estaciones.servicio;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Estaciones.DTO.BicicletaDTO;
import Estaciones.DTO.EstacionDTO;
import Estaciones.modelo.Bicicleta;
import Estaciones.modelo.Estacion;
import Estaciones.repositorio.RepositorioBicis;
import Estaciones.repositorio.RepositorioEstacion;
import repositorio.RepositorioException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@Transactional
public class ServicioEstacion implements IServicioEstacion {

	RepositorioEstacion repositorioEstacion;
	RepositorioBicis repositorioBici;

	@Autowired
	public ServicioEstacion(RepositorioEstacion repositorioEstacion, RepositorioBicis repositorioBici) {
		this.repositorioEstacion = repositorioEstacion;
		this.repositorioBici = repositorioBici;

	}

	@Override
	public String addEstacion(Estacion estacion) {
		return repositorioEstacion.save(estacion).getId();
	}

	@Override
	public Estacion getEstacion(String id) {
		Optional<Estacion> estacion = repositorioEstacion.findById(id);
		if (!estacion.isPresent())
			throw new EntityNotFoundException("no existe la estacion");
		else
			return estacion.get();
	}

	@Override
	public void deleteEstacion(String id) {
		Optional<Estacion> e = repositorioEstacion.findById(id);
		if (!e.isPresent())
			throw new EntityNotFoundException("no existe la estacion");
		else
			repositorioEstacion.delete(e.get());
	}

	@Override
	public List<Estacion> getEstaciones() {
		LinkedList<Estacion> estaciones = new LinkedList<Estacion>();
		for (Estacion e : repositorioEstacion.findAll()) {
			estaciones.add(e);
		}
		return estaciones;
	}

	@Override
	public String addBicicleta(Bicicleta bicicleta) {
		return repositorioBici.save(bicicleta).getCodigo();
	}

	@Override
	public void updateBicicleta(Bicicleta bicicleta) {
		repositorioBici.save(bicicleta);
	}

	@Override
	public Bicicleta getBicicleta(String idBicicleta) {
		Optional<Bicicleta> bici = repositorioBici.findById(idBicicleta);
		if (!bici.isPresent())
			throw new EntityNotFoundException("no existe la bicicleta");
		else
			return bici.get();

	}

	@Override
	public void deleteBicicleta(String idBicicleta) {
		Optional<Bicicleta> bici = repositorioBici.findById(idBicicleta);
		if (!bici.isPresent())
			throw new EntityNotFoundException("no existe la bicicleta");
		else
			repositorioBici.delete(bici.get());
	}

	@Override
	public List<Bicicleta> getBicicletas() {
		LinkedList<Bicicleta> bicicletas = new LinkedList<Bicicleta>();
		for (Bicicleta b : repositorioBici.findAll()) {
			bicicletas.add(b);
		}
		return bicicletas;
	}

	@Override
	public String altaEstacion(String nombre, int capacidad, String direccion, double latitud, double longitud) {
		Estacion e = new Estacion();
		if (nombre == null || nombre.isEmpty() || direccion == null || direccion.isEmpty() || capacidad < 1) {
			throw new IllegalArgumentException("Los datos para el alta de la estacion no son validos.");
		}
		e.setNombre(nombre);
		e.setCapacidad(capacidad);
		e.setDireccion(direccion);
		e.setFechaAlta(LocalDate.now());
		e.setLatitud(latitud);
		e.setLongitud(longitud);
		return addEstacion(e);
	}

	@Override
	public String altaBicicleta(String modelo, String idEstacion) {
		if (isCompleta(idEstacion)) {
			throw new IllegalArgumentException("No se puede dar de alta la bicicleta, la estación esta completa.");
		} else {
			Bicicleta b = new Bicicleta();
			b.setCodigo(UUID.randomUUID().toString());
			b.setModelo(modelo);
			b.setFechaAlta(LocalDate.now());
			b.setEstacion(idEstacion);
			return addBicicleta(b);
		}
	}

	@Override
	public void estacionarBicicleta(String idBicicleta, String idEstacion){
		if (isCompleta(idEstacion)) {
			throw new IllegalArgumentException("No se puede estacionar la bicicleta, la estación esta completa.");
		} else {
			Bicicleta b = getBicicleta(idBicicleta);
			b.setEstacion(idEstacion);
			updateBicicleta(b);
		}
	}

	@Override
	public void retirarBicicleta(String idBicicleta) {
		Bicicleta b = getBicicleta(idBicicleta);
		Optional<Estacion> e = repositorioEstacion.findById(b.getEstacion());
		if (!e.isPresent()) {
			throw new EntityNotFoundException("no existe la estacion");
		} else {
			b.setEstacion(null);
		}
	}

	@Override
	public boolean isCompleta(String idEstacion) {
		if (bicicletasEnEstacion(idEstacion) < getEstacion(idEstacion).getCapacidad())
			return true;
		else
			return false;
	}

	public int bicicletasEnEstacion(String idEstacion) {
		Estacion e = getEstacion(idEstacion);
		int i = 0;
		for (Bicicleta b : getBicicletas()) {
			if (b.getEstacion() != null) {
				if (b.getEstacion().equals(e.getId())) {
					i++;
				}
			}

		}
		return i;
	}

	@Override
	public Page<EstacionDTO> getListadoPaginadoEstaciones(Pageable pageable) {
		return this.repositorioEstacion.findAll(pageable).map((estacion) -> {
			return new EstacionDTO(estacion.getId(), estacion.getNombre(), estacion.getDireccion(),
					estacion.getCapacidad(), estacion.getCapacidad() - bicicletasEnEstacion(estacion.getId()),
					estacion.getLatitud(), estacion.getLongitud());
		});
	}

	@Override
	// TODO cambiar para que muestre un enlace a una nueva operacion bajabicicleta
	public Page<BicicletaDTO> getListadoPaginadoBicicletas(Pageable pageable, String id){
		return this.repositorioBici.findByEstacion(id, pageable).map((bicicleta) -> {
			return new BicicletaDTO(bicicleta.getCodigo(), bicicleta.getModelo(), bicicleta.getEstacion());
		});
	}

	@Override
	public Page<BicicletaDTO> getListadoPaginadoBicicletas2(Pageable pageable, String id) {
		return this.repositorioBici.findByEstacion(id, pageable).map((bicicleta) -> {
			return new BicicletaDTO(bicicleta.getCodigo(), bicicleta.getModelo(), bicicleta.getEstacion());
		});
	}

}
