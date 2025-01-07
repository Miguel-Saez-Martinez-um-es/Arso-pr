package alquileres.dto;

import java.time.LocalDateTime;

public class ReservaDTO {
	
    private String idBicicleta;
    private LocalDateTime creada;
    private LocalDateTime caducidad;

    public ReservaDTO(String idBicicleta, LocalDateTime creada, LocalDateTime caducidad) {
        this.idBicicleta = idBicicleta;
        this.creada = creada;
        this.caducidad = caducidad;
    }
    
    public ReservaDTO() {
    	
    }

    public String getIdBicicleta() {
        return idBicicleta;
    }

    public LocalDateTime getCreada() {
        return creada;
    }

    public LocalDateTime getCaducidad() {
        return caducidad;
    }

	public void setIdBicicleta(String idBicicleta) {
		this.idBicicleta = idBicicleta;
	}

	public void setCreada(LocalDateTime creada) {
		this.creada = creada;
	}

	public void setCaducidad(LocalDateTime caducidad) {
		this.caducidad = caducidad;
	}
    
}
