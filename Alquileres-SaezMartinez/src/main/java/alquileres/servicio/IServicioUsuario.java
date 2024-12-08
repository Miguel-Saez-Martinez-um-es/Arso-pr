package alquileres.servicio;

import java.util.List;

import alquileres.modelo.Usuario;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;

public interface IServicioUsuario {

	    void actualizar(Usuario usuario) throws RepositorioException, EntidadNoEncontrada;
	    
		void borrar(String id)  throws RepositorioException, EntidadNoEncontrada;

		Usuario recuperar(String id)  throws RepositorioException, EntidadNoEncontrada;
		
		List<Usuario> recuperarUsuarios() throws RepositorioException, EntidadNoEncontrada;

		Usuario autenticar(String username, String password) throws RepositorioException, EntidadNoEncontrada;
}
