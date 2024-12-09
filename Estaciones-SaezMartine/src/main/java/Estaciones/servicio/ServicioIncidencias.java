package Estaciones.servicio;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Estaciones.modelo.Estado;
import Estaciones.modelo.Incidencia;
import Estaciones.repositorio.RepositorioIncidencia;
import repositorio.RepositorioException;

@Service
@Transactional
public class ServicioIncidencias implements IServicioIncidencias {

	private RepositorioIncidencia repositorio;
	private IServicioEstacion servicio;

	@Autowired
	public ServicioIncidencias(RepositorioIncidencia repositorio, IServicioEstacion servicio) {
		this.repositorio = repositorio;
		this.servicio = servicio;
	}

	@Override
	public String crear(String bicicleta, String descripcion) throws RepositorioException {
		Incidencia i = new Incidencia();
		i.setBicicleta(bicicleta);
		i.setDescripcion(descripcion);
		i.setFechaCreacion(LocalDate.now());
		i.setEstado(Estado.PENDIENTE);
		return repositorio.save(i).getId();
	}

	@Override
	public void actualizar(Incidencia incidencia) throws RepositorioException {
		repositorio.save(incidencia);
	}

	@Override
	public void borrar(String id) throws RepositorioException {
		Incidencia i = repositorio.findById(id).get();
		repositorio.delete(i);
	}

	@Override
	public Incidencia recuperar(String id) throws RepositorioException {
		Optional<Incidencia> incidencia = repositorio.findById(id);
		if (!incidencia.isPresent())
			throw new EntityNotFoundException("no existe la incidencia");
		else
			return incidencia.get();
	}

	public void cancelarIncidencia(String id, String motivo) throws RepositorioException {
		Optional<Incidencia> i = repositorio.findById(id);
		if (!i.isPresent())
			throw new EntityNotFoundException("no existe la incidencia");
		else {

			i.get().setEstado(Estado.CANCELADA);
			i.get().setMotivo(motivo);
			i.get().setFechaCierre(LocalDate.now());
			repositorio.save(i.get());
		}
	}

	public void asignarIncidencia(String id, String operario) throws RepositorioException {
		Optional<Incidencia> i = repositorio.findById(id);
		if (!i.isPresent())
			throw new EntityNotFoundException("no existe la incidencia");
		else {
			i.get().setEstado(Estado.ASIGNADA);
			i.get().setOperario(operario);
			servicio.retirarBicicleta(i.get().getBicicleta());
			repositorio.save(i.get());
		}
	}

	public void resolverIncidencia(String id, String motivo, boolean reparada)
			throws RepositorioException {
		Optional<Incidencia> i = repositorio.findById(id);
		if (!i.isPresent())
			throw new EntityNotFoundException("no existe la incidencia");
		else {
			i.get().setEstado(Estado.RESUELTA);
			i.get().setMotivo(motivo);
			i.get().setFechaCierre(LocalDate.now());
			if (reparada) {
				servicio.estacionarBicicleta(i.get().getBicicleta());
			} else {
				servicio.borrarBicicleta(i.get().getBicicleta());
			}
			repositorio.save(i.get());
		}
	}

	@Override
	public List<Incidencia> recuperarIncidenciasAbiertas() throws RepositorioException {
		LinkedList<Incidencia> incidencias = new LinkedList<Incidencia>();
		for (Incidencia i : repositorio.findAll()) {
			if (!i.getEstado().equals(Estado.RESUELTA) && !i.getEstado().equals(Estado.CANCELADA)) {
				incidencias.add(i);
			}
		}
		return incidencias;
	}

}
