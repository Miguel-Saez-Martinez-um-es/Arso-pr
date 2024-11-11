package alquileres.modelo;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import repositorio.Identificable;

public class Usuario implements Identificable{

	public String id;
	public List<Reserva> reservas;
	public List<Alquiler> alquileres;

	public Usuario() {
		this.reservas = new LinkedList<Reserva>();
		this.alquileres = new LinkedList<Alquiler>();
	}
	
	public Usuario(String id) {
		this.id=id;
		this.reservas = new LinkedList<Reserva>();
		this.alquileres = new LinkedList<Alquiler>();
	}
	
	public List<Reserva> getReservasCaducadas(){
		LinkedList<Reserva> lista = new LinkedList<Reserva>();
		for (Reserva r : reservas) {
			if(r.isCaducada()) {
				lista.add(r);
			}	
		}
		return lista;
	}
	
	public long tiempoUsoHoy(){
		long total = 0; 
		for(Alquiler a : alquileres){
			if(a.getInicio().isAfter(LocalDate.now().atStartOfDay())) {
				total+=a.getTiempo();
			}
		}
		return total;
	}
	
	public int tiempoUsoSemana() {
		int total = 0;
		for (Alquiler a : alquileres) {
			if (a.getInicio().isAfter(LocalDate.now().atStartOfDay().minusDays(7))) {
				total += a.getTiempo();
			}
		}
		return total;
	}
	
	public boolean superaTiempo() {
		if(tiempoUsoHoy() >= 60 || tiempoUsoSemana() >= 180) {
			return true;
		}
		return false;
	}
	
	public Reserva reservaActiva() {
		if(!reservas.isEmpty()) {
			Reserva r = reservas.get(reservas.size() - 1);
			if (r.isActiva()) {
				return r;
			}	
		}
		return null;
	}
	
	public Alquiler alquilerActivo() {
		if(!alquileres.isEmpty()) {
			Alquiler a = alquileres.get(alquileres.size() - 1);
			if (a.isActivo()) {
				return a;
			}
		}
		return null;
	}
	
	public boolean bloqueado() {
		return getReservasCaducadas().size() >= 3;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Reserva> getReservas() {
		return reservas;
	}

	public void setReservas(List<Reserva> reservas) {
		this.reservas = reservas;
	}

	public List<Alquiler> getAlquileres() {
		return alquileres;
	}

	public void setAlquileres(List<Alquiler> alquileres) {
		this.alquileres = alquileres;
	}
	
	public void addReserva(Reserva reserva) {
		this.reservas.add(reserva);
	}
	
	public void removeReserva(Reserva reserva) {
		if(this.reservas.contains(reserva)) {
			this.reservas.remove(reserva);
		}
	}
	
	public void addAlquiler(Alquiler alquiler) {
		this.alquileres.add(alquiler);
	}
	
	public void removeAlquiler(Alquiler alquiler) {
		if(this.alquileres.contains(alquiler)) {
			this.alquileres.remove(alquiler);
		}
	}
	
	@Override
	public String toString() {
		return "Usuario [id=" + id + ", reservas=" + reservas + ", alquileres=" + alquileres + "]";
	}
	
	
}
