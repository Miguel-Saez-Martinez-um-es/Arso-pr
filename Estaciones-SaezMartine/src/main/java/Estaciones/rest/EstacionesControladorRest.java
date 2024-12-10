package Estaciones.rest;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.json.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Estaciones.DTO.BicicletaDTO;
import Estaciones.DTO.EstacionDTO;
import Estaciones.modelo.Bicicleta;
import Estaciones.modelo.Estacion;
import Estaciones.servicio.IServicioEstacion;
import io.swagger.v3.oas.annotations.tags.Tag;
import repositorio.RepositorioException;

@RestController
@RequestMapping("/estaciones")
@Tag(name = "Estaciones", description = "Aplicacion para la gestion de estaciones")
public class EstacionesControladorRest {

	private IServicioEstacion servicio;

	@Autowired
	private PagedResourcesAssembler<EstacionDTO> pagedResourcesAssembler;

	@Autowired
	private PagedResourcesAssembler<BicicletaDTO> pagedResourcesAssembler2;

	@Autowired
	public EstacionesControladorRest(IServicioEstacion servicio) {
		this.servicio = servicio;
	}

	/*
	Gestor: 
		Alta de estacion --> altaEstacion() 
		Alta de bicicleta --> altaBicicleta() 
		Baja de bicicleta Listado de bicicletas de una estacion
	
	Usuario: 
		Listado de estaciones --> getEstaciones() 
		Recuperar informacion de Estacion (sin bicicletas) --> getEstacionById() 
		Listado de bicicletas disponibles en una estacion --> getBicicletasDeEstacion() 
		Estacionar bicicleta --> estacionar bicicleta
	 */

	// Obtener una estacion concreta
	// curl -X GET http://localhost:8080/estaciones/{id}

