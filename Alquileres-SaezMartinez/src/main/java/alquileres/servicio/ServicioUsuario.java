package alquileres.servicio;

import java.util.LinkedList;
import java.util.List;

import alquileres.modelo.Usuario;
import alquileres.repositorio.RepositorioUsuarioMemoria;
import repositorio.EntidadNoEncontrada;
import repositorio.FactoriaRepositorios;
import repositorio.RepositorioException;

public class ServicioUsuario implements IServicioUsuario {

	private RepositorioUsuarioMemoria repositorio = FactoriaRepositorios.getRepositorio(Usuario.class);

	@Override
	public void actualizar(Usuario usuario) throws RepositorioException, EntidadNoEncontrada {
		repositorio.update(usuario);
	}

	@Override
	public void borrar(String id) throws RepositorioException, EntidadNoEncontrada {
		for (String s : repositorio.getIds()) {
			if (s.equals(id)) {
				Usuario usuario = repositorio.getById(id);
				
				repositorio.delete(usuario);
			}
		}
		
		
	}

	@Override
	public Usuario recuperar(String id) throws RepositorioException, EntidadNoEncontrada {
		for (String s : repositorio.getIds()) {
			if (s.equals(id)) {
				return repositorio.getById(id);
			}
		}

		Usuario usuario = new Usuario(id);
		repositorio.add(usuario);
		return usuario;

	}

	@Override
	public List<Usuario> recuperarUsuarios() throws RepositorioException, EntidadNoEncontrada {
		LinkedList<Usuario> usuarios = new LinkedList<Usuario>();
		for (Usuario u : repositorio.getAll()) {
			usuarios.add(u);
		}
		return usuarios;
	}
}
