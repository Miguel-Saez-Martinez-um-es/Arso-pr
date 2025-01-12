package alquileres.servicio;

import java.util.Map;

public interface IServicioVerificacion {

	Map<String, Object> verificarCredenciales(String username, String password);

	Map<String, Object> verificarCredencialesGestor(String username, String password);

}
