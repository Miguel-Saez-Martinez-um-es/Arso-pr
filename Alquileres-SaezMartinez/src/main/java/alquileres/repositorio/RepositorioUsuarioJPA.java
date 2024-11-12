package alquileres.repositorio;

import alquileres.persistencia.jpa.UsuarioEntidad;
import repositorio.RepositorioJPA;

public class RepositorioUsuarioJPA extends RepositorioJPA<UsuarioEntidad>{

	@Override
	public Class<UsuarioEntidad> getClase() {
		return UsuarioEntidad.class;
	}

	@Override
	public String getNombre() {
		return UsuarioEntidad.class.getName().substring(UsuarioEntidad.class.getName().lastIndexOf(".") + 1);
	}

}
