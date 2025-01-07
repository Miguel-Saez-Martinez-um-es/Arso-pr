package alquileres.persistencia.jpa;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import repositorio.Identificable;

@Entity
@Table(name = "Alquiler")
public class AlquilerEntidad implements Serializable, Identificable {
	
	@Id
	private String id;

	private String idBicicleta;
	private LocalDateTime inicio;
	private LocalDateTime fin;


	public AlquilerEntidad() {
        this.id = UUID.randomUUID().toString();
	}

	public AlquilerEntidad(String idBicicleta) {
		this.idBicicleta = idBicicleta;
	}

	public long tiempo() {
		if (inicio != null && fin != null)
			return Duration.between(inicio, fin).toMinutes();
		else
			return 0;
	}

	public boolean isActivo() {
		return fin == null;
	}

	// Getters y setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	
}
