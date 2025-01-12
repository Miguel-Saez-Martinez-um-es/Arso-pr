package alquileres.dto;


public class ReservaDTO {
	
    private String idBicicleta;
    private String creada;
    private String caducidad;

    public ReservaDTO(String idBicicleta, String creada, String caducidad) {
        this.idBicicleta = idBicicleta;
        this.creada = creada;
        this.caducidad = caducidad;
    }
    
    public ReservaDTO() {
    	
    }

    public String getIdBicicleta() {
        return idBicicleta;
    }

    public String getCreada() {
        return creada;
    }

    public String getCaducidad() {
        return caducidad;
    }

	public void setIdBicicleta(String idBicicleta) {
		this.idBicicleta = idBicicleta;
	}

	public void setCreada(String creada) {
		this.creada = creada;
	}

	public void setCaducidad(String caducidad) {
		this.caducidad = caducidad;
	}
    
}
