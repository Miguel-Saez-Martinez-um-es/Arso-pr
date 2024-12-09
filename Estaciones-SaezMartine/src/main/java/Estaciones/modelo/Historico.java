package Estaciones.modelo;


import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "historicos")
public class Historico {

	@Id
    private String id;
	private String idBicicleta;
	private String idEstacion;
	private LocalDate inicio;
	private LocalDate fin;
	
	public Historico() {
		
	}

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

	public String getIdEstacion() {
		return idEstacion;
	}

	public void setIdEstacion(String idEstacion) {
		this.idEstacion = idEstacion;
	}

	public LocalDate getInicio() {
		return inicio;
	}

	public void setInicio(LocalDate inicio) {
		this.inicio = inicio;
	}

	public LocalDate getFin() {
		return fin;
	}

	public void setFin(LocalDate fin) {
		this.fin = fin;
	}

	@Override
	public String toString() {
		return "Historico [id=" + id + ", idBicicleta=" + idBicicleta + ", idEstacion=" + idEstacion + ", inicio="
				+ inicio + ", fin=" + fin + "]";
	}
	
	
}