	@GetMapping("/{id}")
	public EntityModel<EstacionDTO> getEstacionById(@PathVariable String id) throws Exception {
		Estacion e = this.servicio.getEstacion(id);
		EstacionDTO estacion = toEstacionDTO(e);
		// retorna el DTO

		EntityModel<EstacionDTO> model = EntityModel.of(estacion);
		model.add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(EstacionesControladorRest.class).getEstacionById(id)).withSelfRel());
		return model;
	}

	// Obtener todas las estaciones
	// curl -X GET http://localhost:8080/estaciones/
	@GetMapping
	public PagedModel<EntityModel<EstacionDTO>> getEstaciones(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "-1") int size) throws Exception {
		if (size <= 0) {
			size = Math.max(1, servicio.getEstaciones().size());

		}
		Pageable paginacion = PageRequest.of(page, size);
		Page<EstacionDTO> resultado = this.servicio.getListadoPaginadoEstaciones(paginacion);

		return pagedResourcesAssembler.toModel(resultado, estacion -> {
			EntityModel<EstacionDTO> model = EntityModel.of(estacion);
			try {
				model.add(WebMvcLinkBuilder.linkTo(
						WebMvcLinkBuilder.methodOn(EstacionesControladorRest.class).getEstacionById(estacion.getId()))
						.withSelfRel());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return model;
		});
	}

	// Obtener las bicicletas de una estacion
	// curl -X GET http://localhost:8080/estaciones/{id}/bicicletas

	@GetMapping("/{id}/bicicletas")
	public PagedModel<EntityModel<BicicletaDTO>> getBicicletasDeEstacion(@PathVariable String id,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "-1") int size) throws Exception {
		if (size <= 0) {
			size = Math.max(1, servicio.bicicletasEnEstacion(id));
		}
		Pageable paginacion = PageRequest.of(page, size);

		Page<BicicletaDTO> resultado = this.servicio.getListadoPaginadoBicicletas(paginacion, id);

		return pagedResourcesAssembler2.toModel(resultado, bicicleta -> {
			EntityModel<BicicletaDTO> model = EntityModel.of(bicicleta);
			/*try {
				model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EstacionesControladorRest.class)
						.getEstacionById(bicicleta.getCodigo())).withSelfRel());
			} catch (Exception e) {
				e.printStackTrace();
			}*/
			return model;
		});
	}

	/*
	Alta de una estacion
	
	curl -X POST -H "Content-Type: application/json" -d
	"{\"id\":\"123\",\"nombre\":\"Estación Central\",\"direccion\":\"Calle Principal 1\",\"capacidad\":10,\"latitud\":40.4168,\"longitud\":-3.7038}"
	http://localhost:8080/estaciones/altaestacion
	
	Postman: http://localhost:8080/estaciones/altaestacion 
	Json para probar desde postman 
	{"nombre": "Estación Central", "direccion": "Calle Principal 1",
	"capacidad": 10, "latitud": 40.4168, "longitud": -3.7038 }
	 */
	@PostMapping("/altaestacion")
	public EntityModel<EstacionDTO> altaEstacion(@RequestBody Map<String, Object> jsonBody) throws Exception {

		// Extraer los datos del JSON
		String nombre = (String) jsonBody.getOrDefault("nombre", "");
		String direccion = (String) jsonBody.getOrDefault("direccion", "");
		int capacidad = ((Number) jsonBody.getOrDefault("capacidad", 0)).intValue();
		double latitud = ((Number) jsonBody.getOrDefault("latitud", 0.0)).doubleValue();
		double longitud = ((Number) jsonBody.getOrDefault("longitud", 0.0)).doubleValue();

		if (direccion.isEmpty() || nombre.isEmpty()) {
			throw new IllegalArgumentException("Los nombre y direccion son obligatorios");
		}

		String idEstacion = servicio.altaEstacion(nombre, capacidad, direccion, latitud, longitud);
		EstacionDTO estacion = toEstacionDTO(servicio.getEstacion(idEstacion));

		EntityModel<EstacionDTO> model = EntityModel.of(estacion);
		return model;
	}

	/* Alta de bicicleta

		curl -i -X POST -H "Content-Type: application/json" \ -d "{"modelo":"Mountain Bike","estacion":"123"}" \ http://localhost:8080/estaciones/altabicicleta


			http://localhost:8080/estaciones/altabicicleta
		{
		  "modelo": "Mountain Bike",
		  "estacion": "123"
		}

	*/
	@PostMapping("/altabicicleta")
	public EntityModel<BicicletaDTO> altaBicicleta(@RequestBody Map<String, Object> jsonBody) throws Exception {

		// Extraer los datos del JSON
		String modelo = (String) jsonBody.getOrDefault("modelo", "");
		String idEstacion = (String) jsonBody.getOrDefault("estacion", "");

		if (modelo.isEmpty() || idEstacion.isEmpty()) {
			throw new IllegalArgumentException("Los campos id, nombre y direccion son obligatorios");
		}

		String codigo = servicio.altaBicicleta(modelo, idEstacion);
		BicicletaDTO bicicleta = new BicicletaDTO(codigo, modelo, idEstacion);

		EntityModel<BicicletaDTO> model = EntityModel.of(bicicleta);
		return model;
	}

	// Estacionar bicicleta

	@PutMapping("/{id}/estacionar/{idBicicleta}")
	public EntityModel<EstacionDTO> estacionarBicicleta(@PathVariable String id, @PathVariable String idBicicleta) throws Exception{
		servicio.estacionarBicicleta(idBicicleta, id);
		if(servicio.recuperarBicicleta(idBicicleta).getEstacion()!=id) {
			throw new Exception("No se ha podido estacionar la bicicleta");
		}else {
			
			Estacion e = servicio.getEstacion(id);
			EstacionDTO estacion = toEstacionDTO(e);
			EntityModel<EstacionDTO> model = EntityModel.of(estacion);
			return model;
		}
		
	}


	public EstacionDTO toEstacionDTO(Estacion estacion) throws RepositorioException {

		EstacionDTO dto = new EstacionDTO(estacion.getId(), estacion.getNombre(), estacion.getDireccion(),
				estacion.getCapacidad(), estacion.getLatitud(), estacion.getLongitud());
		return dto;
	}

	public BicicletaDTO toBicicletaDTO(Bicicleta bicicleta) {
		BicicletaDTO dto = new BicicletaDTO(bicicleta.getCodigo(), bicicleta.getModelo(), bicicleta.getEstacion());
		return dto;
	}
}
