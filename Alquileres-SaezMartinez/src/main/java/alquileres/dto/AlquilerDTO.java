package alquileres.dto;


public class AlquilerDTO {
    private String idBicicleta;
    private String inicio;
    private String fin;

    // Constructor
    public AlquilerDTO(String idBicicleta, String inicio, String fin) {
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

    public String getInicio() {
        return inicio;
    }

    public String getFin() {
        return fin;
    }

	public void setIdBicicleta(String idBicicleta) {
		this.idBicicleta = idBicicleta;
	}

	public void setInicio(String inicio) {
		this.inicio = inicio;
	}

	public void setFin(String fin) {
		this.fin = fin;
	}
    
}
