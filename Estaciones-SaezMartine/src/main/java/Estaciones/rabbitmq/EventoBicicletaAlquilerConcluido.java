package Estaciones.rabbitmq;

public class EventoBicicletaAlquilerConcluido {
	private String idBicicleta;
	private String idEstacion;
	private String fechaFin;

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

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	@Override
	public String toString() {
		return "EventoBicicletaAlquilerConcluido [idBicicleta=" + idBicicleta + ", idEstacion=" + idEstacion
				+ ", fechaFin=" + fechaFin + "]";
	}

}