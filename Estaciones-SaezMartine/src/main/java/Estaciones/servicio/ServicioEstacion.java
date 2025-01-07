package Estaciones.servicio;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Estaciones.DTO.BicicletaDTO;
import Estaciones.DTO.EstacionDTO;
import Estaciones.modelo.Bicicleta;
import Estaciones.modelo.Estacion;
import Estaciones.rabbitmq.PublicadorEventos;
import Estaciones.repositorio.RepositorioBicis;
import Estaciones.repositorio.RepositorioEstacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@Transactional
public class ServicioEstacion implements IServicioEstacion {

	private RepositorioEstacion repositorioEstacion;
	private RepositorioBicis repositorioBici;
	private PublicadorEventos publicadorEventos;

	@Autowired
	public ServicioEstacion(RepositorioEstacion repositorioEstacion, RepositorioBicis repositorioBici,
			PublicadorEventos publicadorEventos) {
		this.repositorioEstacion = repositorioEstacion;
		this.repositorioBici = repositorioBici;
		this.publicadorEventos = publicadorEventos;

	}

	@Override
	public String addEstacion(Estacion estacion) {
		return repositorioEstacion.save(estacion).getNombre();
	}

	@Override
	public Estacion getEstacion(String nombre) {
		Estacion estacion = repositorioEstacion.findByNombre(nombre);
		return estacion;
	}

	@Override
	public void deleteEstacion(String nombre) {
		Estacion e = repositorioEstacion.findByNombre(nombre);

		repositorioEstacion.delete(e);
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
		return repositorioBici.save(bicicleta).getModelo();
	}

	@Override
	public void updateBicicleta(Bicicleta bicicleta) {
		repositorioBici.save(bicicleta);
	}

	@Override
	public Bicicleta getBicicleta(String modelo) {
		Bicicleta bici = repositorioBici.findByModelo(modelo);
		return bici;

	}

	@Override
	public void deleteBicicleta(String modelo) {
		Bicicleta bici = repositorioBici.findByModelo(modelo);
		repositorioBici.delete(bici);
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
			return null;
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
	public void bajaBicicleta(String idBicicleta) {
		Bicicleta b = getBicicleta(idBicicleta);
		b.setDisponible(false);
		b.setFechaBaja(LocalDate.now());
		updateBicicleta(b);
		publicadorEventos.publicarEventoBicicletaDesactivada(idBicicleta);
	}

	@Override
	public void estacionarBicicleta(String idBicicleta, String idEstacion) {
		if (isCompleta(idEstacion)) {
			throw new IllegalArgumentException(
					"No se puede estacionar la bicicleta, la estación " + idEstacion + " esta completa.");
		} else {
			Bicicleta b = getBicicleta(idBicicleta);
			b.setEstacion(idEstacion);
			b.setDisponible(true);
			updateBicicleta(b);
		}
	}

	@Override
	public void alquilarBicicleta(String idBicicleta) {
		Bicicleta b = getBicicleta(idBicicleta);
		b.setEstacion(null);
		b.setDisponible(false);
		updateBicicleta(b);
	}

	@Override
	public boolean isCompleta(String idEstacion) {
		if (bicicletasEnEstacion(idEstacion) >= getEstacion(idEstacion).getCapacidad()) {
			return true;
		} else {
			return false;
		}
	}

	public int bicicletasEnEstacion(String idEstacion) {
		return repositorioBici.findByEstacion(idEstacion).size();
	}

	public List<Bicicleta> getBicicletasByEstacion(String idEstacion) {
		return repositorioBici.findByEstacion(idEstacion);
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
	public Page<BicicletaDTO> getListadoPaginadoBicicletas(Pageable pageable, String id) {
		return this.repositorioBici.findByEstacion(id, pageable).map((bicicleta) -> {
			return new BicicletaDTO(bicicleta.getCodigo(), bicicleta.getModelo(), bicicleta.getEstacion());
		});
	}

}
