package Estaciones.servicio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
	public String crear(Estacion estacion) throws RepositorioException {
		return repositorioEstacion.save(estacion).getId();
	}

	@Override
	public void actualizar(Estacion estacion) throws RepositorioException {
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
	public void borrar(String id) throws RepositorioException {
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
	public String crearBicicleta(Bicicleta bicicleta) throws RepositorioException {
		return repositorioBici.save(bicicleta).getCodigo();
	}

	@Override
	public void actualizarBicicleta(Bicicleta bicicleta) throws RepositorioException {
		repositorioBici.save(bicicleta);
	}

	@Override
	public Bicicleta recuperarBicicleta(String idBicicleta) throws RepositorioException {
		Optional<Bicicleta> bici = repositorioBici.findById(idBicicleta);
		if (!bici.isPresent())
			throw new EntityNotFoundException("no existe la bicicleta");
		else
			return bici.get();

	}

	@Override
	public void borrarBicicleta(String idBicicleta) throws RepositorioException {
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

	// Funcion de usuario?
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
		return crearBicicleta(b);
	}

	@Override
	public void estacionarBicicleta(String idBicicleta, String idEstacion) throws RepositorioException {
		Bicicleta b = recuperarBicicleta(idBicicleta);
		b.setEstacion(idEstacion);
		actualizarBicicleta(b);
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
		Bicicleta b = recuperarBicicleta(idBicicleta);
		Optional<Estacion> e = repositorioEstacion.findById(b.getEstacion());
		if (!e.isPresent()) {
			throw new EntityNotFoundException("no existe la estacion");
		} else {
			b.setEstacion(null);
		}
	}

	@Override
	public List<Bicicleta> recuperarBicicletasCercanasPosicion(double latitud, double longitud)
			throws RepositorioException {

		ArrayList<Estacion> estaciones = (ArrayList<Estacion>) getEstaciones().stream()
				.sorted(Comparator.comparingDouble(estacion -> calcularDistancia(latitud, longitud,
						estacion.getLatitud(), estacion.getLongitud())))
				.limit(3).collect(Collectors.toList());

		// System.out.println("Estaciones en bicicletasCercanas: "+estaciones.size());

		LinkedList<Bicicleta> bicis = new LinkedList<Bicicleta>();
		for (Bicicleta bici : getBicicletas()) {
			for (Estacion e : estaciones) {
				// System.out.println("Estacion: " + e.getId());
				if (bici.getEstacion().equals(e.getId())) {
					bicis.add(bici);
				}
			}
		}
		System.out.println("Bicis en bicicletasCercanas: " + bicis.size());
		return bicis;
	}

	@Override
	public double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double R = 6371;
		return R * c;
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
			return new EstacionDTO(estacion.getId(), estacion.getNombre(), estacion.getDireccion(),
					estacion.getCapacidad(),  estacion.getLatitud(),
					estacion.getLongitud());
			
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

	public EstacionDTO toEstacionDTO(Estacion estacion) throws RepositorioException {

		EstacionDTO dto = new EstacionDTO(estacion.getId(), estacion.getNombre(), estacion.getDireccion(),
				estacion.getCapacidad(), estacion.getLatitud(), estacion.getLongitud());
		return dto;
	}

	public BicicletaDTO toBicicletaDTO(Bicicleta bicicleta) {
		
		BicicletaDTO dto = new BicicletaDTO(bicicleta.getCodigo(), bicicleta.getModelo(), bicicleta.getEstacion());
		return dto;
	}

}
