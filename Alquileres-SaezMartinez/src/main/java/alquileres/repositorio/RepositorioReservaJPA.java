package alquileres.repositorio;

import alquileres.persistencia.jpa.ReservaEntidad;
import repositorio.RepositorioJPA;

public class RepositorioReservaJPA extends RepositorioJPA<ReservaEntidad>{

	@Override
	public Class<ReservaEntidad> getClase() {
		return ReservaEntidad.class;
	}

	@Override
	public String getNombre() {
		return ReservaEntidad.class.getName().substring(ReservaEntidad.class.getName().lastIndexOf(".") + 1);
	}

}
