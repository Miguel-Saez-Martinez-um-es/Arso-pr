package Estaciones.servicio;

import java.time.LocalDate; 
import java.util.LinkedList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Estaciones.modelo.Estado;
import Estaciones.modelo.Incidencia;
import Estaciones.repositorio.RepositorioIncidencia;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;


@Service
@Transactional
public class ServicioIncidencias implements IServicioIncidencias {

	private RepositorioIncidencia repositorio;
	private IServicioEstacion servicio;

	@Autowired
	public ServicioIncidencias(RepositorioIncidencia repositorio, IServicioEstacion servicio) {
		this.repositorio=repositorio;
		this.servicio=servicio;
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
	public void actualizar(Incidencia incidencia) throws RepositorioException, EntidadNoEncontrada {
		repositorio.save(incidencia);
	}

	@Override
	public void borrar(String id) throws RepositorioException, EntidadNoEncontrada {
		Incidencia i = repositorio.findById(id).get();
		repositorio.delete(i);
	}

	@Override
	public Incidencia recuperar(String id) throws RepositorioException, EntidadNoEncontrada {
		return repositorio.findById(id).get();
	}

	public void cancelarIncidencia(String id, String motivo) throws RepositorioException, EntidadNoEncontrada{
		Incidencia i = repositorio.findById(id).get();
		i.setEstado(Estado.CANCELADA);
		i.setMotivo(motivo);
		i.setFechaCierre(LocalDate.now());
		repositorio.save(i);
	}
	
	public void asignarIncidencia(String id, String operario) throws RepositorioException, EntidadNoEncontrada{
		Incidencia i = repositorio.findById(id).get();
		i.setEstado(Estado.ASIGNADA);
		i.setOperario(operario);
		servicio.retirarBicicleta(i.getBicicleta());
		repositorio.save(i);
	}

	public void resolverIncidencia(String id, String motivo, boolean reparada)
			throws RepositorioException, EntidadNoEncontrada{
		Incidencia i = repositorio.findById(id).get();
		i.setEstado(Estado.RESUELTA);
		i.setMotivo(motivo);
		i.setFechaCierre(LocalDate.now());
		if(reparada) {
			servicio.estacionarBicicleta(i.getBicicleta());
		}else {
			servicio.borrarBicicleta(i.getBicicleta());
		}
		repositorio.save(i);
	}

	@Override
	public List<Incidencia> recuperarIncidenciasAbiertas() throws RepositorioException, EntidadNoEncontrada {
		LinkedList<Incidencia> incidencias = new LinkedList<Incidencia>();
		for (Incidencia i : repositorio.findAll()) {
			if (!i.getEstado().equals(Estado.RESUELTA) && !i.getEstado().equals(Estado.CANCELADA)) {
				incidencias.add(i);
			}
		}
		return incidencias;
	}

}
