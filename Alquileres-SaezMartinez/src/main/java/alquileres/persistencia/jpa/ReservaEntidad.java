package alquileres.persistencia.jpa;

import java.io.Serializable; 
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import repositorio.Identificable;

@Entity
@Table(name = "Reserva")
public class ReservaEntidad implements Serializable, Identificable {
	@Id
	private String id;

	private String idBicicleta;
	private LocalDateTime creada;
	private LocalDateTime caducidad;
	
	/*
	@ManyToOne
	@JoinColumn(name = "usuario_id")
    private String idUsuario;
	*/
	
	public ReservaEntidad(String idBicicleta, LocalDateTime creada, LocalDateTime caducidad) {
		this.idBicicleta = idBicicleta;
		this.creada = creada;
		this.caducidad = caducidad;
	}

	public ReservaEntidad() {
        this.id = UUID.randomUUID().toString();
	}

	public boolean isActiva() {
		return !isCaducada();
	}

	public boolean isCaducada() {
		if (LocalDateTime.now().isAfter(caducidad))
			return true;
		else
			return false;
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

	/*
	public String getUsuario() {
		return idUsuario;
	}

	public void setUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}
	*/
}
