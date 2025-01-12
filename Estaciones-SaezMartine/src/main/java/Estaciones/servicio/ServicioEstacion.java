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
		cargarDatosPrueba();
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

	public void cargarDatosPrueba() {
		try {
			for (Bicicleta b : getBicicletas()) {
				deleteBicicleta(b.getModelo());
			}
			for (Estacion e : getEstaciones()) {
				deleteEstacion(e.getNombre());
			}

			// Estaciones

			Estacion estacion = new Estacion();
			estacion.setNombre("Estacion1");
			estacion.setCapacidad(1);
			estacion.setFechaAlta(LocalDate.now());
			estacion.setDireccion("Calle San Basilio");
			estacion.setLatitud(37.99633205165018);
			estacion.setLongitud(-1.1449311025769682);

			String idEstacion = addEstacion(estacion);
			
			String idEstacion2 = altaEstacion("Estacion2", 15, "Calle Mayor", 37.564824972468024,
					-1.2588498598537197);

			String idEstacion3 = altaEstacion("Estacion3", 5, "Avenida Tierno Galvan", 37.560160502584075,
					-1.2699767938007211);
			altaEstacion("Estacion4", 10, "Avenida Doctor Meca", 37.572165662442714, -1.266359944888294);

			// Bicicletas

			Bicicleta bici = new Bicicleta();
			bici.setCodigo(UUID.randomUUID().toString());
			bici.setFechaAlta(LocalDate.now());
			bici.setModelo("Modelo1");
			bici.setEstacion(idEstacion);

			Bicicleta bici2 = new Bicicleta();
			bici2.setCodigo(UUID.randomUUID().toString());
			bici2.setFechaAlta(LocalDate.now());
			bici2.setModelo("Modelo2");
			bici2.setEstacion(idEstacion2);

			addBicicleta(bici);
			addBicicleta(bici2);
			altaBicicleta("Modelo3", idEstacion3);
			altaBicicleta("Modelo4", idEstacion3);
			
			
			System.out.println();
			
			System.out.println("Bicicletas:");
			for (Bicicleta b : getBicicletas()) {
				System.out.println(b.getModelo() + " en la estacion: " + b.getEstacion());
			}
			
			System.out.println();
			
			System.out.println("Estaciones:");
			for (Estacion e : getEstaciones()) {
				int huecos = e.getCapacidad() - bicicletasEnEstacion(e.getNombre());
				System.out.println(
						e.getNombre() + " con id: " + e.getId() + " con " + bicicletasEnEstacion(e.getNombre()) + "/"
								+ e.getCapacidad() + " bicicletas. Huecos: " + huecos);
			}
		} catch (

		Exception e) {
			System.err.println("Error al cargar los datos de prueba: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
