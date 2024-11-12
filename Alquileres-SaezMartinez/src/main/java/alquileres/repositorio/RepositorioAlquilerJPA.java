package alquileres.repositorio;

import alquileres.persistencia.jpa.AlquilerEntidad;
import repositorio.RepositorioJPA;

public class RepositorioAlquilerJPA extends RepositorioJPA<AlquilerEntidad>{

	@Override
	public Class<AlquilerEntidad> getClase() {
		return AlquilerEntidad.class;
	}

	@Override
	public String getNombre() {
		return AlquilerEntidad.class.getName().substring(AlquilerEntidad.class.getName().lastIndexOf(".") + 1);
	}

}
