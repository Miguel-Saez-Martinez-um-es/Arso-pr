package alquileres.persistencia.jpa;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import repositorio.Identificable;

@Entity
@Table(name = "Usuario")
public class UsuarioEntidad implements Serializable, Identificable {
	@Id
	private String id;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ReservaEntidad> reservas;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AlquilerEntidad> alquileres;


	public UsuarioEntidad(String id) {
		this.id = id;
		this.reservas = new ArrayList<>();
		this.alquileres = new ArrayList<>();
	}

	public UsuarioEntidad() {
        this.id = UUID.randomUUID().toString();
		this.reservas = new ArrayList<>();
		this.alquileres = new ArrayList<>();
	}

	public int reservasCaducadas() {
		int cont = 0;
		for (ReservaEntidad r : reservas) {
			if (r.isCaducada()) {
				cont++;
			}
		}
		return cont;
	}

	public int tiempoUsoHoy() {
		int uso = 0;
		for (AlquilerEntidad a : alquileres) {
			if (a.getInicio().isAfter(LocalDate.now().atStartOfDay())) {
				uso += a.tiempo();
			}
		}
		return uso;
	}

	public int tiempoUsoSemana() {
		int uso = 0;
		for (AlquilerEntidad a : alquileres) {
			if (a.getInicio().isAfter(LocalDate.now().atStartOfDay().minusDays(7))) {
				uso += a.tiempo();
			}
		}
		return uso;
	}

	public boolean superaTiempo() {
		if (this.tiempoUsoHoy() >= 60 || this.tiempoUsoSemana() >= 180) {
			return true;
		}
		return false;
	}

	public ReservaEntidad reservaActiva() {
		if(!reservas.isEmpty()) {
			ReservaEntidad r = reservas.get(reservas.size() - 1);
			if (r.isActiva()) {
				return r;
			}
		}
		return null;
	}

	public AlquilerEntidad alquilerActivo() {
		if(!reservas.isEmpty()) {
			AlquilerEntidad a = alquileres.get(alquileres.size() - 1);
			if (a.isActivo()) {
				return a;
			}
		}
		return null;
	}

	public boolean bloqueado() {
		return reservasCaducadas() >= 3;
	}
	
	// Getters y setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<ReservaEntidad> getReservas() {
		return reservas;
	}

	public void setReservas(List<ReservaEntidad> reservas) {
		this.reservas = reservas;
	}

	public List<AlquilerEntidad> getAlquileres() {
		return alquileres;
	}

	public void setAlquileres(List<AlquilerEntidad> alquileres) {
		this.alquileres = alquileres;
	}
	
	public void addReserva(ReservaEntidad reserva) {
		this.reservas.add(reserva);
	}

	public void addAlquiler(AlquilerEntidad alquiler) {
		this.alquileres.add(alquiler);
	}
}
