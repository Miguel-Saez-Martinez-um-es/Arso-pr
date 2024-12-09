package Estaciones.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Estaciones.modelo.Estacion;
import Estaciones.servicio.IServicioEstacion;

@RestController
@RequestMapping("/estaciones")
public class EstacionesControladorRest {
	
	private IServicioEstacion servicio;

	@Autowired
	public EstacionesControladorRest(IServicioEstacion servicio) {
		this.servicio = servicio;
	}
	
	@GetMapping("/{id}")
	public Estacion getEstacionById(@PathVariable String id) throws Exception {
		Estacion estacion = this.servicio.getEstacion(id);
		// retorna el DTO
		return estacion;
	}
}
