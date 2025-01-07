package retrofit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EstacionPOJO {

	private String id;
	private String nombre;
	private String direccion;
	private int capacidad;
	private int huecos;
	private double latitud;
	private double longitud;

	public EstacionPOJO() {
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

	public int getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public int getHuecos() {
		return huecos;
	}

	public void setHuecos(int huecos) {
		this.huecos = huecos;
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

	@Override
	public String toString() {
		return "Estacion [id=" + getId() + ", nombre=" + getNombre() + ", direccion=" + getDireccion() + ", capacidad=" + getCapacidad()
			+ ", huecos: " + getHuecos() + ", latitud=" + getLatitud() + ", longitud=" + getLongitud() + "]";
	}

}
