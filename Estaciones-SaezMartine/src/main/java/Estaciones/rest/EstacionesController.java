package Estaciones.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Estaciones.servicio.IServicioEstacion;

@RestController
@RequestMapping("/estaciones")
public class EstacionesController {
	
	private IServicioEstacion servicio;

	@Autowired
	public EstacionesController(IServicioEstacion servicio) {
	this.servicio = servicio;
	}
}
