package Estaciones.modelo;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Document(collection = "Estaciones")
public class Estacion{


	@Id
    private String id;
	@NotNull
    private String nombre;
    private int capacidad;
    private String direccion;
	private double latitud;
	private double longitud;
	private LocalDate fechaAlta;
    
    public Estacion() {
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

	public LocalDate getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(LocalDate localDate) {
		this.fechaAlta = localDate;
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
		return "Estacion [id=" + id + ", nombre=" + nombre + ", capacidad=" + capacidad + ", direccion=" + direccion
				+ ", latitud=" + latitud + ", longitud=" + longitud + ", fechaAlta=" + fechaAlta +"]";
	}
    

    
}
