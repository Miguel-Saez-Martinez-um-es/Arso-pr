package alquileres.servicio;


public interface IServicioEstaciones {
	
    boolean tieneHuecoDisponible(String idEstacion);
    
    boolean situarBicicleta(String idBicicleta, String idEstacion);
    
}