package alquileres.repositorio;

import java.time.LocalDateTime;

import alquileres.modelo.Roles;
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
		
		//Casos de prueba
		try {
			
			//Borramos lo anterior
			for(UsuarioEntidad u : this.getAll()) {
				delete(u);
			}
			
			
			String idUsuario = "Usuario";
			String idUsuario2 = "Usuario2";
			String password = "clave";
			String password2 = "clave2";
			String idBicicleta = "Bicicleta";
			String idBicicleta2 = "Bicicleta2";

			UsuarioEntidad u = new UsuarioEntidad(idUsuario, Roles.GESTOR, password);
			UsuarioEntidad u2 = new UsuarioEntidad(idUsuario2, Roles.USUARIO, password2);

			
			AlquilerEntidad a = new AlquilerEntidad();
			a.setIdBicicleta(idBicicleta);
			a.setInicio(LocalDateTime.now().minusMinutes(35));
			a.setFin(LocalDateTime.now().minusMinutes(5));
			
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
