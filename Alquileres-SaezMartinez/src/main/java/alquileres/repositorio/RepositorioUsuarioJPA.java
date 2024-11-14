package alquileres.repositorio;

import java.time.LocalDateTime;

import alquileres.persistencia.jpa.AlquilerEntidad;
import alquileres.persistencia.jpa.ReservaEntidad;
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

	public RepositorioUsuarioJPA() {
		
		try {
			
			String idUsuario = "Usuario Pruebas";
			String idUsuario2 = "Usuario Pruebas 2";
			String idBicicleta = "Bicicleta Pruebas";
			String idBicicleta2 = "Bicicleta Pruebas 2";
			
			for(UsuarioEntidad u : this.getAll()) {
				delete(u);
			}
			
			UsuarioEntidad u = new UsuarioEntidad(idUsuario);
			UsuarioEntidad u2 = new UsuarioEntidad(idUsuario2);
			
			
			AlquilerEntidad a = new AlquilerEntidad();
			a.setIdBicicleta(idBicicleta);
			a.setInicio(LocalDateTime.now());
			
			ReservaEntidad r = new ReservaEntidad();
			r.setCreada(LocalDateTime.now());
			r.setIdBicicleta(idBicicleta2);
			
			u.addAlquiler(a);
			u.addReserva(r);
			
			add(u);
			add(u2);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
