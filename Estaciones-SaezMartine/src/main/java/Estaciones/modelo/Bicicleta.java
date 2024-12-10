package Estaciones.modelo;

import java.time.LocalDate;
import java.util.UUID;



import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Document(collection = "bicicletas")
public class Bicicleta{

	@Id
	private String codigo;

	private String modelo;

	private LocalDate fechaAlta;

	
	private LocalDate fechaBaja;
	
	
	private String motivo;
	
	
	private String estacion;

	public Bicicleta() {
        this.codigo = UUID.randomUUID().toString();
	}

	public Bicicleta( String modelo, LocalDate fechaAlta, LocalDate fechaBaja, String motivo, String estacion) {
		this.codigo = UUID.randomUUID().toString();
		this.modelo = modelo;
		this.fechaAlta = fechaAlta;
		this.fechaBaja = fechaBaja;
		this.motivo = motivo;
		this.estacion=estacion;
	}


	public String getCodigo() {
		return codigo;
	}

	public String getEstacion() {
		return estacion;
	}

	public void setEstacion(String estacion) {
		this.estacion = estacion;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public LocalDate getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(LocalDate fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public LocalDate getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(LocalDate fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	@Override
	public String toString() {
		return "Bicicleta [codigo=" + codigo + ", modelo=" + modelo + ", fechaAlta=" + fechaAlta
				+ ", fechaBaja=" + fechaBaja + ", motivo=" + motivo + ", estacion=" + estacion + "]";
	}
	
	
}
