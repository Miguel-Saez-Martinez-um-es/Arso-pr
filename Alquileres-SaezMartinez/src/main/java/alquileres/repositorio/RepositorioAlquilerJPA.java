package alquileres.repositorio;

import alquileres.modelo.Usuario;
import alquileres.persistencia.jpa.AlquilerEntidad;
import alquileres.servicio.IServicioAlquileres;
import alquileres.servicio.IServicioUsuario;
import repositorio.RepositorioJPA;
import servicio.FactoriaServicios;

public class RepositorioAlquilerJPA extends RepositorioJPA<AlquilerEntidad> {

	@Override
	public Class<AlquilerEntidad> getClase() {
		return AlquilerEntidad.class;
	}

	@Override
	public String getNombre() {
		return AlquilerEntidad.class.getName().substring(AlquilerEntidad.class.getName().lastIndexOf(".") + 1);
	}
}
