package Estaciones.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de la entidad Estacion")
public class EstacionDTO {
	
	@Schema(description = "Identificador de la estacion")
	private String id;
	
	@Schema(description = "Nombre de la estacion", example = "Estacion 1")
	private String nombre;

	@Schema(description = "Direccion de la estacion", example = "Calle Mayor, nº 1,")
	private String direccion;
	
	@Schema(description = "Cantidad de huecos disponibles dentro de la estacion", example = "Calle Mayor, nº 1,")
	private int huecos;
	
	@Schema(description = "Capacidad total, en bicicletas, de la estacion")
	private int capacidad;
	
	@Schema(description = "Coordenada latitud de la estacion")
	private double latitud;
	
	@Schema(description = "Coordenada longitud de la estacion")
	private double longitud;
	
	public EstacionDTO() {
		
	}
	
	public EstacionDTO(String id, String nombre, String direccion, int capacidad, int huecos, double latitud,
			double longitud) {
		this.id = id;
		this.nombre = nombre;
		this.direccion = direccion;
		this.huecos=huecos;
		this.capacidad = capacidad;
		this.latitud = latitud;
		this.longitud = longitud;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public int getCapacidad() {
		return capacidad;
	}
	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}

	public double getLatitud() {
		return latitud;
	}
	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	public double getLongitud() {
		return longitud;
	}
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public int gethuecos() {
		return huecos;
	}

	public void sethuecos(int huecos) {
		this.huecos = huecos;
	}
	
}
