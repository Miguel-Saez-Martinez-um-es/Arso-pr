package alquileres.dto;

import java.util.List;

import alquileres.modelo.Roles;

public class UsuarioDTO {
    private String id;
    private List<ReservaDTO> reservas;
    private List<AlquilerDTO> alquileres;
    private Roles rol;
    private String password;
	

    
    // Constructor
    public UsuarioDTO(String id, List<ReservaDTO> reservas, List<AlquilerDTO> alquileres, Roles rol, String password) {
        this.id = id;
        this.reservas = reservas;
        this.alquileres = alquileres;
        this.rol=rol;
    }

    public UsuarioDTO() {
    	
    }
    
    // Getters y setters
    public String getId() {
        return id;
    }

    public List<ReservaDTO> getReservas() {
        return reservas;
    }

    public List<AlquilerDTO> getAlquileres() {
        return alquileres;
    }

	public void setId(String id) {
		this.id = id;
	}

	public void setReservas(List<ReservaDTO> reservas) {
		this.reservas = reservas;
	}

	public void setAlquileres(List<AlquilerDTO> alquileres) {
		this.alquileres = alquileres;
	}
    
    	public Roles getRol() {
		return rol;
	}

	public void setRol(Roles rol) {
		this.rol = rol;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
