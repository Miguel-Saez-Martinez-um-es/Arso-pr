package Estaciones.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de la entidad Bicicleta")
public class BicicletaDTO {
	
	@Schema(description = "Identificador de la bicicleta")
	private String codigo;
	
	@Schema(description = "modelo de la Bicicleta", example = "Mountain Bike")
	private String modelo;
	
	@Schema(description = "Identificador de la estacion en la que se encuentra Bicicleta")
	private String estacion;



	public BicicletaDTO() {

	}

	public BicicletaDTO(String codigo, String modelo, String estacion) {
		this.codigo = codigo;
		this.modelo = modelo;
		this.estacion = estacion;
	}

	public String getCodigo() {
		return codigo;
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

	public String getEstacion() {
		return estacion;
	}

	public void setEstacion(String estacion) {
		this.estacion = estacion;
	}
}
