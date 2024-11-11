package alquileres.modelo;

import java.time.LocalDateTime;

public class Reserva {

	public String idBicicleta;
	public LocalDateTime creada;
	public LocalDateTime caducidad;
	
	public Reserva() {
		
	}
	
	public Reserva(String idBicicleta, LocalDateTime creada, LocalDateTime caducidad) {
		this.idBicicleta = idBicicleta;
		this.creada = creada;
		this.caducidad = caducidad;
	}
	
	public String getIdBicicleta() {
		return idBicicleta;
	}
	public void setIdBicicleta(String idBicicleta) {
		this.idBicicleta = idBicicleta;
	}
	public LocalDateTime getCreada() {
		return creada;
	}
	public void setCreada(LocalDateTime creada) {
		this.creada = creada;
	}
	public LocalDateTime getCaducidad() {
		return caducidad;
	}
	public void setCaducidad(LocalDateTime caducidad) {
		this.caducidad = caducidad;
	}
	
	public boolean isCaducada() {
		if(LocalDateTime.now().isAfter(caducidad)) {
			return true;
		}
		return false;
	}
	
	public boolean isActiva() {
		if(!isCaducada()) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Reserva [idBicicleta=" + idBicicleta + ", creada=" + creada + ", caducidad=" + caducidad + "]";
	}
	

}
