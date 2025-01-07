package alquileres.dto;

import java.time.LocalDateTime;

public class AlquilerDTO {
    private String idBicicleta;
    private LocalDateTime inicio;
    private LocalDateTime fin;

    // Constructor
    public AlquilerDTO(String idBicicleta, LocalDateTime inicio, LocalDateTime fin) {
        this.idBicicleta = idBicicleta;
        this.inicio = inicio;
        this.fin = fin;
    }
    
    public AlquilerDTO() {
    	
    }

    // Getters y setters
    public String getIdBicicleta() {
        return idBicicleta;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public LocalDateTime getFin() {
        return fin;
    }

	public void setIdBicicleta(String idBicicleta) {
		this.idBicicleta = idBicicleta;
	}

	public void setInicio(LocalDateTime inicio) {
		this.inicio = inicio;
	}

	public void setFin(LocalDateTime fin) {
		this.fin = fin;
	}
    
}
