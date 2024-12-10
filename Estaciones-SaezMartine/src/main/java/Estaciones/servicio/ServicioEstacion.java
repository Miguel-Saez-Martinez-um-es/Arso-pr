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
	public String addEstacion(Estacion estacion) throws RepositorioException {
		return repositorioEstacion.save(estacion).getId();
	}

	@Override
	public void updateEstacion(Estacion estacion) throws RepositorioException {
		repositorioEstacion.save(estacion);
	}

	@Override
	public Estacion getEstacion(String id) throws RepositorioException {
		Optional<Estacion> estacion = repositorioEstacion.findById(id);
		if (!estacion.isPresent())
			throw new EntityNotFoundException("no existe la estacion");
		else
			return estacion.get();
	}

	@Override
	public void deleteEstacion(String id) throws RepositorioException {
		Optional<Estacion> e = repositorioEstacion.findById(id);
		if (!e.isPresent())
			throw new EntityNotFoundException("no existe la estacion");
		else
			repositorioEstacion.delete(e.get());
	}

	@Override
	public List<Estacion> getEstaciones() throws RepositorioException {
		LinkedList<Estacion> estaciones = new LinkedList<Estacion>();
		for (Estacion e : repositorioEstacion.findAll()) {
			estaciones.add(e);
		}
		return estaciones;
	}

	@Override
	public String addBicicleta(Bicicleta bicicleta) throws RepositorioException {
		return repositorioBici.save(bicicleta).getCodigo();
	}

	@Override
	public void updateBicicleta(Bicicleta bicicleta) throws RepositorioException {
		repositorioBici.save(bicicleta);
	}

	@Override
	public Bicicleta getBicicleta(String idBicicleta) throws RepositorioException {
		Optional<Bicicleta> bici = repositorioBici.findById(idBicicleta);
		if (!bici.isPresent())
			throw new EntityNotFoundException("no existe la bicicleta");
		else
			return bici.get();

	}

	@Override
	public void deleteBicicleta(String idBicicleta) throws RepositorioException {
		Optional<Bicicleta> bici = repositorioBici.findById(idBicicleta);
		if (!bici.isPresent())
			throw new EntityNotFoundException("no existe la bicicleta");
		else
			repositorioBici.delete(bici.get());
	}

	@Override
	public List<Bicicleta> getBicicletas() throws RepositorioException {
		LinkedList<Bicicleta> bicicletas = new LinkedList<Bicicleta>();
		for (Bicicleta b : repositorioBici.findAll()) {
			bicicletas.add(b);
		}
		return bicicletas;
	}

	@Override
	public String altaEstacion(String nombre, int capacidad, String direccion, double latitud, double longitud)
			throws RepositorioException {
		Estacion e = new Estacion();
		e.setNombre(nombre);
		e.setCapacidad(capacidad);
		e.setDireccion(direccion);
		e.setFechaAlta(LocalDate.now());
		e.setLatitud(latitud);
		e.setLongitud(longitud);
		return repositorioEstacion.save(e).getId();
	}

	@Override
	public String altaBicicleta(String modelo, String idEstacion) throws RepositorioException {
		Bicicleta b = new Bicicleta();
		b.setCodigo(UUID.randomUUID().toString());
		b.setModelo(modelo);
		b.setFechaAlta(LocalDate.now());
		b.setEstacion(idEstacion);
		return addBicicleta(b);
	}

	@Override
	public void estacionarBicicleta(String idBicicleta, String idEstacion) throws RepositorioException {
		Bicicleta b = getBicicleta(idBicicleta);
		b.setEstacion(idEstacion);
		updateBicicleta(b);
	}

	@Override
	public void estacionarBicicleta(String idBicicleta) throws RepositorioException {
		String idEstacion = null;
		int i = 0;
		LinkedList<Estacion> estaciones = (LinkedList<Estacion>) getEstaciones();
		while (idEstacion == null) {
			if (isCompleta(estaciones.get(i).getId())) {
				idEstacion = estaciones.get(i).getId();
			}
			i++;
		}
		estacionarBicicleta(idBicicleta, idEstacion);
	}

	@Override
	public void retirarBicicleta(String idBicicleta) throws RepositorioException {
		Bicicleta b = getBicicleta(idBicicleta);
		Optional<Estacion> e = repositorioEstacion.findById(b.getEstacion());
		if (!e.isPresent()) {
			throw new EntityNotFoundException("no existe la estacion");
		} else {
			b.setEstacion(null);
		}
	}

	@Override
	public boolean isCompleta(String idEstacion) throws RepositorioException {
		if (bicicletasEnEstacion(idEstacion) < getEstacion(idEstacion).getCapacidad())
			return true;
		else
			return false;
	}

	public int bicicletasEnEstacion(String idEstacion) throws RepositorioException {
		Estacion e = getEstacion(idEstacion);
		int i = 0;
		for (Bicicleta b : getBicicletas()) {
			if (b.getEstacion() != null) {
				if (b.getEstacion().equals(e.getId())) {
					// System.out.println("bici: "+b.getCodigo() +" en estacion: "+e.getId());
					i++;
				}
			}

		}
		return i;
	}

	@Override
	public Page<EstacionDTO> getListadoPaginadoEstaciones(Pageable pageable) throws Exception {
		return this.repositorioEstacion.findAll(pageable).map((estacion) -> {
			try {
				return new EstacionDTO(estacion.getId(), estacion.getNombre(), estacion.getDireccion(),
						estacion.getCapacidad(), estacion.getCapacidad()-bicicletasEnEstacion(estacion.getId()), estacion.getLatitud(),
						estacion.getLongitud());
			} catch (RepositorioException e) {
				e.printStackTrace();
			}
			return null;
			
		});
	}

	@Override
	public Page<BicicletaDTO> getListadoPaginadoBicicletas(Pageable pageable, String id) throws Exception {
		return this.repositorioBici.findByEstacion(id, pageable).map((bicicleta) -> {
			return new BicicletaDTO(bicicleta.getCodigo(), bicicleta.getModelo(), bicicleta.getEstacion());
		});
	}
	
	@Override
	//TODO cambiar para que solo muestre las que no esten en estado disponible o similar
	public Page<BicicletaDTO> getListadoPaginadoBicicletas2(Pageable pageable, String id) throws Exception {
		return this.repositorioBici.findByEstacion(id, pageable).map((bicicleta) -> {
			return new BicicletaDTO(bicicleta.getCodigo(), bicicleta.getModelo(), bicicleta.getEstacion());
		});
	}




}
