package alquileres.servicio;

import java.util.LinkedList;
import java.util.List;

import alquileres.modelo.Alquiler;
import alquileres.modelo.Reserva;
import alquileres.modelo.Usuario;
import alquileres.persistencia.jpa.AlquilerEntidad;
import alquileres.persistencia.jpa.ReservaEntidad;
import alquileres.persistencia.jpa.UsuarioEntidad;
import alquileres.repositorio.RepositorioUsuarioJPA;
import alquileres.repositorio.RepositorioUsuarioMemoria;
import repositorio.EntidadNoEncontrada;
import repositorio.FactoriaRepositorios;
import repositorio.Repositorio;
import repositorio.RepositorioException;

public class ServicioUsuario implements IServicioUsuario {

	private RepositorioUsuarioJPA repositorio = FactoriaRepositorios.getRepositorio(UsuarioEntidad.class);
	//private Repositorio<UsuarioEntidad, String> repositorio = FactoriaRepositorios.getRepositorio(UsuarioEntidad.class);

	@Override
	public void actualizar(Usuario usuario) throws RepositorioException, EntidadNoEncontrada {
		repositorio.update(usuarioAUsuarioEntidad(usuario));
	}

	@Override
	public void borrar(String id) throws RepositorioException, EntidadNoEncontrada {
		for (String s : repositorio.getIds()) {
			if (s.equals(id)) {
				UsuarioEntidad usuario = repositorio.getById(id);

				
				repositorio.delete(usuario);
			}
		}
	}
	
	

	@Override
	public Usuario recuperar(String id) throws RepositorioException, EntidadNoEncontrada {
	    if (id == null) {
	        throw new IllegalArgumentException("El ID de usuario no puede ser null");
	    }
		for (String s : repositorio.getIds()) {
			if (s.equals(id)) {
				return usuarioEntidadAUsuario(repositorio.getById(id));
			}
		}

		UsuarioEntidad usuario = new UsuarioEntidad(id);
		repositorio.add(usuario);
		return usuarioEntidadAUsuario(usuario);

	}

	@Override
	public List<Usuario> recuperarUsuarios() throws RepositorioException, EntidadNoEncontrada {
		LinkedList<Usuario> usuarios = new LinkedList<Usuario>();
		for (UsuarioEntidad u : repositorio.getAll()) {
			usuarios.add(usuarioEntidadAUsuario(u));
		}
		return usuarios;
	}

	public Usuario usuarioEntidadAUsuario(UsuarioEntidad e) {
		Usuario u = new Usuario();
		u.setId(e.getId());
		if (!e.getAlquileres().isEmpty())
			for (AlquilerEntidad a : e.getAlquileres())
				u.addAlquiler(alquilerEntidadAAlquiler(a));
		if (!e.getReservas().isEmpty())
			for (ReservaEntidad r : e.getReservas())
				u.addReserva(reservaEntidadAReserva(r));

		return u;
	}

	public Alquiler alquilerEntidadAAlquiler(AlquilerEntidad e) {
		Alquiler a = new Alquiler();
		a.setIdBicicleta(e.getIdBicicleta());
		if (e.getInicio() != null)
			a.setInicio(e.getInicio());
		if (e.getFin() != null)
			a.setFin(e.getFin());
		return a;
	}

	public Reserva reservaEntidadAReserva(ReservaEntidad e) {
		Reserva r = new Reserva();
		r.setIdBicicleta(e.getIdBicicleta());
		if (e.getCreada() != null)
			r.setCreada(e.getCreada());
		if (e.getCaducidad() != null)
			r.setCaducidad(e.getCaducidad());
		return r;
	}

	public UsuarioEntidad usuarioAUsuarioEntidad(Usuario e) {
		UsuarioEntidad u = new UsuarioEntidad();
		u.setId(e.getId());
		if (!e.getAlquileres().isEmpty())
			for (Alquiler a : e.getAlquileres())
				u.addAlquiler((alquilerAAlquilerEntidad(a, e.getId())));

		if (!e.getReservas().isEmpty())
			for (Reserva r : e.getReservas())
				u.addReserva(reservaAReservaEntidad(r, e.getId()));

		return u;
	}

	public AlquilerEntidad alquilerAAlquilerEntidad(Alquiler e, String id) {
		AlquilerEntidad a = new AlquilerEntidad();
		a.setIdBicicleta(e.getIdBicicleta());
	//	a.setIdUsuario(id);
		if (e.getInicio() != null)
			a.setInicio(e.getInicio());
		if (e.getFin() != null)
			a.setFin(e.getFin());
		return a;
	}

	public ReservaEntidad reservaAReservaEntidad(Reserva e, String id) {
		ReservaEntidad r = new ReservaEntidad();
		r.setIdBicicleta(e.getIdBicicleta());
	//	r.setUsuario(id);
		if (e.getCreada() != null)
			r.setCreada(e.getCreada());
		if (e.getCaducidad() != null)
			r.setCaducidad(e.getCaducidad());
		return r;
	}
}
