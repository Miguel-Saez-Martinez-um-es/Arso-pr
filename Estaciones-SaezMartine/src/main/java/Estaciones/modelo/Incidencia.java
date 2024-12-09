package Estaciones.modelo;

import java.time.LocalDate;
import java.util.UUID;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "incidencias")
public class Incidencia {

	@Id
	private String id;

	
	private String operario;

	
	private String bicicleta;

	
	private LocalDate fechaCreacion;

	
	private Estado estado;

	
	private String descripcion;
	
	
	private String motivo;
	
	
	private LocalDate fechaCierre;
	
	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public LocalDate getFechaCierre() {
		return fechaCierre;
	}

	public void setFechaCierre(LocalDate fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public Incidencia() {
		this.id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBicicleta() {
		return bicicleta;
	}

	public void setBicicleta(String bicicleta) {
		this.bicicleta = bicicleta;
	}

	public LocalDate getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDate fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getOperario() {
		return operario;
	}

	public void setOperario(String operario) {
		this.operario = operario;
	}

	@Override
	public String toString() {
		return "Incidencia [id=" + id + ", operario=" + operario + ", bicicleta=" + bicicleta + ", fechaCreacion="
				+ fechaCreacion + ", estado=" + estado + ", descripcion=" + descripcion + ", motivo=" + motivo
				+ ", fechaCierre=" + fechaCierre + "]";
	}

}
