package alquileres.modelo;

import java.time.Duration;
import java.time.LocalDateTime;

public class Alquiler {

	private String idBicicleta;
	private LocalDateTime inicio;
	private LocalDateTime fin;

	public Alquiler() {

	}

	public Alquiler(String idBicicleta) {
		this.idBicicleta = idBicicleta;
	}

	public boolean isActivo() {
		return fin == null;
	}
	
	public long getTiempo() { 
		if (inicio != null && fin != null) {
			return Duration.between(inicio, fin).toMinutes();
		} else if (inicio != null && fin == null){
			return Duration.between(inicio, LocalDateTime.now()).toMinutes();
		}else {
			return 0;
		}
	}

	public String getIdBicicleta() {
		return idBicicleta;
	}

	public void setIdBicicleta(String idBicicleta) {
		this.idBicicleta = idBicicleta;
	}

	public LocalDateTime getInicio() {
		return inicio;
	}

	public void setInicio(LocalDateTime inicio) {
		this.inicio = inicio;
	}

	public LocalDateTime getFin() {
		return fin;
	}

	public void setFin(LocalDateTime fin) {
		this.fin = fin;
	}
	
	@Override
	public String toString() {
		return "Alquiler [idBicicleta=" + idBicicleta + ", inicio=" + inicio + ", fin=" + fin + "]";
	}
}
